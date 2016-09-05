package eu.arcadia.sample;

import eu.arcadia.agentglue.ChainingInfo;
import eu.arcadia.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.logging.Logger;


/**
 * Created by nikos on 26/5/2016.
 */
@ArcadiaComponent(componentname = "Component33",componentversion = "0.0.1",componentdescription = "A dummy leaf component",tags={"dummy","leaf"})
@ArcadiaMetric(name="sentRequests",description = "Total number of requests triggered in the context of the Service Graph",unitofmeasurement = "integer",valuetype = ValueType.String,maxvalue = "2147483648",minvalue = "0",higherisbetter = false)
@ArcadiaMetric(name="receivedRequests",description = "Number of requests received from the connected component",unitofmeasurement = "integer",valuetype = ValueType.String,maxvalue = "2147483648",minvalue = "0",higherisbetter = false)
@ArcadiaConfigurationParameter(name = "test",description = "test",parametertype = ParameterType.String,defaultvalue = "test", mutableafterstartup = false)
@DependencyExport(CEPCID = "dummy_inf_connection",allowsMultipleTenants = true)
public class Component {

    private static final Logger logger = Logger.getLogger(Component.class.getName());

    public static ConfigurableApplicationContext appContext;

    /*
    ArcadiaConfigurationParameter setter/getter
    */
    public static String getTest(){
        //application logic
        return  "test";
    }

    /*
    ArcadiaMetrics (getters only)
    */
    public static String getSentRequests(){
        //application logic
        return System.getProperty("Component.sentRequests");
    }

    public static String getReceivedRequests(){
        //application logic
        return System.getProperty("Component.receivedRequests");
    }

    /*
    Component Lifecycle Management
    */
    @LifecycleInitialize
    public static void init(){
        System.setProperty("Component.sentRequests", String.valueOf(0));
        System.setProperty("Component.receivedRequests", String.valueOf(0));
        System.setProperty("app.port",Application.defaultPort.toString());
        logger.info("----INIT END---");
    }

    @LifecycleStart
    public static String start(){
        if(appContext == null) {
            appContext = SpringApplication.run(new Class[]{Application.class, CustomizationBean.class}, new String[]{});
        }else logger.severe("AppContext is not null ! Application is already started !");
        logger.info("----START END---");
        return String.valueOf(appContext.isActive());
    }

    @LifecycleStop
    public static String stop(){
        if(appContext != null) {
            SpringApplication.exit(appContext);
            appContext.close();
            appContext = null;
        }else logger.severe("AppContext is null ! Application has not been started !");
        return String.valueOf((appContext == null));

    }


    /*
    DependencyExport-related methods
    */
    public static String getUri(){
        return Application.getterURI;
    }

    public static String getPort(){
        return  System.getProperty("app.port");
    }

    /*
    Handle the bidning
    */
    @DependencyResolutionHandler(CEPCID = "dummy_inf_connection")
    public static void  bindedRootComponent(ChainingInfo chainingInfo){
        logger.info("BINDED COMPONENT:"+chainingInfo.toString());
    }

}