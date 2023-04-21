package net.sf.solarnetwork.node;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for loading system settings.
 *
 * @author matt
 * @version $Revision: 108 $ $Date: 2008-08-14 19:17:07 -0400 (Thu, 14 Aug 2008) $
 */
public final class SolarNodeSettings {

    /** The name of the solar node properties file. */
    public static final String SOLARNODE_PROPERTIES_NAME = "solarnode.properties";

    private static final Logger LOG = Logger.getLogger(SolarNodeSettings.class.getName());

    private static final Properties SETTINGS = new Properties();

    private SolarNodeSettings() {
        super();
    }

    /**
	 * Get the node settings in the form of a Properties file.
	 * 
	 * @return the node settings
	 */
    public static Properties getSettings() {
        if (SETTINGS.size() > 0) {
            return SETTINGS;
        }
        synchronized (SETTINGS) {
            String sysPropFilePath = System.getProperty(SOLARNODE_PROPERTIES_NAME);
            if (sysPropFilePath != null) {
                if (LOG.isLoggable(Level.CONFIG)) {
                    LOG.config("Using settings properties file [" + sysPropFilePath + "] as specified by system property [" + SOLARNODE_PROPERTIES_NAME + ']');
                }
                try {
                    SETTINGS.load(new BufferedInputStream(new FileInputStream(sysPropFilePath)));
                } catch (IOException e) {
                    throw new RuntimeException("Unable to load settings from file:" + sysPropFilePath, e);
                }
                return SETTINGS;
            }
            try {
                SETTINGS.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(SOLARNODE_PROPERTIES_NAME));
            } catch (IOException e) {
                throw new RuntimeException("Unable to load settings from classpath:" + SOLARNODE_PROPERTIES_NAME, e);
            }
        }
        return SETTINGS;
    }

    /**
	 * Get a String setting value.
	 * 
	 * @param key the setting to get
	 * @return the setting value, or <em>null</em> if not available
	 */
    public static String getSetting(String key) {
        return getSetting(key, null);
    }

    /**
	 * Get a String setting value.
	 * 
	 * @param key the setting to get
	 * @return the setting value
	 * @throws RuntimeException if the setting is not available
	 */
    public static String getRequiredSetting(String key) {
        String val = getSetting(key, null);
        if (val != null && !"".equals(val)) {
            return val;
        }
        throw new RuntimeException("Setting [" + key + "] must be provided");
    }

    /**
	 * Get a String setting value or a default if not found.
	 * 
	 * @param key the setting to get
	 * @param defaultValue the default value if not found
	 * @return the setting value, or <em>null</em> if not available
	 */
    public static String getSetting(String key, String defaultValue) {
        return getSettings().getProperty(key);
    }

    /**
	 * Get a group of settings for a given prefix.
	 * 
	 * <p>Any setting whose key starts with {@code prefix} will be returned. The 
	 * setting keys will have their prefix stripped, and the resulting
	 * Map will contain the resulting keys and corresponding values.</p> 
	 * 
	 * @param prefix the setting key prefix to look for
	 * @return a Map of short setting keys and corresponding values
	 */
    public static Map<String, String> getSettingsForPrefix(String prefix) {
        Properties settings = getSettings();
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<Object, Object> me : settings.entrySet()) {
            if (me.getKey() == null) {
                continue;
            }
            String key = me.getKey().toString();
            if (!key.startsWith(prefix)) {
                continue;
            }
            String value = me.getValue() == null ? null : me.getValue().toString();
            result.put(key.substring(prefix.length()), value);
        }
        return result;
    }

    /**
	 * Get an Integer setting value.
	 * 
	 * @param key the setting to get
	 * @return the setting value, or <em>null</em> if not available
	 */
    public static Integer getSettingInteger(String key) {
        String value = getSettings().getProperty(key);
        if (value == null || "".equals(value)) {
            return null;
        }
        return Integer.valueOf(value);
    }

    /**
	 * Get a Long setting value.
	 * 
	 * @param key the setting to get
	 * @return the setting value, or <em>null</em> if not available
	 */
    public static Long getSettingLong(String key) {
        String value = getSettings().getProperty(key);
        if (value == null || "".equals(value)) {
            return null;
        }
        return Long.valueOf(value);
    }
}
