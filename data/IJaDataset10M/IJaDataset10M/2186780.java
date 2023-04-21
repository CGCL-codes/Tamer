package de.spieleck.util;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

/**
 * A utility class providing operations for String objects
 * @author  Patrick Carl
 * @author  fsn
 */
public class StringUtil {

    /**
     * Tokenize a String.
     * @param string A string to tokenize
     * @param delim the token separator char
     * @return the tokens of the given String as a Set. A token of the
     * given String is delimited by the given delim char.
     */
    public static Set getTokensAsSet(String string, char delim) {
        if (string == null || string.length() == 0) return Collections.EMPTY_SET;
        HashSet res = new HashSet();
        int from = 0;
        int index = string.indexOf(delim);
        if (index == -1) res.add(string);
        while (index != -1) {
            res.add(string.substring(from, index));
            from = index + 1;
            index = string.indexOf(delim, from);
        }
        res.add(string.substring(string.lastIndexOf(delim) + 1));
        return res;
    }

    /**
     * Tokenize a String.
     * Note: According to superficial benchmarks String.indexOf() is 
     * faster even as * special purpose Set implementations for delimiter
     * numbers less than about 30.
     * @param str A string to tokenize
     * @param delims A set of characters to separate tokens
     * @return the tokens of the given String as a Set. A token of the
     * given String is delimited by the given delim char.
     */
    public static Set getTokensAsSet(String str, String delims) {
        if (str == null) return Collections.EMPTY_SET;
        int pos = 0;
        int length = str.length();
        while (pos < length && delims.indexOf(str.charAt(pos)) != -1) pos++;
        if (pos >= length) return Collections.EMPTY_SET;
        HashSet res = new HashSet();
        do {
            int start = pos;
            while (pos < length && delims.indexOf(str.charAt(pos)) == -1) pos++;
            res.add(str.substring(start, pos));
            while (pos < length && delims.indexOf(str.charAt(pos)) != -1) pos++;
        } while (pos < length);
        return res;
    }

    /**
     * Replace characters in a String.
     * @param str String to modify
     * @param old "Set" of old characters
     * @param neu "Set" of new characters
     */
    public static String translate(String str, String old, String neu) {
        if (str == null) return null;
        if (old == null) return str;
        int neuLen = -1;
        if (neu != null) neuLen = neu.length();
        StringBuffer res = new StringBuffer(str.length());
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            int index = old.indexOf(ch);
            if (index == -1) {
                res.append(ch);
            } else if (index < neuLen) {
                res.append(neu.charAt(index));
            }
        }
        return res.toString();
    }
}
