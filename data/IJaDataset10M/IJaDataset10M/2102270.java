package com.innovative.main;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.innovative.plugins.Loader;
import com.innovative.util.FileUtils;
import java.util.concurrent.Semaphore;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.DustSkin;

/**
 * A singleton class that initializes and executes a new instance of {@link MainFrame}.
 * 
 * @author Dylon Edwards 
 * @since 0.2
 */
public final class Main extends AbstractMain {

    /** Holds Logger utility for the application */
    private static final Logger logger = Logger.getLogger(Main.class);

    /** Holds whether {@link #instance} is running in production mode */
    public static final int PRODUCTION = 0x1 << 0;

    /** Holds whether {@link #instance} is running in staging mode */
    public static final int STAGING = 0x1 << 1;

    /** Holds whether {@link #instance} is running in testing mode */
    public static final int TESTING = 0x1 << 2;

    /** Holds whether {@link #instance} is running in development mode */
    public static final int DEVELOPMENT = 0x1 << 3;

    /** Holds whether {@link #instance} is running in debugging mode */
    public static final int DEBUGGING = 0x1 << 4;

    /**
	 * Holds the current mode of {@link #instance}. By default, this is {@link #DEVELOPMENT}, {@link #TESTING}, and
	 * {@link #DEBUGGING}.
	 *
	 * @see #PRODUCTION
	 * @see #DEVELOPMENT
	 * @see #STAGING
	 * @see #TESTING
	 * @see #DEBUGGING
	 */
    private static int mode = DEVELOPMENT | TESTING | DEBUGGING;

    /** Holds the singleton instance of this class, which is instantiated in {@link MainRunner#run()} */
    private static Main instance;

    private static final long serialVersionUID = 7378906454285101805L;

    /** Holds the current temporary directory for processing documents */
    private static final File tmpdir = FileUtils.getFile(TMP_PATH, name + "-tmp");

    private static final TaskCoordinator coordinator = new TaskCoordinator();

    /** Holds the timestamp of when the application was initialized */
    private final long timestamp = System.currentTimeMillis();

    /** Holds the {@link com.innovative.plugins.PlugInLoader} for this application */
    protected final Loader loader = new Loader(frame);

    /**
	 * Constructs a new Main object which configures and instantiates the {@link MainFrame}
	 *
	 * @param args The command-line arguments supplied upon application execution
	 */
    private Main(final String... args) {
    }

    /**
	 * Returns the singleton instance of this class
	 *
	 * @return {@link #instance}
	 */
    public static Main getInstance() {
        return instance;
    }

    public static TaskCoordinator getCoordinator() {
        return coordinator;
    }

    public static File getTmpDir() {
        return tmpdir;
    }

    /**
	 * Configures the runtime mode of the {@link #instance}.
	 *
	 * @param modes The modes with which to configure the running {@link #instance}.
	 * @see #PRODUCTION
	 * @see #DEVELOPMENT
	 * @see #STAGING
	 * @see #TESTING
	 * @see #DEBUGGING
	 */
    public static void setMode(final int... modes) {
        Main.mode = 0;
        for (final int mode : modes) {
            Main.mode |= mode;
        }
    }

    public static void addMode(final int mode) {
        Main.mode |= mode;
    }

    public static void removeMode(final int mode) {
        Main.mode ^= mode;
    }

    public static void unsetMode() {
        Main.mode = 0;
    }

    /**
	 * Returns the current runtime mode of {@link #instance}
	 *
	 * @return {@link #mode}
	 * @see #PRODUCTION
	 * @see #DEVELOPMENT
	 * @see #STAGING
	 * @see #TESTING
	 * @see #DEBUGGING
	 */
    public static int getMode() {
        return mode;
    }

    /**
	 * Returns whether the application is running in {@link #PRODUCTION} mode.
	 *
	 * @return Whether the application is running in {@link #PRODUCTION} mode.
	 */
    public static boolean production() {
        return (mode & PRODUCTION) != 0;
    }

    /**
	 * Returns whether the application is running in {@link #STAGING} mode.
	 *
	 * @return Whether the application is running in {@link #STAGING} mode.
	 */
    public static boolean staging() {
        return (mode & STAGING) != 0;
    }

    /**
	 * Returns whether the application is running in {@link #TESTING} mode.
	 *
	 * @return Whether the application is running in {@link #TESTING} mode.
	 */
    public static boolean testing() {
        return (mode & TESTING) != 0;
    }

    /**
	 * Returns whether the application is running in {@link #DEVELOPMENT} mode.
	 *
	 * @return Whether the application is running in {@link #DEVELOPMENT} mode.
	 */
    public static boolean development() {
        return (mode & DEVELOPMENT) != 0;
    }

    /**
	 * Returns whether the application is running in {@link #DEBUGGING} mode.
	 *
	 * @return Whether the application is running in {@link #DEBUGGING} mode.
	 */
    public static boolean debug() {
        return (mode & DEBUGGING) != 0;
    }

    /**
	 * Returns the time at which this application was initialized
	 *
	 * @return {@link #timestamp}
	 */
    public long getTimestamp() {
        return timestamp;
    }

    /**
	 * Returns the plugin loader for this class
	 *
	 * @return {@link #loader}
	 */
    public Loader getPlugInLoader() {
        return loader;
    }

    @Override
    public void init() {
        if (!(tmpdir.exists() || tmpdir.mkdirs())) {
            throw new RuntimeException("Could not create tmpdir: " + tmpdir);
        }
    }

    /**
	 * Loads the application plugins
	 */
    @Override
    public void load() {
        loader.loadPlugIns();
    }

    /**
	 * Configures the application environment before invoking the main frame
	 *
	 * @param args The command-line arguments supplied to the application upon execution.
	 * @throws Exception
	 */
    public static void main(final String... args) throws Exception {
        final File properties = FileUtils.getFile(CONFIG_PATH, "log4j.properties");
        PropertyConfigurator.configure(properties.toURI().toURL());
        if (logger.isInfoEnabled()) {
            logger.info("Initializing application");
        }
        SwingUtilities.invokeAndWait(new MainRunner(args));
        instance.init();
        instance.load();
    }

    /**
	 * Allows the main class to be launched from a separate thread
	 */
    private static class MainRunner implements Runnable {

        /** Holds the command-line arguments passed to the application */
        private final String[] args;

        /**
		 * Constructs a {@link java.lang.Runnable} instance that initializes and executes the main application
		 *
		 * @param args Command-line arguments passed to the application upon initialization
		 */
        public MainRunner(final String... args) {
            this.args = args;
        }

        /**
		 * Logs a fatal exception and exits the application unsuccessfully
		 *
		 * @param exception The fatal exception to log
		 */
        private void logFatal(final Exception exception) {
            logger.fatal("The application failed to initialize", exception);
            final String msg = String.format("This application failed to initialize due to the following error, and must now close:%n%s", exception.getMessage());
            final String title = "Error Initializing Application";
            final int type = JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(MainFrame.getInstance(), msg, title, type);
            System.exit(1);
        }

        /**
		 * Initializes and executes the main application
		 */
        @Override
        public void run() {
            try {
                SubstanceLookAndFeel.setSkin(new DustSkin());
                Main.instance = new Main(args);
            } catch (final Exception exception) {
                logFatal(exception);
            }
        }
    }
}
