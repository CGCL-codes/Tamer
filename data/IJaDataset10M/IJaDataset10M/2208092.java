package gpl.scotlandyard.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Works as Properties.
 * 
 * @author Norbert Martin
 */
public class Config {

    private static final Properties PROPERTIES = new Properties();

    /**
	 * Loads properties in conf. If some already exists they will be overwritten.
	 * 
	 * @param conf to load.
	 * @throws IOException
	 */
    public static void load(File conf) throws IOException {
        PROPERTIES.load(new FileInputStream(conf));
    }

    /**
	 * Loads properties in conf. If some already exists they will be overwritten.
	 * The stream is closed at the end.
	 * 
	 * @param conf to load.
	 * @throws IOException
	 */
    public static void load(InputStreamReader conf) throws IOException {
        PROPERTIES.load(conf);
        conf.close();
    }

    /**
	 * Gets associated value to this key, null if doesn't exist.
	 * 
	 * @param key
	 * @return associated value or null if doesn't exist
	 */
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
	 * Gets associated value to this key and convert as int.
	 * 
	 * @param key
	 * @return associated value as int or throws exception if doesn't exist
	 * @throws NumberFormatException if result is null
	 */
    public static int getInteger(String key) {
        return Integer.parseInt(PROPERTIES.getProperty(key));
    }

    /**
	 * Gets associated value to this key and convert as boolean.
	 * 
	 * @param key
	 * @return associated value as boolean
	 */
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(PROPERTIES.getProperty(key));
    }

    @Override
    public String toString() {
        return "Config : " + PROPERTIES.toString();
    }
}
