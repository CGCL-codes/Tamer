package com.fivenineuptime.dbmodeller.settings;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Reads and holds the various properties files containing
 * the different messages depending on the language preference.
 * @author jonathon
 *
 */
public class LanguageSettings {

    public static final int SETTING_TYPE_MENU = 1;

    private Properties menuProperties = null;

    private Properties labelProperties = null;

    /**
	 * Parse the properties files associated with the given
	 * language and make them available to the application.
	 * @param language the 2 character language code. The properties
	 * files associated with this language code must exist in the
	 * properties directory.
	 * @throws IllegalArgumentException if an invalid language code
	 * is supplied.
	 */
    public LanguageSettings(String language) {
        if (language == null || language.length() != 2) {
            throw new IllegalArgumentException("Invalid language setting: " + language);
        }
        menuProperties = readProperties("menu." + language + ".properties");
        labelProperties = readProperties("label." + language + ".properties");
    }

    /**
	 * Gets the menu property of the given type.
	 * @param key
	 * @param defaultValue
	 * @return the value of the property, defaultValue if not found
	 */
    public String getMenuProperty(String key, String defaultValue) {
        return menuProperties.getProperty(key, defaultValue);
    }

    /**
	 * Gets the label property of the given type.
	 * Labels are used in dialog boxes and windows.
	 * @param key
	 * @param defaultValue
	 * @return the value of the property, defaultValue if not found
	 */
    public String getLabelProperty(String key, String defaultValue) {
        return labelProperties.getProperty(key, defaultValue);
    }

    /**
     * Read the properties from the given file name and return them.
     * @param filename
     * @return the properties
     */
    private static Properties readProperties(String filename) {
        Properties props = new Properties();
        filename = "properties" + File.separatorChar + filename;
        try {
            File optionsFile = new File(filename);
            props.load(new FileInputStream(optionsFile));
        } catch (Exception e) {
            System.err.println("Failed to load properties " + filename + ": " + e);
        }
        return props;
    }
}
