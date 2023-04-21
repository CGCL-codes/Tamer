package com.tensegrity.palobrowser.wizards.saveasview;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * {@<describe>}
 * <p>
 * TODO: DOCUMENT ME
 * </p>
 * {@</describe>}
 *
 * @author ArndHouben
 * @version $Id$
 */
public class SaveAsViewMessages {

    private static final String BUNDLE_NAME = "com.tensegrity.palobrowser.wizards.saveasview.SaveAsViewMessages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private SaveAsViewMessages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
