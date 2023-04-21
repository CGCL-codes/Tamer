package hu.scytha.common;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created on 02.02.2005
 * 
 * This class only has static methods.
 * 
 * @author Bertalan Lacza
 */
public class Messages {

    private static final String BUNDLE_NAME = "hu.scytha.common.messages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
    * Gets the text from the properties file.
    * @param key The name of the key
    * @return The text to the key
    */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    /**
    * Gets the text from the properties file, filled with arguments.
    * @param key The name of the key
    * @param arguments The values to replace 
    * @return The text to the key
    */
    public static String getString(String key, Object[] arguments) {
        String str = getString(key);
        String tmp = str;
        try {
            str = MessageFormat.format(str, arguments);
        } catch (MissingResourceException e) {
            str = tmp;
        }
        return str;
    }
}
