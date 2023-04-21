package jvc.util.security;

import javax.crypto.*;
import javax.crypto.spec.*;

public class Des {

    private static final String TRANSFORMATION = "DES";

    public static byte[] encrypt(byte[] src, byte[] key) {
        if (src == null || key == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, TRANSFORMATION));
            return cipher.doFinal(src);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] decrypt(byte[] src, byte[] key) {
        if (src == null || key == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, TRANSFORMATION));
            return cipher.doFinal(src);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] hex2Byte(String str) {
        if (str == null || str.length() == 0) return null;
        byte[] tmp = new byte[8];
        int len = (str.length() > 16 ? 16 : str.length()) / 2;
        for (int i = 0; i < len; i++) {
            try {
                tmp[i] = (byte) (Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16) & 0XFF);
            } catch (Exception e) {
            }
        }
        return tmp;
    }

    public static String byte2Hex(byte b[]) {
        return byte2Hex(b, 8);
    }

    public static String byte2Hex(byte b[], int limitLen) {
        if (b == null) return "";
        StringBuffer tmp = new StringBuffer();
        int len = b.length > limitLen ? limitLen : b.length;
        for (int i = 0; i < len; i++) {
            String s = Integer.toHexString(b[i] & 0XFF);
            if (s.length() < 2) tmp.append('0');
            tmp.append(s);
        }
        while (tmp.length() < limitLen * 2) tmp.append("00");
        return tmp.substring(0, limitLen * 2).toUpperCase();
    }

    private static byte[] hexBase = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static byte[] intToByte(int value) {
        byte buff[] = new byte[8];
        int pos = 8;
        while (value != 0) {
            buff[--pos] = hexBase[value & 0xF];
            value >>>= 4;
        }
        while (pos > 0) {
            buff[--pos] = hexBase[0];
        }
        return buff;
    }

    public static String intToString(int value) {
        return new String(Des.intToByte(value));
    }

    public static void main(String arg[]) {
        String des = Des.byte2Hex(Des.encrypt("123".getBytes(), "12345678".getBytes()));
        System.out.println(des);
        System.out.println(new String(Des.decrypt(Des.hex2Byte(des), "12345678".getBytes())));
    }
}
