package prest.xml;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Wrapper for the resource bundle containing properties for this extension
 *
 * @author Daniel Buchta
 * @author Peter Rybar
 */
public class XmlRpcMessages {

    private static final String BUNDLE_NAME = "prest.xml.XmlRpcMessages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
	 * Returns a message from the resource bundle corresponding to the given
	 * key.
	 *
	 * @param key
	 *            The message key.
	 * @return The message corresponding to the message key.
	 * @throws MissingResourceException
	 *             If the message is not found.
	 */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
