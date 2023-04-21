package ch.bbv.dnm.users.server;

import static ch.bbv.dnm.users.common.UserApplicationConstants.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.ORB;
import ch.bbv.application.ComponentException;
import ch.bbv.application.Application;
import ch.bbv.cmdlineparser.CmdLineBuilder;
import ch.bbv.dnm.users.TypeFactory;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.LightweightObjectHandlerImpl;
import ch.bbv.dog.persistence.PersistenceHandler;
import ch.bbv.dog.persistence.ojb.PersistenceHandlerOjb;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import static ch.bbv.dnm.users.common.UserApplicationConstants.*;

/**
 * The class defines the server application connecting to the database and to
 * the servers. The application is configured to support 2 tiers, CORBA client
 * server and J2EE client server modes through start-up parameters.
 * @author MarcelBaumann
 * @version $Revision: 1.6 $
 */
public class UserApplication extends Application {

    /**
   * Logger of the application class.
   */
    private static Log log = LogFactory.getLog(Application.class);

    /**
   * The argument list of the application from the command line.
   */
    private static String[] args;

    /**
   * Returns the client application singleton instance. The application uses the
   * locator pattern.
   * @return the client application unique instance
   */
    public static UserApplication getApplication() {
        return (UserApplication) Application.getApplication();
    }

    /**
   * Entry point the client application. The arguments of the application
   * specify the mode.
   * @param args arguments of the client application
   */
    public static void main(String[] args) {
        UserApplication.args = args;
        Options options = createCommandLine();
        Parser parser = new PosixParser();
        try {
            CommandLine line = parser.parse(options, args, true);
            if (line.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("pmMDA", options);
                return;
            }
            if (line.hasOption("v")) {
                log.info("logging level set to verbose");
            }
            if (line.hasOption("q")) {
                log.info("logging level set to quiet");
            }
            String mode = line.getOptionValue("m", USE_DIRECT);
            mode = (mode == null ? USE_DIRECT : mode);
            String synchronization = line.getOptionValue("s", USE_REPLACE).toUpperCase();
            synchronization = (synchronization == null ? USE_REPLACE : synchronization);
            if (log.isInfoEnabled()) {
                log.info(Arrays.asList(args));
            }
            new UserApplication(mode, "REPLACE");
        } catch (ComponentException e) {
            log.fatal(e);
        } catch (ParseException e) {
            log.fatal(e);
        }
    }

    /**
   * Default constructor used in the EJB generated source artifacts for the
   * session bean.
   * @throws ComponentException if an error occured creating the client
   *         application
   */
    public UserApplication() throws ComponentException {
        this(USE_J2EE, "REPLACE");
    }

    /**
   * Default constructor of the class.
   * @param mode mode of the client application
   * @param synchronization synchronization mode of the O/R system
   * @throws ComponentException if an error occured creating the client
   *         application
   * @pre (mode != null) && (synchronization != null)
   */
    public UserApplication(String mode, String synchronization) throws ComponentException {
        assert (mode != null) && (synchronization != null);
        DataObjectMgr manager = null;
        ORB orb = null;
        register(this);
        PersistenceHandler.Mode sync = PersistenceHandler.Mode.valueOf(synchronization);
        if (mode.equalsIgnoreCase(USE_DIRECT)) {
            log.info("Application started in direct mode");
            manager = new DataObjectMgr(new PersistenceHandlerOjb(false, sync), new LightweightObjectHandlerImpl(), null, SCHEMA);
            setDataObjectMgr(manager);
        } else if (mode.equalsIgnoreCase(USE_CORBA)) {
            log.info("Application started in CORBA mode");
            manager = new DataObjectMgr(new PersistenceHandlerOjb(false, sync), new LightweightObjectHandlerImpl(), null, SCHEMA);
            setDataObjectMgr(manager);
            orb = ORB.init(args, null);
            register(orb, ORB_NAME);
            createCorbaServer("ch.bbv.dnm.users.server.UsersServerCorba");
        } else if (mode.equalsIgnoreCase(USE_J2EE)) {
            log.info("Application started in J2EE mode");
            manager = new DataObjectMgr(new PersistenceHandlerOjb(true, sync), new LightweightObjectHandlerImpl(), null, SCHEMA);
            setDataObjectMgr(manager);
        }
        TypeFactory factory = new TypeFactory(manager);
        factory.registerAllTypes();
        initialize();
        startup();
        manager.getDataObjectHandler().openDatasource(SCHEMA, false);
        if (mode.equalsIgnoreCase(USE_CORBA)) {
            orb.run();
        }
    }

    /**
   * Returns the CORBA object request broker of the application.
   * @return the ORB of the application
   */
    public ORB getOrb() {
        return (ORB) getSubsystem(ORB_NAME);
    }

    /**
   * Creates the CORBA server using reflection to avoid compile dependencies to
   * the CORBA libraries.
   * @param qualifiedName qualified name of the corba wrapper server
   */
    protected final void createCorbaServer(String qualifiedName) {
        try {
            Class clazz = Class.forName(qualifiedName);
            Method method = clazz.getMethod("create");
            method.invoke(null);
        } catch (ClassNotFoundException e) {
            log.fatal(e);
        } catch (SecurityException e) {
            log.fatal(e);
        } catch (NoSuchMethodException e) {
            log.fatal(e);
        } catch (IllegalArgumentException e) {
            log.fatal(e);
        } catch (IllegalAccessException e) {
            log.fatal(e);
        } catch (InvocationTargetException e) {
            log.fatal(e);
        }
    }
}
