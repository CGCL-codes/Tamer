package org.plazmaforge.studio.reportviewer;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * I18N
 * 
 */
class Messages {

    private static final String BUNDLE_NAME = "org.plazmaforge.studio.reportviewer.messages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private Messages() {
    }

    /**
	 * Returns the internationalized value for the given key
	 * 
	 * @param key the key
	 * @return internationalized value
	 */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
