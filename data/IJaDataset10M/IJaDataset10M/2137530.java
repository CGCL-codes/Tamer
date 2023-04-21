package edu.princeton.wordnet.browser;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Localizer
 * 
 * @author <a href="mailto:bbou@ac-toulouse.fr">Bernard Bou</a>
 */
public class Messages {

    /**
	 * Resource bundle name
	 */
    private static final String BUNDLE_NAME = "edu.princeton.wordnet.browser.messages";

    /**
	 * Resource bundle
	 */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(Messages.BUNDLE_NAME);

    /**
	 * Constructor
	 */
    private Messages() {
    }

    /**
	 * Get localized string
	 * 
	 * @param key
	 *            key
	 * @return string value
	 */
    public static String getString(final String key) {
        try {
            return Messages.RESOURCE_BUNDLE.getString(key);
        } catch (final MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
