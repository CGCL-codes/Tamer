package com.struts.tool.helpers;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class IntegerHelper {

    public static boolean isInteger(String str) {
        boolean isInteger = true;
        try {
            int i = Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            isInteger = false;
        }
        return isInteger;
    }
}
