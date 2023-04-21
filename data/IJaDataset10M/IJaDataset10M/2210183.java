package com.mills.cellularengine.view.swing;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * This class loads properties from a property file.
 * 
 * @author mills
 *
 */
public class SwingGUIPropertyManager {

    /**
	 * holds loaded properties
	 */
    public static Properties properties;

    /**
	 * the actual language
	 */
    public static String language = "en";

    /**
	 * loads a property from properties or returns the defaultValue
	 * 
	 * @param prop
	 * @param defaultValue
	 * @return
	 */
    public static String getProperty(String prop, String defaultValue) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(prop + "." + language, defaultValue);
    }

    /**
	 * loads the property file when found
	 */
    private static void loadProperties() {
        try {
            properties = new Properties();
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream("properties.prop"));
            properties.load(stream);
            stream.close();
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                System.out.println("properties file not found.");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
	 * sets the language
	 * @param lang
	 */
    public static void setLanguage(String lang) {
        language = lang;
    }
}
