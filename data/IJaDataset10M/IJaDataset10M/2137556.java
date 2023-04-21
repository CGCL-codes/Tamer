package uk.ac.lkl.migen.mockup.shapebuilder;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.SimpleLayout;
import uk.ac.lkl.common.util.event.UpdateEvent;
import uk.ac.lkl.common.util.event.UpdateListener;
import uk.ac.lkl.common.util.LoggingUtilities;
import uk.ac.lkl.common.util.RobotUtilities;
import uk.ac.lkl.common.util.SubversionUtilities;
import uk.ac.lkl.migen.mockup.shapebuilder.ui.*;

public class Launcher {

    /**
     * The main logger for the application
     */
    private static Logger logger = Logger.getLogger(Launcher.class);

    private static final String DEFAULT_CONFIGURATION_FILENAME = "default";

    private static final String CONFIG_PATH = "data/config";

    public static void main(String[] args) {
        setLookAndFeel();
        Configuration configuration = createConfiguration(args);
        final NamePrompt namePrompt = new NamePrompt();
        namePrompt.setVisible(true);
        final ShapeBuilder shapeBuilder = new ShapeBuilder(configuration, true);
        namePrompt.addUpdateListener(new UpdateListener<NamePrompt>() {

            public void objectUpdated(UpdateEvent<NamePrompt> e) {
                String name = namePrompt.getName();
                namePrompt.dispose();
                mainLoggingConfig(name);
                shapeBuilder.unblock();
                String now = LoggingUtilities.formatTime();
                logger.info("Starting application (" + now + ")...");
            }
        });
        shapeBuilder.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent arg0) {
                String[] onEndKeys = { "VK_CONTROL", "VK_SHIFT", "VK_F11" };
                uk.ac.lkl.common.util.RobotUtilities.sendKeysCombo(onEndKeys);
                String now = LoggingUtilities.formatTime();
                logger.info("Stopping application (" + now + ").");
            }
        });
        sendStartKeys();
    }

    private static void sendStartKeys() {
        String[] onStartKeys = { "VK_CONTROL", "VK_SHIFT", "VK_F9" };
        RobotUtilities.sendKeysCombo(onStartKeys);
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (InstantiationException e) {
            System.out.println(e);
        }
    }

    private static Configuration createConfiguration(String[] args) {
        String configurationHandle;
        if (args.length == 0) configurationHandle = DEFAULT_CONFIGURATION_FILENAME; else configurationHandle = args[0];
        Configuration configuration;
        if (configurationHandle.endsWith(".xml")) {
            String userDir = System.getProperty("user.dir");
            File file = new File(userDir, configurationHandle);
            configuration = Configuration.readConfiguration(file);
        } else {
            String packageName = Launcher.class.getPackage().getName();
            String configurationFilename = packageName.replace('.', '/') + "/" + CONFIG_PATH + "/" + configurationHandle + ".xml";
            ClassLoader cl = ShapeBuilderPanel.class.getClassLoader();
            URL url = cl.getResource(configurationFilename);
            if (url == null) {
                throw new IllegalArgumentException("Unable to find internal configuration '" + configurationHandle + "'");
            }
            configuration = Configuration.readConfiguration(url);
        }
        return configuration;
    }

    private static void mainLoggingConfig(String studentName) {
        try {
            String now = LoggingUtilities.formatTime();
            String myLogfile = new String();
            myLogfile += "log/shapebuilder_" + now + "_" + SubversionUtilities.getCoreVersion() + ".log";
            myLogfile = myLogfile.replace(":", "-");
            PatternLayout myPattern = new PatternLayout("%6r %d{HH:mm:ss,SSS} %p %c %x - %m%n");
            FileAppender fileAppender = new FileAppender(myPattern, myLogfile);
            Logger parentLogger = Logger.getLogger("uk.ac.lkl.migen.mockup.shapebuilder");
            parentLogger.addAppender(fileAppender);
            ConsoleAppender consoleAppender = new ConsoleAppender(new SimpleLayout());
            parentLogger.addAppender(consoleAppender);
            parentLogger.setLevel((Level) Level.INFO);
            Logger dplogger = Logger.getLogger("Darren");
            dplogger.addAppender(new ConsoleAppender(new SimpleLayout()));
            dplogger.setLevel((Level) Level.INFO);
            if (studentName != null) {
                parentLogger.info("ShapeBuilder log for '" + studentName + "'.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("ERROR 66: No logging is possible. Exiting...");
            System.exit(0);
        }
    }
}
