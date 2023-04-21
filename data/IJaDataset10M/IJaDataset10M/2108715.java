package ch.wess.ezclipse.communication.nl1;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Auto generated class.
 * 
 * @author Alain Sahli (a.sahli[at]wess.ch)
 */
public class Messages {

    private static final String BUNDLE_NAME = "ch.wess.ezclipse.communication.nl1.messages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private Messages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
