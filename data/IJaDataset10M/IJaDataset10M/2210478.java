package org.beanstalk4j.utils;

public class StringUtils {

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    public static boolean isBlank(String s) {
        if ((s == null) || (s.length()) == 0) {
            return true;
        } else {
            for (int i = 0; i < s.length(); ++i) {
                if (!(Character.isWhitespace(s.charAt(i)))) {
                    return false;
                }
            }
            return true;
        }
    }
}
