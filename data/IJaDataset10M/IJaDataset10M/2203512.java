package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import java.util.ResourceBundle;

/**
 * The messages class for the layout editor.
 */
public final class LayoutMessages {

    /**
     * The resource bundle name
     */
    private static final String BUNDLE_NAME = "com.volantis.mcs.eclipse.ab.editors.layout.LayoutMessages";

    /**
     * The resource bundle
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Prevents instances of this class from being created
     */
    private LayoutMessages() {
    }

    /**
     * Get a keyed property message as a String. This method delegates to
     * EclipseCommonMessages.getString().
     *
     * @param key The message key.
     * @return The message or null if no message is found.
     */
    public static String getString(String key) {
        return EclipseCommonMessages.getString(getResourceBundle(), key);
    }

    /**
     * Get a keyed property message as an Integer. This method delegates to
     * EclipseCommonMessages.getInteger().
     *
     * @param key The message key.
     * @return The message or null if no message is found.
     */
    public static Integer getInteger(String key) {
        return EclipseCommonMessages.getInteger(getResourceBundle(), key);
    }

    /**
     * Get the ResourceBundle for Layouts.
     *
     * @return The Controls ResourceBundle.
     */
    public static ResourceBundle getResourceBundle() {
        return RESOURCE_BUNDLE;
    }
}
