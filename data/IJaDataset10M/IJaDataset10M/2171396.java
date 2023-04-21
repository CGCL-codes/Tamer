package org.kisst.cordys.caas.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;

public class PasswordHasher {

    private static final String SHA1 = "SHA1";

    private static final String MD5 = "MD5";

    private static final String SHA1_prefix = "{" + SHA1 + "}";

    private static final String MD5_prefix = "{" + MD5 + "}";

    public static String encryptPassword(String password) {
        return hashNative(password, SHA1, SHA1_prefix, false);
    }

    public static String hashNative(String password, String algorithm, String prefixKey, boolean useDefaultEncoding) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            if (useDefaultEncoding) {
                digest.update(password.getBytes());
            } else {
                for (char c : password.toCharArray()) {
                    digest.update((byte) (c >> 8));
                    digest.update((byte) c);
                }
            }
            byte[] digestedPassword = digest.digest();
            BASE64Encoder encoder = new BASE64Encoder();
            String encodedDigested = encoder.encode(digestedPassword);
            return prefixKey + encodedDigested;
        } catch (NoSuchAlgorithmException ne) {
            return password;
        }
    }

    public static String hashJopl(String password, String algorithm, String prefixKey, boolean useDefaultEncoding) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            if (useDefaultEncoding) {
                digest.update(password.getBytes());
            } else {
                for (char c : password.toCharArray()) {
                    digest.update((byte) (c >> 8));
                    digest.update((byte) c);
                }
            }
            byte[] digestedPassword = digest.digest();
            BASE64Encoder encoder = new BASE64Encoder();
            String encodedDigestedStr = encoder.encode(digestedPassword);
            return prefixKey + encodedDigestedStr;
        } catch (NoSuchAlgorithmException ne) {
            return password;
        }
    }

    public static void main(String[] args) {
        String encryptionType = "SHA1";
        String password = "cordys";
        String encryptedPasswordNative = null;
        String encryptedPasswordJopl = null;
        if (MD5.equals(encryptionType)) {
            encryptedPasswordNative = hashNative(password, MD5, MD5_prefix, true);
            encryptedPasswordJopl = hashJopl(password, MD5, MD5_prefix, true);
        } else if (SHA1.equals(encryptionType)) {
            encryptedPasswordNative = hashNative(password, SHA1, SHA1_prefix, false);
            encryptedPasswordJopl = hashJopl(password, SHA1, SHA1_prefix, false);
        } else {
            printUsage();
        }
        System.out.println("Native\t:" + password + " -> " + encryptedPasswordNative);
        System.out.println("Jopl  \t:" + password + " -> " + encryptedPasswordJopl);
    }

    private static void printUsage() {
        System.out.println("Usage:\tPasswordHasher encryptionType password\n" + "\tencryptionType can be MD5 or SHA1\n");
    }
}
