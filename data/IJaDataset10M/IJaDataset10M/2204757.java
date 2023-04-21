package com.trendsoft.eye.cls;

/**
 * @author vgagin
 *
 * TODO Add JavaDoc
 */
public class JvmCommandLength {

    public static final int[] LEN = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, -1, -1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 5, -1, 3, 2, 3, 1, 1, 3, 3, 1, 1, -1, 4, 3, 3, 5, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

    public static int length(byte[] code, int offset) {
        int cop = code[offset] & 0xff;
        if (cop == 0xaa) {
            int n = (offset + 4) & (-4) + 4;
            int low = ByteSource.readInt(code, n);
            n += 4;
            int high = ByteSource.readInt(code, n);
            return n + 4 + (high - low + 1) * 4 - offset;
        }
        if (cop == 0xab) {
            throw new UnsupportedOperationException("COP: " + cop);
        }
        if (cop >= LEN.length) {
            throw new UnsupportedOperationException("COP: " + cop);
        }
        int len = LEN[cop];
        if (len <= 0) {
            throw new UnsupportedOperationException("COP: " + cop);
        }
        return len;
    }
}
