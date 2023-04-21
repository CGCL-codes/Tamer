package com.blogspot.qbeukes.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author quintin
 */
public class DataUtil {

    /**
   * Convert a hex string of the form {HH}{HH}{HH} to a byte array. Each 2
   * consecutive characters are used to build the array, so the length of the array
   * will equals half the length of the string.
   *
   * @param hexString
   * @return Byte array
   */
    public static byte[] hexStringToByteArray(String hexString) {
        Pattern p = Pattern.compile("([0-9a-f]{2})+", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(hexString);
        if (!m.matches()) {
            if (hexString.length() % 2 != 0) {
                throw new IllegalArgumentException("The hex string has an uneven length.");
            } else {
                throw new IllegalArgumentException("The hex string contains invalid characters.");
            }
        }
        StringReader in = new StringReader(hexString);
        char[] buf = new char[2];
        byte[] byteArr = new byte[hexString.length() / 2];
        int idx = 0;
        try {
            while (in.read(buf) > 0) {
                String s = new String(buf);
                byteArr[idx++] = (byte) Integer.parseInt(s, 16);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse hex string.", e);
        }
        return byteArr;
    }

    /**
   * Convert an array of bytes to a hex string. This function doesn't append the
   * 0x in front of every byte.
   * @param bytes
   * @return String of bytes as their hex values, comma separated.
   */
    public static String getByteHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder("0x");
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(getByteHex(bytes[i]));
        }
        return sb.toString();
    }

    /**
   * Convert a byte to 2 character hex (0 padded)
   *
   * @param b
   * @return Hex String
   */
    public static String getByteHex(byte b) {
        String s = Integer.toHexString(b & 0xff).toUpperCase();
        if ((b & 0xff) < 0xf) {
            return "0" + s;
        } else {
            return s;
        }
    }

    /**
   * Convert a byte array to a MAC address
   *
   * @param byteMac
   * @return String MAC
   */
    public static String stringMac(byte[] byteMac) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteMac.length; ++i) {
            if (i > 0) {
                sb.append(':');
            }
            sb.append(getByteHex(byteMac[i]));
        }
        return sb.toString();
    }

    /**
   * Convert a String MAC address to a byte array mac. Each of the string MAC's
   * byte components have to be 2 character lengths, meaning they have to
   * each be padded with a 0 if necessary. This means the total length of the string
   * has to be 17 characters.
   *
   * @param stringMac
   * @return Byte array MAC address
   */
    public static byte[] byteMac(String stringMac) {
        if (stringMac.length() != 17) {
            throw new IllegalArgumentException("Invalid string mac address.");
        }
        return hexStringToByteArray(stringMac.replaceAll(":", ""));
    }
}
