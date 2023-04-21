package com.khan.net;

public class Base64 {

    private static final char[] legalChars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

    private Base64() {
    }

    public static int Base64Length(String str) {
        if (str == null && str.equals("")) return 0;
        int off = str.indexOf("=");
        if (off > -1) str = str.substring(0, off);
        int len = str.length();
        int mod = (len % 4);
        return len / 4 * 3 + (mod == 0 ? 0 : mod - 1);
    }

    public static byte Base64Char2Byte(char c) {
        byte b = (byte) c;
        if (b >= 0x41 && b <= 0x5A) {
            return (byte) (b - 0x41);
        }
        if (b >= 0x61 && b <= 0x7A) {
            return (byte) (b - 0x61 + 26);
        }
        if (b >= 0x30 && b <= 0x39) {
            return (byte) (b - 0x30 + 52);
        }
        if (b == 0x2B) {
            return 62;
        }
        if (b == 0x2F) {
            return 63;
        }
        return -1;
    }

    public static String Encode(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer("");
        int len = data.length;
        int i = 0;
        while (i < len) {
            if (len < i + 3) break;
            byte bts = 0;
            byte bt = (byte) (data[i] & 0x03);
            bts = (byte) ((data[i] >> 2) & 0x3F);
            sb.append(legalChars[bts]);
            bts = (byte) ((bt << 4) & 0xF0);
            bt = (byte) (data[i + 1] & 0x0F);
            bts = (byte) (((data[i + 1] >> 4) & 0x0F) | bts);
            sb.append(legalChars[(int) bts]);
            bts = (byte) ((bt << 2) & 0xFC);
            bt = (byte) (data[i + 2] & 0x3F);
            bts = (byte) (((data[i + 2] >> 6) & 0x03) | bts);
            sb.append(legalChars[(int) bts]);
            sb.append(legalChars[(int) bt]);
            i = i + 3;
        }
        int mod = len % 3;
        if (mod == 1) {
            byte bt = (byte) (data[i] & 0x03);
            byte bts = (byte) ((data[i] >> 2) & 0x3F);
            sb.append(legalChars[(int) bts]);
            sb.append(legalChars[(bt << 4) & 0xF0]);
            sb.append("==");
        }
        if (mod == 2) {
            byte bt = (byte) (data[i] & 0x03);
            byte bts = (byte) ((data[i] >> 2) & 0x3F);
            sb.append(legalChars[(int) bts]);
            bts = (byte) ((bt << 4) & 0xF0);
            bt = (byte) (data[i + 1] & (byte) 0x0F);
            bts = (byte) (((data[i + 1] >> 4) & 0x0F) | bts);
            sb.append(legalChars[(int) bts]);
            sb.append(legalChars[((bt << 2) & 0xFC)]);
            sb.append('=');
        }
        return sb.toString();
    }

    public static byte[] Decode(String str) {
        if (str == null || str.length() < 4) {
            return null;
        }
        int off = str.indexOf("=");
        if (off > -1) str = str.substring(0, off);
        byte[] bts = new byte[str.length()];
        int len = bts.length;
        for (int i = 0; i < bts.length; ++i) {
            char c = str.charAt(i);
            if (c != '=') {
                bts[i] = Base64Char2Byte(c);
                if (bts[i] < 0) {
                    bts = null;
                    return bts;
                }
            }
        }
        int step = len / 4;
        int residue = len % 4;
        for (int i = 0; i < step; ++i) {
            byte b0 = bts[i * 4];
            byte b1 = bts[i * 4 + 1];
            byte b2 = bts[i * 4 + 2];
            byte b3 = bts[i * 4 + 3];
            bts[i * 4] = (byte) ((b0 << 2) | (b1 >> 4));
            bts[i * 4 + 1] = (byte) ((b1 << 4) | (b2 >> 2));
            bts[i * 4 + 2] = (byte) ((b2 << 6) | b3);
            bts[i * 4 + 3] = 0x00;
        }
        int offset = len - residue;
        for (int i = 0; i < residue; ++i) {
            if (i + 1 < residue) {
                byte b1 = bts[offset + i];
                byte b2 = bts[offset + i + 1];
                byte swap = (byte) (b2 >> (8 / (int) Math.pow(2, i + 1)));
                bts[offset + i] = (byte) ((b1 << (2 * (i + 1))) | swap);
                bts[offset + i + 1] = (byte) (b2 & (0xF / (int) Math.pow(2, i * 2)));
            }
        }
        byte[] result = new byte[Base64Length(str)];
        for (int i = 0; i < step; ++i) {
            System.arraycopy(bts, i * 4, result, i * 3, 3);
        }
        System.arraycopy(bts, len - residue, result, step * 3, result.length % 3);
        bts = null;
        return result;
    }
}
