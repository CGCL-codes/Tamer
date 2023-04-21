package org.apptools.ui.swing;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SwingUITexts {

    private static final String BUNDLE_NAME = "org.apptools.ui.swing.swinguitexts";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private SwingUITexts() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
