package reqtrack;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.apache.commons.logging.*;
import org.springframework.context.*;
import org.springframework.context.support.*;
import org.springframework.richclient.application.*;
import reqtrack.config.*;
import reqtrack.ui.swing.*;

/**
 * Main represents the entry point and central access point for
 * the Swing based reqtrack application, implemented as a
 * Singleton with a main() method.
 * 
 * @author jastram
 */
public class Main {

    /** Jakarta Common Log Facility */
    private static Log log = LogFactory.getLog(Main.class);

    /** The Spring Application Context, initialized by initialize(). */
    private ApplicationContext context;

    /**
     * Holds the file associated with the user's local configuration.
     * On startup, the values are loaded into localProperties, and they
     * are written back on exit.
     */
    private File localPropertyFile;

    /**
     * Holds the local (machine specific) configuration.  Information in
     * this Object is synchronized with the user's local configuration
     * file.
     */
    private Properties localProperties;

    /** Singleton Constructor should not be called from anywhere else */
    private Main() {
    }

    /** The entry point to the standalone Swing system.
     * @param args no or one argument: the configuration file location.  If
     *         not provided, a file called "reqtrack.properties" in the user's
     *         home directory is used.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new ApplicationLauncher(new ClassPathXmlApplicationContext("reqtrack/ctx/bootstrap-context.xml"));
    }

    /**
     * Holds the properties that are specific to the current machine.
     * These properties are loaded on startup and persisted on exit.
     * Only store information here that is really local (e.g. the
     * context path, window sizes and positions, etc.).  Information
     * that is not local should be stored in the database.
     * @return the configuration object for local properties
     */
    public Properties getLocalConfiguration() {
        return localProperties;
    }

    /**
     * Returns the Spring applicaiton context.
     * @return the Spring applicaiton context
     */
    public ApplicationContext getContext() {
        return context;
    }

    /**
     * Cleanly shuts down the application.
     * @param exitCode the exit code anded to {@link System#exit(int)}.
     * @param message information written to the Log and the console.
     */
    public void exit(int exitCode, String message) {
        saveLocalConfiguration();
        log.info("Shutting down reqtrack: " + message);
        System.exit(exitCode);
    }

    /**
     * Returns an ApplicationContext that contains the static context that is
     * part of the distribution with the provided parent context.  This helper
     * methods allows us to build the proper context, no matter whether the
     * parent comes from a file or an app server, or some other location.
     * 
     * @param parentContext the context to be integrated with the static context
     * @return the combined application context
     */
    public static ApplicationContext integrateWithStaticContext(ApplicationContext parentContext) {
        return new ClassPathXmlApplicationContext(new String[] { "reqtrack/ctx/static-context.xml", "reqtrack/ctx/richclient-context.xml" }, parentContext);
    }

    /**
     * Performs System initialization.
     * @param configFileName the configuration file location.  If null, the default
     *         location will be used.
     * @throws Exception
     */
    private void initialize(String configFileName) throws Exception {
        localProperties = getConfiguration(configFileName);
        if (localProperties == null) exit(0, "User canceled property creation.");
        if (!localPropertyFile.exists()) saveLocalConfiguration();
        String springContext = localProperties.getProperty("springContextFile");
        log.info("Opening Spring Context: " + springContext);
        ApplicationContext parentContext = new FileSystemXmlApplicationContext(springContext);
        context = integrateWithStaticContext(parentContext);
        log.info("Context created.");
        ReqtrackConfiguration config = (ReqtrackConfiguration) context.getBean("reqtrackConfiguration");
        config.configureDb();
        log.info("Database Configuration complete.");
        new ApplicationLauncher(context);
    }

    /**
     * Configures the main window and makes it visible.
     */
    private void showMainWindow() {
        final JFrame frame = new JFrame("RT");
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame, "Do you really want to quit?", "Exiting", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    exit(0, "User exit.");
                }
            }
        });
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new RequirementsPane());
        frame.setSize(300, 300);
        frame.setJMenuBar(new MainMenu());
        frame.setVisible(true);
        log.info("Main Window up.");
    }

    /**
     * Retrieves the configuration properties, no matter whether the given
     * configuration file exists.  If it exists, the properties are read from
     * it and returned.  Otherwise, the configuration wizard will run.  If the
     * user cancels the configuration, this method will return null.
     * 
     * @param configFileName the configuration file name (from the command line).
     *         If null is provided, the default file will be used.
     * @return the configuration properties, or null if the user aborted configuration.
     * @throws IOException
     */
    private Properties getConfiguration(String configFileName) throws IOException {
        log.info("Using configuration file: " + configFileName);
        localPropertyFile = new File(configFileName);
        Properties properties = new Properties();
        if (localPropertyFile.exists()) {
            properties.load(new FileInputStream(configFileName));
        } else {
        }
        return properties;
    }

    /**
     * Tries to save the local configuration, if the file is known and properties
     * exist.  If something goes wrong while writing the configuration, a log
     * entry is created, but no Exception is thrown.
     */
    private void saveLocalConfiguration() {
        if (localProperties != null && localPropertyFile != null) {
            try {
                localProperties.store(new FileOutputStream(localPropertyFile), "Reqtrack properties");
            } catch (IOException e) {
                log.fatal("Couldn't write configuration file: " + localPropertyFile, e);
            }
            log.info("local properties saved to " + localPropertyFile);
        }
    }
}
