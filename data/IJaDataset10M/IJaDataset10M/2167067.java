package org.gerhardb.jibs.optimizer;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.gerhardb.lib.io.FileUtil;

/**
 * Wraps the preferences used by the viewer and provides an way of editing
 * the preferences.
 * @author  Gerhard Beck
 */
public class OptimizerPreferences {

    private static final String PREF_PATH = "/org/gerhardb/jibs/optimizer/OptimizerPreferences";

    private static Preferences clsPrefs;

    private static int clsFullScreenHeight = 600;

    private static int clsFullScreenWidth = 800;

    static {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (ge.isHeadlessInstance()) {
            throw new RuntimeException("Java is reporting that this instance " + "of your GraphicsEnvironment is headless.  A monitor " + " and keyboard are required to run JIBS.");
        }
        try {
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            DisplayMode dm = gd.getDisplayMode();
            clsFullScreenWidth = dm.getWidth();
            clsFullScreenHeight = dm.getHeight();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            System.out.println("------------------------------------------------");
            System.out.println(ge.getDefaultScreenDevice());
            System.out.println("------------------------------------------------");
        }
    }

    private OptimizerPreferences() {
    }

    /**
    * @throws SecurityException
    */
    public static void init() {
        if (clsPrefs != null) {
            return;
        }
        clsPrefs = Preferences.userRoot().node(PREF_PATH);
    }

    public static int getFullScreenHeight() {
        return clsFullScreenHeight;
    }

    public static int getFullScreenWidth() {
        return clsFullScreenWidth;
    }

    private static final String LOG_DIRECTORY = "logDirectory";

    private static final String LOG_NUMBER = "logNumber";

    /**
    * @return null if not correctly set.
    */
    public static String getLogDirectory() {
        String rtnMe = FileUtil.checkDirectory(clsPrefs.get(LOG_DIRECTORY, System.getProperty("user.home")));
        if (rtnMe == null) {
            rtnMe = System.getProperty("user.home");
        }
        return rtnMe;
    }

    public static void setLogDirectory(String dir) {
        String validated = FileUtil.safeValidateDirectory(dir);
        if (validated != null) {
            clsPrefs.put(LOG_DIRECTORY, validated);
        } else {
            clsPrefs.remove(LOG_DIRECTORY);
        }
    }

    public static int getLogNumber() {
        return clsPrefs.getInt(LOG_NUMBER, 0);
    }

    public static void setLogNumber(int setting) throws Exception {
        clsPrefs.putInt(LOG_NUMBER, setting);
        clsPrefs.flush();
    }

    private static final String DEDUP = "dedup";

    private static final String DIR_UTIL_ACROSS = "dirAcross";

    public static boolean getDedup() {
        return clsPrefs.getBoolean(DEDUP, false);
    }

    public static void setDedup(boolean setting) {
        clsPrefs.putBoolean(DEDUP, setting);
    }

    public static boolean getDirectoryAcross() {
        return clsPrefs.getBoolean(DIR_UTIL_ACROSS, false);
    }

    public static void setDirectoryAcross(boolean setting) {
        clsPrefs.putBoolean(DIR_UTIL_ACROSS, setting);
    }

    private static final String RELABEL_NUMBER = "relabelNumber";

    private static final String DIR_UTIL_UNIQUE_DOWN = "uniqueRenamingDowload";

    private static final String DIR_UTIL_WINDOWS = "dirUtilWindows";

    private static final String DIR_UTIL_FIRST_ENDING = "dirUtilFirstEnding";

    public static int getRelabelNumber() {
        return clsPrefs.getInt(RELABEL_NUMBER, 0);
    }

    public static void setRelabelNumber(int setting) throws Exception {
        clsPrefs.putInt(RELABEL_NUMBER, setting);
        clsPrefs.flush();
    }

    public static boolean getUniqueRenamingDownload() {
        return clsPrefs.getBoolean(DIR_UTIL_UNIQUE_DOWN, false);
    }

    public static void setUniqueRenamingDownload(boolean setting) {
        clsPrefs.putBoolean(DIR_UTIL_UNIQUE_DOWN, setting);
    }

    public static boolean getWindowsFileConvention() {
        return clsPrefs.getBoolean(DIR_UTIL_WINDOWS, false);
    }

    public static void setWindowsFileConvention(boolean setting) {
        clsPrefs.putBoolean(DIR_UTIL_WINDOWS, setting);
    }

    public static boolean getFirstEndingConvention() {
        return clsPrefs.getBoolean(DIR_UTIL_FIRST_ENDING, false);
    }

    public static void setFirstEndingConvention(boolean setting) {
        clsPrefs.putBoolean(DIR_UTIL_FIRST_ENDING, setting);
    }

    private static final String STRAIN = "strainOption";

    static final int STRAIN_NOTHING = 0;

    static final int STRAIN_MOVE = 1;

    static final int STRAIN_DELETE = 2;

    public static int getStrain() {
        return clsPrefs.getInt(STRAIN, STRAIN_NOTHING);
    }

    public static void setStrain(int setting) {
        clsPrefs.putInt(STRAIN, setting);
    }

    private static final String RESIZE = "dirResize";

    private static final String DIR_UTIL_HEIGHT = "dirUtilHeight";

    private static final String DIR_UTIL_WIDTH = "dirUtilWidth";

    public static boolean getResize() {
        return clsPrefs.getBoolean(RESIZE, false);
    }

    public static void setResize(boolean setting) {
        clsPrefs.putBoolean(RESIZE, setting);
    }

    public static int getResizeWidth() {
        return clsPrefs.getInt(DIR_UTIL_WIDTH, clsFullScreenWidth);
    }

    public static void setResizeWidth(int setting) {
        clsPrefs.putInt(DIR_UTIL_WIDTH, setting);
    }

    public static int getResizeHeight() {
        return clsPrefs.getInt(DIR_UTIL_HEIGHT, clsFullScreenHeight);
    }

    public static void setResizeHeight(int setting) {
        clsPrefs.putInt(DIR_UTIL_HEIGHT, setting);
    }

    private static final String BOTH_SMALLER = "bothSmaller";

    private static final String KILL = "killSmallFiles";

    private static final String KILL_HEIGHT = "dirUtilHeight";

    private static final String KILL_WIDTH = "dirUtilWidth";

    public static boolean getBothSmaller() {
        return clsPrefs.getBoolean(BOTH_SMALLER, false);
    }

    public static void setBothSmaller(boolean setting) {
        clsPrefs.putBoolean(BOTH_SMALLER, setting);
    }

    public static boolean getKill() {
        return clsPrefs.getBoolean(KILL, false);
    }

    public static void setKill(boolean setting) {
        clsPrefs.putBoolean(KILL, setting);
    }

    public static int getKillWidth() {
        return clsPrefs.getInt(KILL_WIDTH, 20);
    }

    public static void setKillWidth(int setting) {
        clsPrefs.putInt(KILL_WIDTH, setting);
    }

    public static int getKillHeight() {
        return clsPrefs.getInt(KILL_HEIGHT, 20);
    }

    public static void setKillHeight(int setting) {
        clsPrefs.putInt(KILL_HEIGHT, setting);
    }

    private static final String REPACK = "repack";

    public static boolean getRepack() {
        return clsPrefs.getBoolean(REPACK, false);
    }

    public static void setRepack(boolean setting) {
        clsPrefs.putBoolean(REPACK, setting);
    }

    private static final String SKIP_RECHECK = "skip_recheck";

    public static boolean getSkipRecheck() {
        return clsPrefs.getBoolean(SKIP_RECHECK, true);
    }

    public static void setSkipRecheck(boolean setting) {
        clsPrefs.putBoolean(SKIP_RECHECK, setting);
    }

    public static void flush() {
        try {
            clsPrefs.flush();
        } catch (BackingStoreException ex) {
            ex.printStackTrace();
        }
    }
}
