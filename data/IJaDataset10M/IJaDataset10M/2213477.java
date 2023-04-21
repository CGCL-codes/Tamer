package org.aspectbrains.contractj.paramprecondition.vanilla;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

@SuppressWarnings("all")
class Messages {

    private static final String BUNDLE_NAME = "org.aspectbrains.contractj.paramprecondition.vanilla.messages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private Messages() {
    }

    public static String getString(final String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (final MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
