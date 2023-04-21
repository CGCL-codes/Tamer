package com.brokat.usr.util;

import com.brokat.usr.Globals;

public class BByteUtils {

    private static final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
		encodes the provided byte array using base64 as specified
		in RFC 1341.
		@see Base64
	*/
    public static String encodeBase64(byte[] bytes) {
        return Base64.encode(bytes);
    }

    /**
		encodes the provided bate array into a hexadecimal String.
		Capital letters are used. The String is NOT prefixed with 0x
		or a similar indicator.
	*/
    public static String encodeHex(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i != bytes.length; i++) {
            buf.append(hexChars[(bytes[i] >>> 4) & 0x0f]);
            buf.append(hexChars[(bytes[i] & 0x0f)]);
        }
        return buf.toString();
    }

    public static String encodeHex(byte b) {
        String hex = "";
        hex += hexChars[(b >>> 4) & 0x0f];
        hex += hexChars[(b & 0x0f)];
        return hex;
    }

    /**
		Decode a base64 encoded String into a byte array.
		@see Base64
	*/
    public static byte[] decodeBase64(String base64) throws BException {
        return Base64.decode(base64);
    }

    /**
		Decode a hexencoded String into a byte array.
		@throws BException if the provided String length is not a
		multiple of 2 (2 hex characters make up a byte) or
		invlaid charactes (not a-z,A-Z,0-9) are included in
		the String.
	*/
    public static byte[] decodeHex(String hex) throws BException {
        if (hex.length() % 2 != 0) throw new BException("[BByteUtils.decodeHex] length of provided String not a multiple of 2: " + hex, Globals.ERR_INVALID_ARGUMENT);
        char[] chars = hex.toUpperCase().toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int b = 0;
        for (int i = 0; i < chars.length; i += 2) {
            bytes[b++] = decodeHex(chars, i);
        }
        return bytes;
    }

    private static byte decodeHex(char[] arr, int offSet) throws BException {
        byte b = decodeHex(arr[offSet]);
        b <<= 4;
        b |= decodeHex(arr[offSet + 1]);
        return b;
    }

    private static byte decodeHex(char c) throws BException {
        if (c >= 'A' && c <= 'F') return (byte) ((c - 'A') + 10);
        if (c >= '0' && c <= '9') return (byte) (c - '0');
        throw new BException("[BByteUtils.decodeHex] illegal character: " + c, Globals.ERR_INVALID_ARGUMENT);
    }

    /**
		utility for debugging, creates a String representing
		the pattern of bits set in the provided int value:<br>
		e.g:<br>
		passing:<br>
		<code>4</code> yields <code>00000000 00000000 00000000 00000100</code><br>
		<code>-4</code> yields <code>11111111 11111111 11111111 11111100</code><br>
		
	*/
    public static String showBits(int val) {
        StringBuffer buf = new StringBuffer(32);
        for (int i = 31; i != -1; i--) {
            char c = ((val >>> i) & 1) == 1 ? '1' : '0';
            buf.append(c);
            if ((i) % 8 == 0) buf.append(" ");
        }
        return buf.toString();
    }

    /**
		@see #showBits(int)
	*/
    public static String showBits(char ch) {
        StringBuffer buf = new StringBuffer(16);
        for (int i = 15; i != -1; i--) {
            char c = ((ch >>> i) & 1) == 1 ? '1' : '0';
            buf.append(c);
            if ((i) % 8 == 0) buf.append(" ");
        }
        return buf.toString();
    }

    /**
		@see #showBits(int)
	*/
    public static String showBits(byte b) {
        StringBuffer buf = new StringBuffer(8);
        for (int i = 7; i != -1; i--) {
            char c = ((b >>> i) & 1) == 1 ? '1' : '0';
            buf.append(c);
        }
        return buf.toString();
    }

    public static boolean cmpArray(byte[] arr1, byte[] arr2) {
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i != arr1.length; i++) {
            if (arr1[i] != arr2[i]) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            byte[] bytes = { -127, -20, 0, 20, 12, 60 };
            System.out.println(encodeHex(bytes));
            bytes = decodeHex(encodeHex(bytes));
            for (int i = 0; i != bytes.length; i++) {
                System.out.println(bytes[i]);
            }
        } catch (Throwable t) {
        }
    }
}
