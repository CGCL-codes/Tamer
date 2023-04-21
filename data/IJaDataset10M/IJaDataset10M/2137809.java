package org.eclipse.jface.text;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

class JFaceTextMessages {

    private static final String RESOURCE_BUNDLE = "org.eclipse.jface.text.JFaceTextMessages";

    private static ResourceBundle fgResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);

    private JFaceTextMessages() {
    }

    public static String getString(String key) {
        try {
            return fgResourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }
}
