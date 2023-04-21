package jmri.jmrit.throttle;

import java.util.ResourceBundle;

/**
 * Common access to the ThrottleBundle of properties.
 *
 * Putting this in a class allows it to be loaded only
 * once.
 *
 * @author Bob Jacobsen  Copyright 2010
 * @since 2.9.5
 * @version $Revision: 1.1 $
 */
public class ThrottleBundle {

    public static final ResourceBundle b = java.util.ResourceBundle.getBundle("jmri.jmrit.throttle.ThrottleBundle");

    public static ResourceBundle bundle() {
        return b;
    }
}
