package tr.extract.clean;

import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import tr.appl.Constants;

/**
 * Extract clean preferences.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class ExtractCleanPrefs {

    private static final Logger LOG = Logger.getLogger("tr.extract.clean");

    private static final Preferences prefs = Preferences.userRoot().node(Constants.PREFS_PATH + "/extract");

    private static final String KEY_CLEAN_LAST_DATE_MS = "clean.last.date.ms";

    private static final long DEF_CLEAN_LAST_DATE_MS = 0;

    /** Clean interval days value for Every Startup */
    public static final int CLEAN_INTERVAL_STARTUP = 0;

    /** Clean interval days value for Never */
    public static final int CLEAN_INTERVAL_NEVER = Integer.MAX_VALUE;

    private static final String KEY_CLEAN_INTERVAL_DAYS = "clean.interval.days";

    private static final int DEF_CLEAN_INTERVAL_DAYS = CLEAN_INTERVAL_STARTUP;

    private static final String KEY_CLEAN_AGE_DAYS = "clean.age.days";

    private static final int DEF_CLEAN_AGE_DAYS = 0;

    /**
     * Gets the value for the last clean date milliseconds preference.
     * @return The value.
     */
    public static final long getCleanLastDateMS() {
        return prefs.getLong(KEY_CLEAN_LAST_DATE_MS, DEF_CLEAN_LAST_DATE_MS);
    }

    /**
     * Sets the value for the last clean date milliseconds preference.
     * @param value The value.
     */
    public static final void setCleanLastDateMS(long value) {
        prefs.putLong(KEY_CLEAN_LAST_DATE_MS, value);
        flush();
    }

    /**
     * Gets the value for the clean interval days preference.
     * @return The value.
     */
    public static final int getCleanIntervalDays() {
        return prefs.getInt(KEY_CLEAN_INTERVAL_DAYS, DEF_CLEAN_INTERVAL_DAYS);
    }

    /**
     * Sets the value for the clean interval days preference.
     * @param value The value.
     */
    public static final void setCleanIntervalDays(int value) {
        prefs.putInt(KEY_CLEAN_INTERVAL_DAYS, value);
        flush();
    }

    /**
     * Gets the value for the clean file age days preference.
     * @return The value.
     */
    public static final int getCleanAgeDays() {
        return prefs.getInt(KEY_CLEAN_AGE_DAYS, DEF_CLEAN_AGE_DAYS);
    }

    /**
     * Sets the value for the clean file age days preference.
     * @param value The value.
     */
    public static final void setCleanAgeDays(int value) {
        prefs.putInt(KEY_CLEAN_AGE_DAYS, value);
        flush();
    }

    private static void flush() {
        try {
            prefs.flush();
        } catch (BackingStoreException ex) {
            LOG.severe("Extract clean preferences error. " + ex.getMessage());
        }
    }
}
