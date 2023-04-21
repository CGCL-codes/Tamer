package cej.jhebrew.resources;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author cjung
 *
 */
public class JHebrewMessages {

    private static ResourceBundle messages = null;

    private JHebrewMessages() {
    }

    /**
     * @param key
     * @return an i18n-ed message
     */
    public static String getMessage(String key) {
        if (messages == null) {
            messages = ResourceBundle.getBundle("cej.jhebrew.resources.MessageBundle");
        }
        try {
            return messages.getString(key);
        } catch (MissingResourceException e) {
            return "";
        }
    }
}
