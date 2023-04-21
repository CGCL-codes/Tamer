package org.wings.session;

import javax.swing.UIManager;
import java.util.*;

/**
 * User: hengels Date: May 11, 2009 Time: 2:42:27 PM
 */
public class DefaultLocalizer implements Localizer {

    public String getString(String key, Locale locale) {
        return UIManager.getString(key, locale);
    }
}
