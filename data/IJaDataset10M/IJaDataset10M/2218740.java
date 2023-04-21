package org.geometerplus.zlibrary.core.util;

public class ZLUnicodeUtil {

    public static int utf8Length(byte[] buffer, int str, int len) {
        final int last = str + len;
        int counter = 0;
        while (str < last) {
            final int bt = buffer[str];
            if ((bt & 0x80) == 0) {
                ++str;
            } else if ((bt & 0x20) == 0) {
                str += 2;
            } else if ((bt & 0x10) == 0) {
                str += 3;
            } else {
                str += 4;
            }
            ++counter;
        }
        return counter;
    }
}
