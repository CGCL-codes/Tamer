package client.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class provides basic routines for encryption/decryption of string values.
 * @author danon
 */
public class Crypto {

    /**
     * Calculates MD5-hash code of specified string.
     * @param s String to encrypt
     * @return 5d5-hash as a hexadecimal sequence of bytes converted to string.
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String md5(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(s.getBytes("UTF-8"));
        String res = "";
        for (int i = 0; i < bytes.length; i++) {
            int k = 0;
            if (bytes[i] < 0) k = 256 + bytes[i]; else k = bytes[i];
            String t = Integer.toString(k, 16);
            if (t.length() < 2) t = "0" + t;
            res += t;
        }
        return res;
    }
}
