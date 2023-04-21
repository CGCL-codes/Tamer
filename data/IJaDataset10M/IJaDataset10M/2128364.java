package com.luxoft.fitpro.htmleditor.core.messages;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class CoreMessages {

    private static final String BUNDLE_NAME = "com.luxoft.fitpro.htmleditor.core.messages.messages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private CoreMessages() {
    }

    public static String getMessage(String key, Object... args) {
        try {
            return MessageFormat.format(RESOURCE_BUNDLE.getString(key), args);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
