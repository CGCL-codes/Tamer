package org.openexi.proc.util;

public final class HexBin {

    private static final String HEXBIN_ASCIIS = "0123456789ABCDEF";

    public static void encode(byte[] octets, StringBuffer encodingResult) {
        if (octets != null && encodingResult != null) {
            int i, len;
            for (i = 0, len = octets.length; i < len; i++) {
                int dec0, dec1;
                if ((dec0 = (octets[i] >> 4)) < 0) dec0 &= 0x000F;
                dec1 = octets[i] & 0x000F;
                encodingResult.append(HEXBIN_ASCIIS.charAt(dec0));
                encodingResult.append(HEXBIN_ASCIIS.charAt(dec1));
            }
        }
    }

    public static int decode(String norm, final byte[] octets) {
        int pos, len, dec, opos;
        for (pos = opos = 0, len = norm.length(), dec = 0; pos < len; dec = 0) {
            int nc;
            for (nc = 0; nc < 2 && pos < len; pos++) {
                char c = norm.charAt(pos);
                if (Character.isWhitespace(c)) {
                    continue;
                }
                if ('`' < c) {
                    if ('f' < c) {
                        return -1;
                    } else dec |= (10 + (c - 'a')) << (4 * (1 - nc++));
                } else if (c < ':') {
                    if (c < '0') {
                        return -1;
                    } else dec |= (c - '0') << (4 * (1 - nc++));
                } else if ('@' < c && c < 'G') {
                    dec |= (10 + (c - 'A')) << (4 * (1 - nc++));
                } else {
                    return -1;
                }
            }
            if (nc == 1) return -1;
            if (nc != 0) octets[opos++] = (byte) dec; else {
                assert pos == len;
                return opos;
            }
        }
        return opos;
    }
}
