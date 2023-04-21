package de.juwimm.swing;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id: Messages.java 8 2009-02-15 08:54:54Z skulawik $
 */
public final class Messages {

    private static final String BUNDLE_NAME = "de.juwimm.swing.Messages";

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
