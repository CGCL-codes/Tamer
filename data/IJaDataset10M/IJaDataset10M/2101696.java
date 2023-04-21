package org.plazmaforge.bsolution.purchase.client.swt.forms;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/** 
 * @author Oleh Hapon
 * $Id: Messages.java,v 1.2 2010/04/28 06:31:04 ohapon Exp $
 */
public class Messages {

    private static final String BUNDLE_NAME = "org.plazmaforge.bsolution.purchase.client.swt.forms.messages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private Messages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }
}
