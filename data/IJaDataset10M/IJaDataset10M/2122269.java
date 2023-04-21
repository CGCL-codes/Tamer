package net.sf.jimo.platform.tests;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

    private static ResourceBundle bundle = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".messages");

    public static String getMessage(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "??" + key + "??";
        }
    }

    public static String formatMessage(String pattern, Object... arguments) {
        return MessageFormat.format(Messages.getMessage(pattern), arguments);
    }
}
