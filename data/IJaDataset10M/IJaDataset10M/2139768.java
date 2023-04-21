package com.hs.framework.common.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * General purpose utility class that describes an API for retrieving
 * Locale-sensitive messages from underlying resource locations of an
 * unspecified design, and optionally utilizing the <code>MessageFormat</code>
 * class to produce internationalized messages with parametric replacement.
 * <p>
 * Calls to <code>getMessage()</code> variants without a <code>Locale</code>
 * argument are presumed to be requesting a message string in the default
 * <code>Locale</code> for this JVM.
 * <p>
 * Calls to <code>getMessage()</code> with an unknown key.  Otherwise,
 * a suitable error message will be returned instead.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - Classes that extend this class
 * must be Serializable so that instances may be used in distributable
 * application server environments.
 *
 *@version  [Date] 2001.10.18
 *@author   [Creater] lifachun
 *@author   [Changer] lifachun
 */
public class MessageResources implements Serializable {

    /**
         * The configuration parameter used to initialize this MessageResources.
         */
    protected String name = null;

    /**
         * The ResourceBundle for this .
         */
    protected ResourceBundle resourceBundle;

    public String getName() {
        return name;
    }

    /**
         * The default Locale for our environment.
         */
    protected Locale defaultLocale = Locale.getDefault();

    /**
         * Construct a new MessageResources according to the specified parameters.
         *
         * @param name The configuration parameter for this MessageResources
         */
    public MessageResources(String name) {
        this.name = name;
        resourceBundle = ResourceBundle.getBundle(name);
    }

    public static void main(String[] args) {
        MessageResources mr = new MessageResources("E:\\jbproject\\study\\src\\test.zh_CN");
        mr.getMessage("ACTION");
    }

    /**
         * Returns a text message for the specified key, for the default Locale.
         *
         * @param key The message key to look up
         */
    public String getMessage(String key) {
        return getMessage(key, "???" + key + "???");
    }

    /**
         * Returns an integer value for the specified key.
         *
         * @param key The message key to look up
         */
    public int getInteger(String key) {
        return getInteger(key, 0);
    }

    /**
         * Returns an integer value for the specified key.
         *
         * @param key The message key to look up
         */
    public int getInteger(String key, int defaultValue) {
        int value = defaultValue;
        try {
            value = Integer.parseInt(getMessage(key));
        } catch (NumberFormatException mfe) {
            ;
        }
        return value;
    }

    /**
         * Returns a boolean value for the specified key.
         *
         * @param key The message key to look up
         */
    public boolean getBoolean(String key) {
        return Boolean.valueOf(getMessage(key)).booleanValue();
    }

    /**
         * Returns a text message for the specified key, for the default Locale.
         *
         * @param key The message key to look up
         */
    public String getMessage(String key, String defaultValue) {
        String value = defaultValue;
        try {
            value = resourceBundle.getString(key);
        } catch (MissingResourceException e) {
        }
        return value;
    }

    /**
         * Returns a text message after parametric replacement of the specified
         * parameter placeholders.
         *
         * @param key The message key to look up
         * @param args An array of replacement parameters for placeholders
         */
    public String getMessage(String key, Object args[]) {
        MessageFormat format = null;
        String formatString = getMessage(key);
        if (!isPresent(key)) {
            return formatString;
        }
        format = new MessageFormat(formatString);
        return (format.format(args));
    }

    /**
         * Return <code>true</code> if there is a defined message for the specified
         * key in the system default locale.
         *
         * @param key The message key to look up
         */
    public boolean isPresent(String key) {
        String message = getMessage(key);
        if (message == null) return false; else if (message.startsWith("???") && message.endsWith("???")) return false; else return true;
    }

    /**
         * Create and return an instance of <code>MessageResources</code> for the
         * created by the default <code>MessageResourcesFactory</code>.
         *
         * @param name Configuration parameter for this message bundle.
         */
    public static MessageResources getMessageResources(String name) {
        return new MessageResources(name);
    }
}
