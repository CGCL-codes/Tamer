package org.wuhsin.canon.app;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuhsin.canon.AppError;
import org.wuhsin.canon.commandline.CommandLine;
import org.wuhsin.canon.commandline.CommandLineArgumentError;
import org.wuhsin.canon.commandline.Result;
import org.wuhsin.canon.events.Event;
import org.wuhsin.canon.events.annotations.EventHandler;
import org.wuhsin.canon.events.annotations.EventHandler.Action;
import org.wuhsin.canon.lifecycle.Lifecycle;
import org.wuhsin.canon.preferences.PreferenceError;
import org.wuhsin.canon.preferences.Preferences;

/**
 * A foundation class for creating applications that have a standard lifecycle
 * and set of features. It specifically supports the use of annotations in the
 * subclasses to signal additional commandline processing to be done as well as
 * what data elements need to be stored as preferences.
 * 
 * @author jmochel
 */
public abstract class AppBase implements Lifecycle {

    /**
     * this abstract method is overloaded by the children classes with the
     * actual business logic that uses the already process the preferences and
     * commandline arguments
     */
    public abstract void go() throws AppError;

    /**
     * The main method of the application base. it collects the configuration
     * items for commandline processing and preferences as annotated in the
     * subclasses and then runs through the lifecycle of the application.
     * <p>
     * The lifecycle is the following:
     * Initialization - Extract configuration from the application class itself.
     * Startup Go Shutdown Deinitialization
     * 
     * @param app
     * @param args
     * @throws CommandLineArgumentError
     * @throws AppError
     */
    public static <X extends AppBase> void launch(Class<X> appClass, String[] args) throws CommandLineArgumentError, AppError {
        X app = initializeFramework(appClass, args);
        app.distributor.subscribe(app);
        app.distributor.publish(new Event(INIT, "Framework invoked initialization"));
        app.distributor.publish(new Event(STARTUP, "Framework invoked startup"));
        app.go();
        app.distributor.publish(new Event(SHUTDOWN, "Framework invoked shutdown"));
        app.distributor.publish(new Event(DEINIT, "Framework invoked deinitialization"));
        deinitializeFramework(app);
    }

    @EventHandler(action = Action.After, eventNames = STARTUP)
    protected void startUp() throws AppError, CommandLineArgumentError {
        startTime = System.currentTimeMillis();
        recoverStoredStateFromPreferences();
        Result result = commandLine.parse(applicationArgs);
        if (result != Result.ISOK) {
            distributor.publish(new Event(FAILURE, result.getMsg()));
        }
    }

    @EventHandler(action = Action.After, eventNames = SHUTDOWN)
    protected void shutDown() throws AppError {
        endTime = System.currentTimeMillis();
        storeStateToPreferences();
    }

    @EventHandler(action = Action.Before, eventNames = SUCCESS)
    protected void handleSuccess(Event event) {
        logger.info("Application succeeded with a final message of: " + event.getMessage());
        System.exit(0);
    }

    @EventHandler(action = Action.Before, eventNames = FAILURE)
    protected void handleFailure(Event event) {
        logger.error("Application failed with a final message of: " + event.getMessage());
        System.exit(-1);
    }

    @EventHandler(action = Action.Before, eventNames = EPICFAILURE)
    protected void handleEpicFailure(Event event) {
        logger.error("Application failed with a final message of: " + event.getMessage());
        System.exit(-2);
    }

    /**
     * Initializes the command line and preferences from the annotated
     * subclasses.
     */
    private static <T extends AppBase> T initializeFramework(Class<T> clazz, String[] args) throws CommandLineArgumentError, AppError {
        String configFileName = null;
        logger.debug("Initializing the Application Framework");
        logger.debug("Preprocessing the command line arguments. Intercepting the -config argument.");
        List<String> filteredArgList = new ArrayList<String>();
        for (String arg : args) {
            if (arg.startsWith("-config=")) {
                int offsetNdx = "-config=".length();
                configFileName = arg.substring(offsetNdx);
            } else {
                filteredArgList.add(arg);
            }
        }
        logger.debug("Creating an instance of the application and configuring it.");
        String[] sampleArray = {};
        applicationArgs = filteredArgList.toArray(sampleArray);
        T app = null;
        try {
            app = (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new AppError(e);
        } catch (IllegalAccessException e) {
            throw new AppError(e);
        }
        app = readInXMLConfigFile(clazz, app, configFileName);
        app.startTime = System.currentTimeMillis();
        try {
            InetAddress address = InetAddress.getLocalHost();
            app.machineId = address.getHostName() + "(" + address.getHostAddress() + ")";
        } catch (UnknownHostException e) {
            throw new AppError(e);
        }
        commandLine = new CommandLine(app);
        try {
            preferences = new Preferences(app);
        } catch (PreferenceError e) {
            throw new AppError(e);
        }
        return app;
    }

    /**
     * Deinitializes the application
     * 
     * @param <T>
     * @param app
     * @throws AppError
     */
    private static <T extends AppBase> void deinitializeFramework(T app) throws AppError {
        logger.debug("De-initializing the Application Framework");
        writeOutToXMLConfigFile(app);
    }

    /**
     * recovers the stored state of the application
     * 
     * @param <T>
     * @param app
     * @throws AppError
     */
    private void recoverStoredStateFromPreferences() throws AppError {
        try {
            logger.debug("Recovering any persistent variables that need to be available for this run.");
            preferences.restore();
        } catch (PreferenceError e) {
            throw new AppError(e);
        }
    }

    /**
     * @param <T>
     * @param clazz
     * @param currentApp
     * @return
     * @throws AppError
     */
    private static <T> T readInXMLConfigFile(Class<T> clazz, T currentApp, String configFileNameForOverride) throws AppError {
        T newApp = currentApp;
        Annotation annotation = clazz.getAnnotation(XmlRootElement.class);
        if (annotation != null) {
            try {
                JAXBContext jc = JAXBContext.newInstance(clazz);
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                File persistentStateFile = null;
                if (configFileNameForOverride != null) {
                    persistentStateFile = new File(configFileNameForOverride);
                    if (!persistentStateFile.exists()) {
                        logger.error("The configuration file you specified on the command line (" + persistentStateFile.getAbsolutePath() + ") was not found. The application will continue on using the defaslts");
                        persistentStateFile = new File(clazz.getSimpleName() + ".xml");
                    }
                } else {
                    persistentStateFile = new File(clazz.getSimpleName() + ".xml");
                }
                if (persistentStateFile.exists()) {
                    logger.debug("Instantiating the application using the configuration stored in " + persistentStateFile.getAbsolutePath());
                    newApp = (T) unmarshaller.unmarshal(persistentStateFile);
                }
            } catch (Exception e) {
                throw new AppError(e);
            }
        }
        return newApp;
    }

    private static void writeOutToXMLConfigFile(Object obj) throws AppError {
        try {
            JAXBContext jc = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jc.createMarshaller();
            File persistentStateFile = new File(obj.getClass().getSimpleName() + ".xml");
            logger.debug("Storing any persistent configuration into " + persistentStateFile.getAbsolutePath());
            marshaller.marshal(obj, persistentStateFile);
        } catch (JAXBException e) {
            throw new AppError(e);
        }
    }

    /**
     * Stores the application state
     * 
     * @param <T>
     * @param app
     * @throws AppError
     */
    private void storeStateToPreferences() throws AppError {
        try {
            logger.debug("Storing any persistent variables that need to be available for the next run.");
            preferences.save();
        } catch (PreferenceError e) {
            throw new AppError(e);
        }
    }

    public void publish(Event event) {
        distributor.publish(event);
    }

    /**
     * the standard logger
     */
    private static Logger logger = LoggerFactory.getLogger(AppBase.class.getSimpleName());

    /**
     * The commandline processor.
     */
    private static CommandLine commandLine;

    /**
     * The commandline arguments.
     */
    private static String[] applicationArgs;

    /**
     * The preferences 
     */
    private static Preferences preferences;

    /**
     * Start time in Milliseconds
     */
    @Getter
    @Setter
    protected Long startTime = 0L;

    /**
     * End time in Milliseconds
     */
    @Getter
    @Setter
    protected Long endTime = 0L;

    @Getter
    protected String machineId = "";

    private Distributor distributor = new Distributor();
}
