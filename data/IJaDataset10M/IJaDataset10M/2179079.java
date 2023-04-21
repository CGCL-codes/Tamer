package com.netstoke.security;

import java.io.Serializable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <p>The <code>Messages</code> class is used to externalize Strings and messages for i18n purposes 
 * for the com.netstoke.security package.
 * @author kmckee &lt;<a href="mailto:kevin.mckee@netstoke.com">kevin.mckee@netstoke.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class Messages implements Serializable {

    static final long serialVersionUID = -7566340057990467448L;

    private static final String BUNDLE_NAME = "com.netstoke.security.messages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
	 * Common Constructor.
	 */
    private Messages() {
    }

    /**
	 * Returns the i18n message.
	 * @param key String
	 * @return String
	 */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
