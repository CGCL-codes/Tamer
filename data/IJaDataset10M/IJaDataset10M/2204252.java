package com.d_project.xprint.barcode;

/**
 * ModUtil
 * @author Kazuhiko Arase
 */
public class ModUtil {

    public static char calcMod43(String data) {
        int sum = 0;
        for (int i = 0; i < data.length(); i++) {
            int d = CODE39.charToInt(data.charAt(i));
            if (d == -1) {
                throw new RuntimeException("illegal char " + data + " at " + (i + 1));
            }
            sum += d;
        }
        return (char) CODE39.intToChar(sum % 43);
    }

    private static int charToInt(int c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        } else {
            return -1;
        }
    }

    public static char calcMod10_3W(String data) {
        int sum = 0;
        for (int i = 0; i < data.length(); i++) {
            int d = charToInt(data.charAt(i));
            if (d == -1) {
                throw new RuntimeException("illegal char " + data + " at " + (i + 1));
            }
            sum += d * (((data.length() - i) % 2) * 2 + 1);
        }
        int r = sum % 10;
        return (r != 0) ? (char) ((10 - r) + '0') : '0';
    }
}
