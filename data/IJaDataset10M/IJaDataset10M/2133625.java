package org.rubato.rubettes.builtin;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

class Messages {

    private static final String BUNDLE_NAME = "org.rubato.rubettes.builtin.messages";

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
