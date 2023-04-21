package de.wannawork.jcalendar;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class handles the Language-Property Files.
 * It was created by Eclipse
 * 
 * @author Bodo Tasche
 */
public class LocaleStrings {

    /**
	 * Bundle-Name
	 */
    private static final String BUNDLE_NAME = "de.wannawork.jcalendar.jcalendar";

    /**
	 * Creates the ResourceBundle
	 */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
	 * Gets a localized String
	 * @param key Key to get
	 * @return localized String
	 */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
