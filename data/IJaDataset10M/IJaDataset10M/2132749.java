package de.bea.util;

import java.util.Properties;

/**
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PropertiesConversion {

    /**
     * Constructor for PropertiesConversion.
     */
    private PropertiesConversion() {
        super();
    }

    public static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        String value = getString(properties, key, null);
        if (value == null) {
            return defaultValue;
        } else {
            return value.equalsIgnoreCase("TRUE");
        }
    }

    public static void putBoolean(Properties properties, String key, boolean value) {
        putString(properties, key, value ? "true" : "false");
    }

    public static int getInt(Properties properties, String key, int defaultValue) {
        String value = getString(properties, key, null);
        if (value == null) {
            return defaultValue;
        } else {
            return Integer.parseInt(value);
        }
    }

    public static long getLong(Properties properties, String key, long defaultValue) {
        String value = getString(properties, key, null);
        if (value == null) {
            return defaultValue;
        } else {
            return Long.parseLong(value);
        }
    }

    public static void putInt(Properties properties, String key, int value) {
        putString(properties, key, String.valueOf(value));
    }

    public static void putLong(Properties properties, String key, long value) {
        putString(properties, key, String.valueOf(value));
    }

    public static String getString(Properties properties, String key, String defaultValue) {
        System.out.println("-I- Reading string value for key " + key);
        String value = (String) properties.get(key);
        return value == null ? defaultValue : value;
    }

    /**
     * Method getString.
     * @param properties
     * @param key
     * @param fallbackKey wird genommen, falls kein Eintrag unter key gefunden werden konnte
     * @param defaultValue
     * @return String
     */
    public static String getString(Properties properties, String key, String fallbackKey, String defaultValue) {
        System.out.println("-I- Reading string value for key " + key);
        String value = (String) properties.get(key);
        if (value == null && fallbackKey != null) {
            System.out.println("-I- Reading string value for fallback-key " + fallbackKey);
            value = (String) properties.get(fallbackKey);
        }
        return value == null ? defaultValue : value;
    }

    public static void putString(Properties properties, String key, String value) {
        properties.put(key, value);
    }
}
