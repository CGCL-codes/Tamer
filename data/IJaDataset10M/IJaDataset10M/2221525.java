package org.yuzz.sql;

import java.util.*;
import java.io.*;
import java.text.*;
import java.lang.reflect.*;

/**
 * @author david.randolph.p2727
 *
 */
public class StringUtil {

    private static final char[] seperators = { '(', ')' };

    /**
	 * @return boolean
	 * @param obj1 java.lang.Object
	 * @param obj2 java.lang.Object
	 */
    public static final boolean compare(Object obj1, Object obj2) {
        boolean isEqual;
        if (obj1 == null) {
            isEqual = (obj2 == null) ? true : false;
        } else if (obj2 == null) {
            return false;
        } else {
            final String str1 = obj1.toString();
            final String str2 = obj2.toString();
            isEqual = str1.equals(str2);
        }
        return isEqual;
    }

    public static final boolean compare(String str1, String str2) {
        boolean isEqual;
        if (str1 == null) {
            isEqual = (str2 == null) ? true : false;
        } else if (str2 == null) {
            isEqual = false;
        } else {
            isEqual = str1.equals(str2);
        }
        return isEqual;
    }

    /**
	 * safer than obj.toString
	 * returns ifNull if null
	*/
    public static final String toString(Object obj, String ifNull) {
        if (obj == null) return ifNull;
        return obj.toString();
    }

    public static final String[] split(String s, char c) {
        java.util.Vector v = new java.util.Vector();
        int i = 0;
        char ch;
        while (i < s.length()) {
            StringBuffer buf = new StringBuffer();
            try {
                while (c != (ch = s.charAt(i++))) buf.append(ch);
            } catch (IndexOutOfBoundsException obe) {
            }
            v.addElement(buf.toString());
        }
        String[] items = new String[v.size()];
        v.copyInto(items);
        return items;
    }

    /**
	* like split(String,char) but always returns a String[count]
	* unused elements will be null
	**/
    public static final String[] split(String s, char c, int count) {
        String[] ss = new String[count];
        int pos = 0;
        for (int i = 0; i < count; i++) {
            int ptr = s.indexOf(c, pos);
            if (ptr >= 0) {
                ss[i] = s.substring(pos, ptr);
                pos = ptr + 1;
            } else {
                ss[i] = s.substring(pos);
                return ss;
            }
        }
        return ss;
    }

    public static final String combine(String[] sarray, String delim) {
        return combine(sarray, delim, 0, sarray.length - 1);
    }

    /**
	 * @param begin - zero based
	 * @param end - up to sarray.length -1
	 */
    public static final String combine(String[] sarray, String delim, int begin, int end) {
        StringBuffer buf = new StringBuffer();
        for (int i = begin; i <= end; i++) {
            buf.append(sarray[i]);
            if (i != end) {
                buf.append(delim);
            }
        }
        return buf.toString();
    }

    public static final String toMixedCaseString(String s) {
        if (s == null) return null;
        s.toUpperCase();
        StringTokenizer st = new StringTokenizer(s, " ");
        String address = "";
        while (st.hasMoreElements()) {
            String token = st.nextToken();
            String capital = (new String(token)).toUpperCase();
            if (st.hasMoreTokens()) {
                address = address + capital.substring(0, 1) + capital.substring(1).toLowerCase() + " ";
            } else {
                address = address + capital;
            }
        }
        return address;
    }

    public static final String fill(char value, int size) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < size; i++) {
            buf.append(value);
        }
        return buf.toString();
    }

    public static final String padR(String phrase, char value, int size) {
        int original_len = 0;
        StringBuffer buf = new StringBuffer();
        if (phrase != null) {
            original_len = phrase.length();
            if (size <= original_len) {
                return phrase;
            }
            buf.append(phrase);
        }
        int count = size - original_len;
        for (int i = 0; i < count; i++) {
            buf.append(value);
        }
        return buf.toString();
    }

    public static final String padL(String phrase, char value, int size) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < (size - (phrase.length())); i++) {
            buf.append(value);
        }
        buf.append(phrase);
        return buf.toString();
    }

    /**
	 * Replace all occurances of find with changeTo in original.
	 * see test/StringUtilTest.java for examples.
	 * Use String.replace(char,char) when possible as it is more efficient
	 * @see java.util.String#replace
	 * @param original
	 * @param find
	 * @param changeTo - can be ""
	 */
    public static final String replace(String original, String find, String changeTo) {
        int i;
        int findlen = find.length();
        StringBuffer buf = new StringBuffer();
        i = original.indexOf(find);
        int ptr = 0;
        while (i != -1) {
            if (ptr != i) {
                buf.append(original.substring(ptr, i));
            }
            ptr = i + findlen;
            buf.append(changeTo);
            i = original.indexOf(find, ptr);
        }
        buf.append(original.substring(ptr));
        return buf.toString();
    }

    public static final String replaceWholeWord(String original, String find, String changeTo) {
        int i;
        String realFind = new StringBuffer(find).append(" ").toString();
        int findlen = find.length();
        StringBuffer buf = new StringBuffer();
        i = original.indexOf(realFind);
        int ptr = 0;
        while (i != -1) {
            if (ptr != i) {
                buf.append(original.substring(ptr, i));
            }
            ptr = i + findlen;
            buf.append(changeTo);
            i = original.indexOf(realFind, ptr);
        }
        buf.append(original.substring(ptr));
        return buf.toString();
    }

    public static final String replaceFirst(String original, String find, String with) {
        int i;
        int findlen = find.length();
        StringBuffer buf = new StringBuffer();
        i = original.indexOf(find);
        if (i == -1) {
            return original;
        }
        if (0 != i) {
            buf.append(original.substring(0, i));
        }
        buf.append(with);
        buf.append(original.substring(i + findlen));
        return buf.toString();
    }

    public static final boolean nullOrEmpty(String s) {
        return (s == null ? true : (s.trim().length() == 0));
    }

    public static final String emptyIfNull(String s) {
        return s == null ? "" : s;
    }

    public static final String nbspIfNull(String s) {
        return s == null ? "&nbsp;" : s;
    }

    public static final String nbspIfNullorEmpty(String str) {
        return (str == null) || (str.length() == 0) ? "&nbsp;" : str;
    }

    public static final String trimOrEmpty(String str) {
        if (str == null) {
            return "";
        } else {
            return str.trim();
        }
    }

    public static final byte[] toBytes(String value) {
        final int len = value.length();
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) value.charAt(i);
        }
        return bytes;
    }

    public static final String toString(byte[] value) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < value.length; i++) {
            buf.append((char) value[i]);
        }
        return buf.toString();
    }

    public static final String[] wordWrap(String line, int maxlen) {
        Vector lines = new Vector();
        String[] ss = split(line.trim(), '\n');
        for (int i = 0; i < ss.length; i++) {
            String l = ss[i];
            while (l.length() > maxlen) {
                int lastwordbreak = l.lastIndexOf(' ', maxlen);
                if (lastwordbreak < 1) lastwordbreak = maxlen;
                lines.addElement(l.substring(0, lastwordbreak).trim());
                l = l.substring(lastwordbreak).trim();
            }
            l = l.trim();
            if (l.length() > 0) {
                lines.addElement(l);
            }
        }
        String[] items = new String[lines.size()];
        lines.copyInto(items);
        return items;
    }

    public static String trim(String str, char white) {
        int len = str.length();
        int count = len;
        int start = 0;
        int off = 0;
        while ((start < len) && (str.charAt(off + start) == white)) {
            start++;
        }
        while ((start < len) && (str.charAt(off + len - 1) == white)) {
            len--;
        }
        String retval = ((start > 0) || (len < count)) ? str.substring(start, len) : str;
        return retval;
    }

    public static final boolean isExistsInArray(String str, String[][] strArray, int col) {
        for (int i = 0; i < strArray.length; i++) {
            if (strArray[i][col].equals(str)) {
                return true;
            }
        }
        return false;
    }

    public String getString(String[] strArray) {
        StringBuffer strBuffer = new StringBuffer("");
        for (int i = 0; i < strArray.length; i++) {
            strBuffer.append(strArray[i]);
            strBuffer.append(" ");
        }
        return strBuffer.toString();
    }

    /**
	 * split on any char in chars;treat consecutive delimiters as one
	 * @param chars split on any charector in the String chars
	 */
    public static final String[] split(String s, String chars) {
        java.util.Vector v = new java.util.Vector();
        int i = 0;
        char ch;
        while (i < s.length()) {
            StringBuffer buf = new StringBuffer();
            try {
                ch = s.charAt(i++);
                while (chars.indexOf(ch) == -1) {
                    buf.append(ch);
                    ch = s.charAt(i++);
                }
                ch = s.charAt(i);
                while (chars.indexOf(ch) != -1) {
                    i++;
                    ch = s.charAt(i);
                }
            } catch (IndexOutOfBoundsException obe) {
            }
            v.addElement(buf.toString());
        }
        String[] items = new String[v.size()];
        v.copyInto(items);
        return items;
    }

    /**
	 * Returns a new string resulting from replacing all occurrences of old characters in this string with a new character.
	 */
    public static final String replace(String orgStr, String oldChars, char newChar) {
        String newStr = orgStr;
        for (int i = 0; i < oldChars.length(); i++) {
            newStr = newStr.replace(oldChars.charAt(i), newChar);
        }
        return newStr;
    }

    public static final String truncate(String orig, int maxlen) {
        if (orig.length() > maxlen) {
            return orig.substring(0, maxlen);
        }
        return orig;
    }

    public static final String removeChar(String instr, char ch) {
        int i = instr.indexOf(ch);
        while (i != -1) {
            instr = instr.substring(0, i) + instr.substring(i + 1, instr.length());
            i = instr.indexOf(ch);
        }
        return instr;
    }

    public static final String fillChar(String phrase, String ch) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < phrase.length(); i++) {
            buf.append(ch);
        }
        return buf.toString();
    }

    public static String join(List list, char delim) {
        int len = list.size();
        if (len == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < (len - 1); i++) {
            sb.append(list.get(i));
            sb.append(delim);
        }
        sb.append(list.get(len - 1));
        return sb.toString();
    }

    public static String join(String[] sa, char delim) {
        int len = sa.length;
        if (len == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < (len - 1); i++) {
            sb.append(sa[i]);
            sb.append(delim);
        }
        sb.append(sa[len - 1]);
        return sb.toString();
    }

    public static String join(char c, String... strings) {
        return join(strings, c);
    }

    public static String join(char delim, Object... sa) {
        int len = sa.length;
        if (len == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < (len - 1); i++) {
            sb.append(toString(sa[i], ""));
            sb.append(delim);
        }
        sb.append(sa[len - 1]);
        return sb.toString();
    }
}
