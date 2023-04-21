package jhomenet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import jhomenet.auth.LoginManager;
import jhomenet.gui.MainView;
import jhomenet.hw.management.*;
import jhomenet.hw.pollers.*;
import jhomenet.responsive.*;
import jhomenet.system.*;
import jhomenet.shell.*;
import jhomenet.shell.server.SystemInputStream;
import jhomenet.shell.server.SystemPrintStream;
import org.apache.log4j.*;
import org.jdesktop.swingx.JXLoginPanel;

/**
 * The main jHomenet server class.
 * <br>
 * Id: $Id: JHomeNetServer.java 1099 2005-12-30 22:56:28Z dhirwinjr $
 * 
 * @author David Irwin
 */
public class JHomeNetServer implements SystemInterface {

    /**
     * Define the root logger.
     */
    private static Logger logger = Logger.getLogger(JHomeNetServer.class.getName());

    /**
     * Used for singleton references.
     */
    private static JHomeNetServer instance = null;

    /**
     * Reference to the resource bundle used for retrieving properties.
     */
    private static ResourceBundle resources = ResourceBundle.getBundle("jhomenet.resource.Resources");

    /**
     * The release build ID.
     */
    public static final String buildId = resources.getString("project.version");

    /**
     * Reference to the command shell. The command shell is responsible for parse command line
     * inputs from the administrator.
     */
    private static Shell shell = null;

    /**
     * Flag used to indicate whether the shell should be enabled.
     */
    private static boolean enableShell = false;

    /**
     * Define a variable for storing the start time of the server.
     */
    private static Calendar _startTime = null;

    /**
     * A reference to the sensor responsive scheduler
     */
    private ResponsiveScheduler responsiveScheduler;

    /**
     * Default constructor.
     */
    private JHomeNetServer() {
    }

    /**
     * Get an instance of the jHomenet server.
     *
     * @return A reference to the server object
     */
    public static JHomeNetServer getInstance() {
        if (instance == null) {
            instance = new JHomeNetServer();
        }
        return instance;
    }

    /**
     * The server main starting point.
     * 
     * @param args A list of the command line arguments
     */
    public static void main(String[] args) throws Exception {
        initializeConfiguration(args);
        final JHomeNetServer server = getInstance();
        server.loadProperties();
        server.startServer();
    }

    private void startServer() throws Exception {
        logger.info("Starting server");
        logger.info(resources.getString("project.name") + " [version " + buildId + "]");
        logger.info(resources.getString("project.copyright"));
        logger.info("Hardware config file: " + ServerProperties.getProperty(ServerProperties.PropertyNames.HwConfigFile.toString()));
        logger.info("Server start time: " + getStartTime());
        buildModules();
        MainView mainView = MainView.createAndShowGUI(true);
        JXLoginPanel.Status loginStatus = LoginManager.showLoginDialog(mainView.getMainFrame());
        logger.debug("Authentication status: " + loginStatus.toString());
        if (loginStatus != JXLoginPanel.Status.SUCCEEDED) {
            quit();
        }
        initializeModules();
        startModules();
    }

    /**
     * Build any necessary jHomenet modules.
     */
    private void buildModules() {
        logger.info("Building server modules");
        LoginManager.instance();
        HardwareManager.instance().setRegistry(HardwareRegistry.instance());
        responsiveScheduler = ResponsiveScheduler.getInstance();
        if (enableShell) {
            shell = JHomenetShell.getInstance();
        }
    }

    /**
     * Initialize the jHomeNet modules.
     */
    private void initializeModules() {
        logger.info("Initializing server modules");
        PollingService.instance().initializeService();
        HardwareManager.instance().initialize();
    }

    /**
     * Start the necessary jHomeNet server m
     */
    private void startModules() {
        logger.info("Starting server modules");
        PollingService.instance().startService();
        responsiveScheduler.startService();
    }

    /**
     * Initialize the configuration using the command line arguments.
     * 
     * @param cmdLineArgs A list of the command line arguments
     */
    private static void initializeConfiguration(String[] cmdLineArgs) {
        _startTime = GregorianCalendar.getInstance();
        _startTime.setTimeInMillis(System.currentTimeMillis());
        PropertyConfigurator.configure("conf\\log4j.properties");
        parseCommandLine(cmdLineArgs);
    }

    /**
     * Parse the command line arguments.
     * 
     * @param args The command line arguments.
     */
    private static void parseCommandLine(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h") || args[i].equals("-help")) {
                printUsage();
                System.exit(0);
            } else if (args[i].equals("-s") || args[i].equals("-shell")) {
                enableShell = true;
            } else if (args[i].equals("-c") || args[i].equals("-conf")) {
                ServerProperties.setConfigFilename(args[++i]);
            } else {
                System.out.println("Unknown command line argument: " + args[i]);
                printUsage();
                System.exit(0);
            }
        }
    }

    /**
     * Get the server start time.
     * 
     * @return The server start time
     */
    public static String getStartTime() {
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss");
        return format.format(_startTime.getTime());
    }

    /**
     * Load the properties from the properties file.
     */
    public void loadProperties() {
        logger.info("Loading server properties");
        try {
            ServerProperties.loadProperties();
            ServerProperties.printProperties();
        } catch (Throwable exc) {
            logger.error("Failed to load server properties");
        }
        logger.info("Server properties loaded");
    }

    /**
     * Quit the server application.
     */
    public void quit() {
        logger.debug("Quitting server application");
        System.exit(0);
    }

    /**
     * Print the command line server usage.
     */
    public static void printUsage() {
        System.out.println("");
        System.out.println("Running jHomenet server application");
        System.out.println("Usage: java jhomenet.JHomeNetServer [-options]");
        System.out.println("");
        System.out.println("Where options include:");
        System.out.println("  -h, -help                    print this help information");
        System.out.println("  -s, -shell                   enable the command shell");
        System.out.println("  -c, -conf <config filename>  set the configuration filename");
    }

    public static String getShellName() {
        return shell.getName();
    }

    public static String getShellVersion() {
        return shell.getVersion();
    }

    public static void setShell(JHomenetShell shell) {
        JHomeNetServer.shell = shell;
    }

    /**
     * Used to execute shell commands.
     * 
     * @param commandLine
     * @param in
     * @param out
     * @param err
     * @param environment
     * @throws Exception
     */
    public void execute(Object[] commandLine, SystemInputStream in, SystemPrintStream out, SystemPrintStream err, HashMap environment) throws Exception {
        logger.debug("executing a command");
        if (shell != null) {
            shell.execute(commandLine, in, out, err, environment);
        } else {
            logger.error("Can't execute command -> shell set to null");
        }
    }

    /**
     * @see jhomenet.shell.SystemInterface#getShell()
     */
    public Shell getShell() {
        return shell;
    }

    /**
     * @see jhomenet.shell.SystemInterface#getUptime()
     */
    public String getUptime() {
        return Utils.elapsedTime(_startTime.getTimeInMillis(), System.currentTimeMillis());
    }
}
