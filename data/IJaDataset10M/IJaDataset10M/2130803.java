package org.wcb.resources;

import java.util.ResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * <small>
 * Copyright (c)  2006  wbogaardt.
 * Permission is granted to copy, distribute and/or modify this document
 * under the terms of the GNU Free Documentation License, Version 1.2
 * or any later version published by the Free Software Foundation;
 * with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
 * Texts.  A copy of the license is included in the section entitled "GNU
 * Free Documentation License".
 * <p/>
 * $File:  $ <br>
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Apr 10, 2006 3:12:38 PM $ <br>
 * </small>
 *
 * @author wbogaardt
 * @version 1
 *          Date: Apr 10, 2006
 *          Time: 3:12:38 PM
 */
public final class MessageResourceRegister {

    private static final String PATH = "org.wcb.resources.";

    private static MessageResourceRegister instanceValue;

    private ResourceBundle rbInternationalization;

    private MessageResourceRegister() {
        Locale currentLocal = new Locale("en", "US");
        try {
            rbInternationalization = ResourceBundle.getBundle(PATH + "jPilotResource", currentLocal, MessageResourceRegister.class.getClassLoader());
        } catch (MissingResourceException mre) {
            System.err.println("No resource bundle found" + mre);
        }
    }

    /**
     * Gets an instance val of this register.
     * @return The instance.
     */
    public static MessageResourceRegister getInstance() {
        if (instanceValue == null) {
            instanceValue = new MessageResourceRegister();
        }
        return instanceValue;
    }

    /**
     * The value for the key requested.
     * @param key The key
     * @return String text value.
     */
    public String getValue(String key) {
        return rbInternationalization.getString(key);
    }
}
