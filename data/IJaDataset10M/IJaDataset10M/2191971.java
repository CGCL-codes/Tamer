package org.goodjava.ezpz.util;

import java.text.MessageFormat;

/**
 * MessageUtils is written so that the message.properties file is loaded only
 * once into the ResourceBundle, and so that this class only gets instantiated
 * once.
 * @author Matt created on Aug 18, 2005
 */
public final class MessageUtils {

    private static final String PROPERTY_FILE = "messages";

    private MessageUtils() {
        throw new UnsupportedOperationException();
    }

    public static String get(final String messageKey) {
        return PropertyFileReader.getProperty(PROPERTY_FILE, messageKey);
    }

    public static String get(final String messageKey, final Object[] data) {
        String message = get(messageKey);
        return MessageFormat.format(message, data);
    }
}
