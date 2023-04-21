package org.ibex.crypto;

import java.io.*;

public final class Base64 {

    private Base64() {
    }

    private static final char[] encodeMap = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

    private static final int[] decodeMap = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };

    public static void main(String[] args) throws IOException {
        if (args.length == 2 && args[0].equals("encode")) System.out.println(encode(getBytes(args[1]))); else if (args.length == 2 && args[0].equals("decode")) System.out.write(decode(args[1])); else if (args.length == 2 && args[0].equals("encodefile")) {
            FileInputStream fis = new FileInputStream(args[1]);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n;
            while ((n = fis.read(buf)) != -1) baos.write(buf, 0, n);
            fis.close();
            System.out.println(encode(baos.toByteArray()));
        } else {
            System.out.println("Usage: Base64 {encode,decode} filename");
            System.exit(1);
        }
        System.exit(0);
    }

    public static byte[] decode(String inString) {
        char[] in = inString.toCharArray();
        int part = 0;
        int theBytes = 0;
        byte[] out = new byte[in.length / 4 * 3];
        int outPos = 0;
        for (int i = 0; i < in.length; i++) {
            int x = decodeMap[in[i] & 0x7f];
            if (x == -1) continue;
            if (x == -2) break;
            theBytes = (theBytes << 6) | x;
            part++;
            if (part == 4) {
                part = 0;
                out[outPos++] = (byte) ((theBytes >>> 16) & 0xff);
                out[outPos++] = (byte) ((theBytes >>> 8) & 0xff);
                out[outPos++] = (byte) ((theBytes >>> 0) & 0xff);
            }
        }
        switch(part) {
            case 3:
                out[outPos++] = (byte) ((theBytes >>> 10) & 0xff);
                out[outPos++] = (byte) ((theBytes >>> 2) & 0xff);
                break;
            case 2:
                out[outPos++] = (byte) ((theBytes >> 4) & 0xff);
                break;
        }
        if (outPos < out.length) {
            byte[] a = new byte[outPos];
            System.arraycopy(out, 0, a, 0, outPos);
            return a;
        } else {
            return out;
        }
    }

    public static String encode(String in) {
        return encode(getBytes(in));
    }

    public static String encode(byte[] in) {
        int part = 0;
        int theBytes = 0;
        char[] out = new char[(in.length / 3 + 1) * 4];
        int outPos = 0;
        for (int i = 0; i < in.length; i++) {
            theBytes = (theBytes << 8) | (in[i] & 0xff);
            part++;
            if (part == 3) {
                part = 0;
                out[outPos++] = encodeMap[(theBytes >>> 18) & 0x3f];
                out[outPos++] = encodeMap[(theBytes >>> 12) & 0x3f];
                out[outPos++] = encodeMap[(theBytes >>> 6) & 0x3f];
                out[outPos++] = encodeMap[(theBytes >>> 0) & 0x3f];
            }
        }
        switch(part) {
            case 2:
                out[outPos++] = encodeMap[(theBytes >>> 10) & 0x3f];
                out[outPos++] = encodeMap[(theBytes >>> 4) & 0x3f];
                out[outPos++] = encodeMap[(theBytes << 2) & 0x3f];
                out[outPos++] = '=';
                break;
            case 1:
                out[outPos++] = encodeMap[(theBytes >>> 2) & 0x3f];
                out[outPos++] = encodeMap[(theBytes << 4) & 0x3f];
                out[outPos++] = '=';
                out[outPos++] = '=';
                break;
        }
        return new String(out, 0, outPos);
    }

    public static byte[] getBytes(String s) {
        try {
            return s.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new Error("should never happen");
        }
    }
}
