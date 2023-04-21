package com.ibm.celldt.debug.be.cdi.ui.pages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @author Ricardo M. Matinata
 * @since 1.3
 *
 */
public class BEUIMessages {

    private static final String BUNDLE_NAME = "com.ibm.celldt.debug.be.cdi.ui.pages.BEUIMessages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private BEUIMessages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
