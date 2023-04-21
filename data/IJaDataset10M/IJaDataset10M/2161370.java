package com.emental.mindraider;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import com.emental.mindraider.core.MindRaider;
import com.emental.mindraider.ui.panels.ProfileJPanel;

/**
 * Localized messages.
 *
 * @author <a href="mailto:fgiust@users.sourceforge.net">Fabrizio Giustina</a>
 * @author Francesco Tinti
 *
 * @version $Revision: 1.8 $ ($Author: mindraider $)
 */
public final class Messages {

    /**
     * The bundle name constant.
     */
    private static final String BUNDLE_NAME = "com.emental.mindraider.messages";

    /**
     * The ResourceBundle constant.
     */
    private static final ResourceBundle RESOURCE_BUNDLE;

    static {
        if (MindRaider.profile != null && MindRaider.profile.isOverrideSystemLocale()) {
            if (MindRaider.profile.getCustomLocale() != null) {
                if (MindRaider.profile.getCustomLocale().equals(ProfileJPanel.CZECH)) {
                    Locale.setDefault(new Locale("cs", "CZ"));
                    RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ENGLISH);
                } else {
                    if (MindRaider.profile.getCustomLocale().equals(ProfileJPanel.ENGLISH)) {
                        Locale.setDefault(Locale.ENGLISH);
                        RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ENGLISH);
                    } else {
                        if (MindRaider.profile.getCustomLocale().equals(ProfileJPanel.ITALIAN)) {
                            Locale.setDefault(Locale.ITALIAN);
                            RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ITALIAN);
                        } else {
                            RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
                        }
                    }
                }
            } else {
                RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
            }
        } else {
            RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
        }
    }

    /**
     * Private constructor.
     */
    private Messages() {
    }

    /**
     * Returns the message string for given key.
     *
     * @param key
     *            the key.
     * @return Returns the message.
     */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    /**
     * Returns the message string for given key and parameters.
     *
     * @param key
     *            the key.
     * @param params
     *            the parameters array.
     * @return Returns the message.
     */
    public static String getString(String key, Object[] params) {
        try {
            return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    /**
     * Returns the message string for given key and parameter.
     *
     * @param key
     *            the key.
     * @param param
     *            the parameter
     * @return Returns the message.
     */
    public static String getString(String key, Object param) {
        return getString(key, new Object[] { param });
    }
}
