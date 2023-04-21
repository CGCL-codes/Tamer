package com.halozat2009osz.base64;

import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

public class Base64 {

    private static String encTable = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv" + "wxyz0123456789 /";

    private static byte[] decTable;

    static {
        decTable = new byte[256];
        for (int i = 0; i < decTable.length; i++) decTable[i] = -1;
        for (int i = 0; i < encTable.length(); i++) decTable[encTable.charAt(i)] = (byte) i;
        decTable['='] = 0;
    }

    /**
	 * Returns an element from the given byte array at the given position in a
	 * safely manner. (Elements at indexes going out of bounds are treated as if
	 * they were zero bytes.)
	 * 
	 * @param src a byte array
	 * @param i a position in the array
	 * @return the element at the given position in the array, or 0 if the given
	 *         position exceeds the array boundaries.
	 */
    private static byte at(byte[] src, int i) {
        if (i > src.length - 1) return 0;
        return src[i];
    }

    public static String encode(byte[] src) {
        StringBuffer dst = new StringBuffer();
        byte[] tmp = new byte[4];
        for (int i = 0; i < src.length; i += 3) {
            tmp[0] = (byte) ((src[i] & 0xFC) >> 2);
            tmp[1] = (byte) (((src[i] & 0x03) << 4) | ((at(src, i + 1) & 0xF0) >> 4));
            tmp[2] = (byte) (((at(src, i + 1) & 0x0F) << 2) | ((at(src, i + 2) & 0xC0) >> 6));
            tmp[3] = (byte) (at(src, i + 2) & 0x3F);
            if (i + 1 > src.length - 1) tmp[2] = -1;
            if (i + 2 > src.length - 1) tmp[3] = -1;
            for (int j = 0; j < 4; j++) dst.append(tmp[j] != -1 ? encTable.charAt(tmp[j]) : '=');
        }
        return dst.toString();
    }

    public static byte[] decode(String src) throws InvalidParameterException {
        if (src.length() % 4 != 0) throw new InvalidParameterException("Invalid source string length");
        int numGroups = src.length() / 4;
        int skip = 0;
        while (skip != src.length() && src.charAt(src.length() - 1 - skip) == '=') skip++;
        if (skip > 2) throw new InvalidParameterException("Unexpected number of '=' characters: " + skip);
        byte[] dst = new byte[numGroups * 3 - skip];
        byte[] tmp = new byte[4];
        for (int i = 0; i < numGroups; i++) {
            for (int j = 0; j < 4; j++) {
                char c = src.charAt(4 * i + j);
                if (c == '=' && 4 * i + j < src.length() - skip) throw new InvalidParameterException("Unexpected character in source string: " + c);
                tmp[j] = decTable[c];
                if (tmp[j] == -1) throw new InvalidParameterException("Invalid character in source string: " + c);
            }
            dst[3 * i + 0] = (byte) (((tmp[0] & 0x3F) << 2) | ((tmp[1] & 0x30) >> 4));
            if (3 * i + 1 <= dst.length - 1) dst[3 * i + 1] = (byte) (((tmp[1] & 0x0F) << 4) | ((tmp[2] & 0x3C) >> 2));
            if (3 * i + 2 <= dst.length - 1) dst[3 * i + 2] = (byte) (((tmp[2] & 0x03) << 6) | (tmp[3] & 0x3F));
        }
        return dst;
    }

    /**
	 * Simple tester routine.
	 */
    private static void test() {
        ArrayList<byte[]> testArrays = new ArrayList<byte[]>();
        Charset charset = Charset.forName("UTF-8");
        testArrays.add("x".getBytes(charset));
        testArrays.add("y".getBytes(charset));
        testArrays.add("xy".getBytes(charset));
        testArrays.add("xyz".getBytes(charset));
        testArrays.add("xyzA".getBytes(charset));
        testArrays.add("This, is; a: test.".getBytes(charset));
        for (byte[] testArray : testArrays) {
            System.out.print("Testing \"" + new String(testArray, charset) + "\": ");
            if (!Arrays.equals(decode(encode(testArray)), testArray)) System.out.println("FAILED"); else System.out.println("OK");
        }
    }

    public static void main(String[] args) {
        test();
    }

    private Base64() {
    }
}
