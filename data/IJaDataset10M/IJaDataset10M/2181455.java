package com.tensegrity.palobrowser.util;

/**
 * <code>StringUtilities</code>, simple string utilities.
 *
 * @author Stepan Rutz
 * @version $ID$
 */
public class StringUtilities {

    public static String getArrayString(int a[]) {
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < a.length; ++i) {
            if (i != 0) sb.append(",");
            sb.append(a[i]);
        }
        return sb.append("]").toString();
    }

    public static final String hashcode(Object object) {
        int ptr = System.identityHashCode(object);
        String s = Integer.toHexString(ptr);
        char buf[] = new char[10];
        buf[0] = '0';
        buf[1] = 'x';
        int l = s.length();
        int miss = 8 - l;
        for (int i = 0; i < miss; ++i) buf[2 + i] = '0';
        for (int i = 0; i < l; ++i) buf[2 + miss + i] = s.charAt(i);
        return new String(buf);
    }
}
