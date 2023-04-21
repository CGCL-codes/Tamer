package org.pojosoft.core.configuration;

import org.pojosoft.core.support.StringUtils;
import java.util.Properties;
import java.util.TimeZone;

/**
 * The configuration is loaded from a properties file. 
 * @author POJO Software
 * @version 1.0
 * @since 1.0
 */
public class DefaultSystemPropertiesConfig extends Properties implements Configuration {

    protected TimeZone appServerTimeZone = TimeZone.getDefault();

    protected TimeZone dbServerTimeZone = null;

    /**
   * Get the property as boolean.
   * @param key
   * @return true if the value string is "true", "Y" or "yes". Case in-sensitive. 
   */
    public boolean getBooleanProperty(String key) {
        String v = getProperty(key);
        return (v != null && (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("Y") || v.equalsIgnoreCase("yes")));
    }

    /**
   * Get a boolean property, use the default if not present.
   * @param key
   * @param defaultValue
   * @return the boolean property
   */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String v = getProperty(key);
        if (v == null) return defaultValue; else return (v != null && (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("Y") || v.equalsIgnoreCase("yes")));
    }

    /**
   * Get the property as Long value.
   * @param key
   * @return the Long property
   * @throws ConfigurationException
   */
    public Long getLongProperty(String key) throws ConfigurationException {
        String v = getProperty(key);
        Long r = null;
        try {
            if (StringUtils.hasValue(v)) r = new Long(v);
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Conversion of " + v + " to Long failed.");
        }
        return (r);
    }

    /**
   * Get the property as Doube value.
   * @param key
   * @return the Double property
   * @throws ConfigurationException
   */
    public Double getDoubleProperty(String key) throws ConfigurationException {
        String v = getProperty(key);
        Double r = null;
        try {
            if (StringUtils.hasValue(v)) r = new Double(v);
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Conversion of " + v + " to Double failed.");
        }
        return (r);
    }

    public TimeZone getAppServerTimeZone() {
        return appServerTimeZone;
    }

    public TimeZone getDatabaseTimeZone() {
        if (dbServerTimeZone == null) {
            if (getProperty("database.timezone") != null) {
                dbServerTimeZone = TimeZone.getTimeZone(getProperty("database.timezone"));
            } else dbServerTimeZone = appServerTimeZone;
        }
        return (dbServerTimeZone);
    }

    /**
   * Is cache enabled. default to true.
   * Reads the  property with key cache.enabled
   * @return true if cache is enabled
   */
    public boolean isCacheEnabled() {
        return getBooleanProperty("cache.enabled", true);
    }
}
