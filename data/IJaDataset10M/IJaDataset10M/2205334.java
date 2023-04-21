package src.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Checksum {

    public static String get(String string) {
        return get(string, "MD5");
    }

    public static String get(String string, String algoString) {
        return get(string.getBytes(), algoString);
    }

    public static String get(char[] stringChar, String algoString) {
        if (stringChar == null) {
            return null;
        }
        byte[] stringByte = new byte[stringChar.length];
        for (int idx = 0; idx < stringChar.length; idx++) {
            stringByte[idx] = (byte) stringChar[idx];
        }
        return get(stringByte, algoString);
    }

    public static String get(byte[] stringBytes, String algoString) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance(algoString);
            algorithm.reset();
            algorithm.update(stringBytes);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
        }
        return null;
    }
}
