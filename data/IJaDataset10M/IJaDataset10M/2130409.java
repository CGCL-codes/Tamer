package org.scubatoolkit;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.scubatoolkit.db.Database;
import org.scubatoolkit.exceptions.ExceptionManager;
import org.scubatoolkit.utils.ScubaUtils;

/** * Main class for the scuba toolkit application. *   */
public class Scubatoolkit {

    /**	 * The main program for the Scubatoolkit class	 * 	 * @param args	 *            The command line arguments	 */
    public static void main(String[] args) {
        SplashScreen splash = new SplashScreen();
        splash.setMessage("Initializing settings...");
        initializeSettings();
        splash.setMessage("Initializing logging...");
        initializeLogging();
        splash.setMessage("Initializing database...");
        initializeDatabase();
        log.info("The Scuba Toolkit activity log saved in : " + Settings.getInstance().getActivityLogPath());
        log.info("Created on " + new Date());
        log.debug("Starting the Scuba Toolkit....");
        log.info("Scuba Toolkit Version    : " + Settings.getInstance().getVersion());
        log.info("Scuba Toolkit Build Date : " + Settings.getInstance().getBuildDate());
        try {
            Settings.getInstance().saveSettings();
        } catch (SettingsException se) {
            log.error("Error saving settings. ", se);
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle r = ge.getMaximumWindowBounds();
        mainWindow = new AppWindow("The Complete Scuba Toolkit");
        mainWindow.setBounds(r.x, r.y, r.width, r.height);
        mainWindow.setVisible(true);
        splash.dispose();
    }

    /**	 * Description of the Method	 */
    public static void createSettingsDirectory() {
        File setting = new File(Settings.getInstance().getSettingsDirectory());
        if (!setting.exists()) {
            setting.mkdir();
        }
    }

    private static void initializeSettings() {
        Settings settings = Settings.getInstance();
        try {
            settings.init();
        } catch (SettingsException se) {
            System.err.println("An error occured while trying to initialise the application settings");
            se.printStackTrace();
        }
        File settingsDirectory = new File(settings.getSettingsDirectory());
        if (!settingsDirectory.exists()) {
            JOptionPane.showMessageDialog(null, ScubaUtils.getProperty("first.run.messagebox.text"), ScubaUtils.getProperty("default.messagebox.title"), JOptionPane.INFORMATION_MESSAGE);
            createSettingsDirectory();
        }
        if (Settings.getInstance().getBooleanSetting("show.disclaimer", true)) {
            JOptionPane.showMessageDialog(null, "Thank you for trying the Scuba Toolkit.\n" + "As you get ready to start the application,\n" + "keep in mind that this is a tool designed for\n" + "certified, trained divers. Do not use these\n" + "tools if you are not properly trained. Also\n" + "remember that this is open source (and free)\n" + "software, distributed under the GPL, so\n" + "\"This program is distributed in the hope\n" + "that it will be useful, but WITHOUT ANY \n" + "WARRANTY; without even the implied warranty\n" + "of MERCHANTABILITY or FITNESS FOR A PARTICULAR\n" + "PURPOSE \" which means, that there are no \n" + "warranties whatsoever that the stuff you see\n" + "in here actually does what you expect it to do.\n" + "We have tried to make the software accurate\n" + "and tried to test it so it contains as few bugs\n" + "as possible. So, for the most part, remember\n" + "that these are tools that will help you, but\n" + "the fact is that scuba diving has its risks,\n" + "and using this software also has its own risks.", "Disclaimer", JOptionPane.OK_OPTION);
            Settings.getInstance().setSetting("show.disclaimer", "false");
            try {
                Settings.getInstance().saveSettings();
            } catch (Exception ex) {
                ExceptionManager.publish(log, ex);
            }
        }
    }

    private static void initializeLogging() {
        File activityLogFile = new File(Settings.getInstance().getActivityLogPath());
        if (activityLogFile.exists()) {
            boolean deleted = activityLogFile.delete();
        }
        try {
            Properties props = new Properties();
            InputStream in = Scubatoolkit.class.getResourceAsStream("/org/scubatoolkit/log4j.properties");
            props.load(in);
            in.close();
            PropertyConfigurator.configure(props);
            FileAppender outputFile = new FileAppender();
            Appender stdout = Logger.getRootLogger().getAppender("console");
            outputFile.setName("file");
            outputFile.setLayout(stdout.getLayout());
            outputFile.setFile(Settings.getInstance().getActivityLogPath());
            outputFile.activateOptions();
            Logger.getRootLogger().addAppender(outputFile);
        } catch (IOException ioe) {
            BasicConfigurator.configure();
        }
    }

    private static void initializeDatabase() {
        try {
            if (!(Database.instance.exists() && Database.instance.getSchemaVersion().equalsIgnoreCase("1.0"))) {
                Database.instance.create();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Unable to initialize the database", "Initialization error", JOptionPane.OK_OPTION);
            ExceptionManager.publish(log, ex);
        }
    }

    private static Logger log = Logger.getLogger(Scubatoolkit.class);

    private static AppWindow mainWindow;
}
