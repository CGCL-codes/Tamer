package org.zkoss.dynclass;

import java.beans.Introspector;

/**
 * Utility methods to encode / decode arbitrary strings as legal
 * Java identifiers.
 */
class PropertyMethodNameTx {

    private static String encodeArbitrary(String str) {
        StringBuffer encoded = new StringBuffer("$");
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);
            if (Character.isJavaIdentifierPart(ch) && ch != '$') {
                encoded.append(ch);
            } else {
                encoded.append('$');
                encoded.append(Integer.toString(ch));
                encoded.append('$');
            }
        }
        return encoded.toString();
    }

    public static String encodeProperty(String str) {
        if (str.length() == 0 || !Character.isJavaIdentifierStart(str.charAt(0)) || str.charAt(0) == '$') {
            return encodeArbitrary(str);
        }
        for (int i = str.length() - 1; i > 0; i--) {
            if (!Character.isJavaIdentifierPart(str.charAt(i))) {
                return encodeArbitrary(str);
            }
        }
        String encoded = Character.toUpperCase(str.charAt(0)) + str.substring(1);
        if (str.equals(Introspector.decapitalize(encoded))) {
            return encoded;
        }
        return encodeArbitrary(str);
    }

    private static String decodeArbitrary(String str, int startIndex) {
        int len = str.length();
        StringBuffer sb = new StringBuffer(len);
        int i = startIndex + 1;
        while (i < len) {
            char ch = str.charAt(i++);
            if (ch != '$') {
                sb.append(ch);
            } else {
                int val = 0;
                try {
                    ch = str.charAt(i++);
                    while (ch != '$') {
                        val *= 10;
                        val += (ch - '0');
                        ch = str.charAt(i++);
                    }
                    sb.append((char) val);
                } catch (StringIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Invalid encoded property: " + str);
                }
            }
        }
        return sb.toString();
    }

    public static String decodeProperty(String str, int startIndex) {
        if (str.charAt(startIndex) == '$') {
            return decodeArbitrary(str, startIndex);
        }
        return Introspector.decapitalize(str.substring(startIndex));
    }
}
