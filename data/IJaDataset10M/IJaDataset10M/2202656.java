package org.ais.convert;

import java.util.List;

/**
 * Like Properties but singleton - for passing parameters (which came from command line arguments
 * or from config file) through the program 
 *
 * @since jdk1.0
 */
public class Parameters extends java.util.Properties {

    /**
	 * The single instance
	 */
    private static Parameters instance = new Parameters();

    /**
	 * Returns single instance of this class
	 */
    public static Parameters getInstance() {
        return instance;
    }

    /**
	 * Returns a property by key as String
	 */
    public static String getString(String key) {
        String value = instance.getProperty(key);
        return (value != null ? value.trim() : null);
    }

    /**
	 * Helper method. Gets an integer by key. Returns 0 if no property exists or it is malformed
	 */
    public static int getAsInteger(String key) {
        String i = Parameters.getString(key);
        try {
            return Integer.parseInt(i);
        } catch (NumberFormatException e) {
            System.err.println("Malformed integer in properties for key=" + key + ":" + i);
        }
        return 0;
    }

    /**
	 * Helper method. Gets a double by key. Returns 0 if no property exists or it is malformed
	 */
    public static double getAsDouble(String key) {
        String i = Parameters.getString(key);
        try {
            return Double.valueOf(i).doubleValue();
        } catch (NumberFormatException e) {
            System.err.println("Malformed double in properties for key=" + key + ":" + i);
        }
        return 0.0;
    }

    /**
	 * Helper method. Gets a boolean by key. Returns true only if the property
	 * is true in either case
	 */
    public static boolean getAsBoolean(String key) {
        String s = Parameters.getString(key);
        return str2Bool(s);
    }

    public static Object getObject(String key) {
        return instance.get(key);
    }

    /**
	 * Helper method used for distinguishing boolean's 'true'.
	 * We use it from other classes - so it is separate from getAsBoolean()
	 */
    public static boolean str2Bool(String s) {
        if (s == null) return false;
        if (s.equals("true")) return true;
        if (s.equals("false")) return false;
        s = s.toLowerCase();
        if (s.equals("true")) return true;
        return false;
    }

    /**
	 * Adds a property overriding the old value if the value with the same key exists
	 * already
	 */
    public static void addProperty(String key, String value) {
        instance.put(key, value);
    }
}
