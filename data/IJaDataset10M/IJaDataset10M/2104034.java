package org.identifylife.key.player.gwt.shared.utils;

/**
 * @author dbarnier
 *
 */
public class StringUtils {

    public static boolean valueIsSet(String value) {
        if (value != null) {
            return value.trim().length() > 0;
        }
        return false;
    }
}
