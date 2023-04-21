package org.jgebafe;

import java.util.*;

public final class StringUtils {

    public static final String ln = System.getProperty("line.separator");

    public static boolean isStrValid(String s) {
        return !(s == null || s.equals(""));
    }

    public static boolean areEquals(String s1, String s2) {
        if (s1 != null && s2 != null) return s1.equals(s2);
        if (s1 == null && s2 == null) return true;
        return false;
    }

    public static String toString(List list) {
        String res = "";
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            String str = (String) i.next();
            res += str + StringUtils.ln;
        }
        return res;
    }

    public static String toString(long n, int numDigit) {
        String res = "" + n;
        if (n < 0) return res;
        int numZeros = numDigit - res.length();
        for (int i = 0; i < numZeros; i++) res = "0" + res;
        return res;
    }
}
