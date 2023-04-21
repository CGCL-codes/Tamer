package com.rise.rois.server.util;

public class InputUtil {

    public static boolean isAlphaNumeric(String str) {
        for (char ch : str.toCharArray()) {
            if (!isAlphaNumeric(ch)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaNumeric(char ch) {
        if ((ch >= '0') && (ch <= '9')) {
            return true;
        }
        if ((ch >= 'A') && (ch <= 'Z')) {
            return true;
        }
        if ((ch >= 'a') && (ch <= 'z')) {
            return true;
        }
        if (ch == ' ') {
            return true;
        }
        return false;
    }
}
