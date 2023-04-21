package org.light.portal.util;

import static org.light.portal.util.Constants._DEFAULT_RESOURCE_BUNDLE;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 * @author Jianmin Liu
 **/
public class MessageUtil {

    public static String getMessage(String key, Locale locale) {
        String value = key;
        try {
            ResourceBundle resource = ResourceBundle.getBundle(_DEFAULT_RESOURCE_BUNDLE, locale);
            String temp = resource.getString(key);
            if (temp != null) value = temp;
        } catch (Exception e) {
        }
        return value;
    }
}
