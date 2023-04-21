package au.edu.ausstage.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A class of methods useful when reading data from a properties file
 */
public class PropertiesManager {

    private Properties props = new Properties();

    /**
	 * Load the properties from the file
	 *
	 * @param path the path to the properties file
	 *
	 * @return     true if, and only if, the file loaded successfully
	 */
    public boolean loadFile(String path) {
        if (InputUtils.isValid(path) == false) {
            throw new IllegalArgumentException("The path to the properties file cannot be null or an empty string");
        }
        try {
            props.load(new FileInputStream(path));
        } catch (IOException ex) {
            System.err.println("ERROR: Unable to load the properties file");
            System.err.println("       " + ex.getMessage());
            return false;
        }
        return true;
    }

    /**
	 * A method to get the value of a parameter
	 *
	 * @param key the key for this parameter
	 *
	 * @return    the value of this parameter
	 */
    public String getProperty(String key) {
        if (InputUtils.isValid(key) == true) {
            return props.getProperty(key);
        }
        return null;
    }

    /**
	 * A method to get the value of a parameter
	 * A method to get the value of a parameter
	 *
	 * @param key the key for this parameter
	 *
	 * @return    the value of this parameter
	 */
    public String getValue(String key) {
        if (InputUtils.isValid(key) == true) {
            return props.getProperty(key);
        }
        return null;
    }
}
