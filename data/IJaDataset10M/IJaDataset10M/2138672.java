package com.vmladenov.utils;

/**
 * User: invincible
 * Date: 2006-12-10
 * Time: 22:41:13
 */
public class StringUtils {

    public static boolean IsCommandChar(String str, Integer charAtPos) {
        Boolean res = false;
        int i = charAtPos;
        while (i > 0) {
            if (str.charAt(i - 1) == '\\') res = !res; else break;
            i--;
        }
        return res;
    }
}
