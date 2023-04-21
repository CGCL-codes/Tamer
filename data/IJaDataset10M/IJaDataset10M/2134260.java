package net.rptools.inittool;

import java.awt.Toolkit;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.rptools.chartool.ui.LoggingEventQueue;
import net.rptools.chartool.ui.component.Utilities;
import net.rptools.chartool.ui.component.Utilities.InitializationInformation;
import net.rptools.inittool.model.Groups;
import net.rptools.inittool.model.InitToolGameSettings;
import net.rptools.inittool.model.InitToolPreferences;
import net.rptools.inittool.model.InitToolSettingsFile;
import net.rptools.inittool.ui.InitToolFrame;
import net.rptools.inittool.ui.InitToolPersistenceSupport;
import net.rptools.lib.AppUtil;
import com.jidesoft.utils.Lm;

/**
 * This class starts the init tool as a new app.
 * 
 * @author jgorrell
 * @version $Revision$ $Date$ $Author$
 */
public class InitTool {

    /**
   * The default files loaded during version upgrade.
   */
    public static final String[] DEFAULT_FILES = { "game/default.rpgame" };

    /**
   * Path to the default icons.
   */
    public static final String DEFAULT_ICON_PATH = "net/rptools/inittool/resources/images";

    /**
   * Name of the default icon files.
   */
    public static final String[] DEFAULT_ICONS = new String[] { "alarmIndicator.png", "delayWaitState.png", "disabledWaitState.png", "readyWaitState.png", "exitWaitState.png", "reminderTimer.png", "spellTimer.png", "bug.png", "award_star_gold_3.png", "award_star_silver_2.png", "award_star_bronze_1.png", "bomb.png", "bell.png", "basket.png", "asterisk_yellow.png", "brick.png", "book.png", "car.png", "cog.png", "coins.png", "cross.png", "exclamation.png", "error.png", "flag_blue.png", "flag_green.png", "group.png", "key.png", "lock.png", "money.png", "ruby.png", "shield.png", "star.png", "tag_purple.png", "tag_red.png", "tag_green.png", "telephone.png", "user.png", "wrench.png", "database_add.png" };

    /**
   * Logger instance for this class.
   */
    private static final Logger LOGGER = Logger.getLogger(InitTool.class.getName());

    /**
   * The initialization info return during initialization
   */
    private static InitializationInformation initInfo = new InitializationInformation();

    /**
   * Start the init tool
   * 
   * @param args Not used
   */
    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
        } catch (Exception e) {
            System.out.println("Unable to load the look and feel, using the default");
            e.printStackTrace();
        }
        try {
            init();
            loadSettings();
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Exception starting InitTool, exiting application.", e);
            System.exit(-1);
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                InitToolFrame frame = InitToolFrame.getInstance();
                Toolkit.getDefaultToolkit().getSystemEventQueue().push(new LoggingEventQueue(frame));
                frame.setVisible(true);
                Utilities.getProgressFrame().setVisible(false);
                Utilities.releaseProgressFrame();
            }
        });
    }

    /**
   * Initialize file and icon support
   */
    public static void init() {
        AppUtil.init("inittoolDbg");
        Lm.verifyLicense("Trevor Croft", "rptools", "5MfIVe:WXJBDrToeLWPhMv3kI2s3VFo");
        InitToolPreferences prefs = InitToolPreferences.getInstance();
        Utilities.initializeApplication(prefs, initInfo, DEFAULT_ICONS, DEFAULT_ICON_PATH);
        if (!initInfo.isAllExist()) {
            prefs.setDefaultEncounterFile(null);
            prefs.setDefaultGroupsFile(null);
        }
        int count = 4 + DEFAULT_FILES.length;
        count += initInfo.isInstall() ? InitToolSettingsFile.installInitToolGameSettingsProgressCount() : InitToolSettingsFile.loadInitToolGameSettingsProgressCount();
        count += prefs.getLoadedSourceFiles().size() * (initInfo.isInstall() ? InitToolSettingsFile.installInitToolGameSourceProgressCount() : InitToolSettingsFile.loadInitToolGameSourceProgressCount());
        Utilities.createProgressFrame(count, "InitTool_SplashScreen.png").setVisible(true);
    }

    /**
   * Load the default settings if available. 
   */
    public static void loadSettings() {
        InitToolPreferences prefs = InitToolPreferences.getInstance();
        Utilities.updateVersionSpecificFiles(DEFAULT_FILES, "net/rptools/chartool/resources/", new Runnable() {

            public void run() {
                InitToolPreferences.getInstance().setDefaultSettingsFile(initInfo.getDefaultSettingsFile());
                InitToolPreferences.getInstance().setLoadedSourceFiles(null);
                InitToolPreferences.getInstance().setDefaultGroupsFile(null);
                InitToolPreferences.getInstance().setDefaultEncounterFile(null);
            }
        }, prefs, initInfo);
        initInfo.setInstall(initInfo.isInstall() || prefs.isInstallSettingsOnStartup());
        Utilities.initializeGameSettings(initInfo, InitToolGameSettings.getInstance(), prefs);
        File defaultEncounterFile = prefs.getDefaultEncounterFile();
        if (defaultEncounterFile != null && defaultEncounterFile.exists() && defaultEncounterFile.canRead()) {
            if (!InitToolFrame.getInstance().loadEncounter(defaultEncounterFile)) prefs.setDefaultEncounterFile(null);
        } else {
            if (defaultEncounterFile != null) prefs.setDefaultEncounterFile(null);
        }
        Utilities.incrementProgressModel(1);
        File defaultGroupsFile = prefs.getDefaultGroupsFile();
        if (defaultGroupsFile != null && defaultGroupsFile.exists() && defaultGroupsFile.canRead()) {
            try {
                Groups.setLoadedGroupsFile(InitToolPersistenceSupport.getInstance().readGroups(defaultGroupsFile));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Exception loading groups file", e);
                JOptionPane.showMessageDialog(Utilities.getProgressFrame(), e.getMessage() + "\nNo groups are currently loaded.", "Error!", JOptionPane.ERROR_MESSAGE);
                prefs.setDefaultGroupsFile(null);
            }
        } else {
            if (defaultGroupsFile != null) prefs.setDefaultGroupsFile(null);
        }
        Utilities.finishProgressModel();
    }
}
