package org.managersheet.util.api;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;

public class Crypt {

    public static String md5Encrypt(String valueToEncrypted) {
        String encryptedValue = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(valueToEncrypted.getBytes());
            BigInteger hash = new BigInteger(1, digest.digest());
            encryptedValue = hash.toString(16);
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return encryptedValue;
    }

    public static String generateUniqueAuthorizationKey(String key) {
        BASE64Encoder encoder = new BASE64Encoder();
        String authKey = Long.toString(System.currentTimeMillis());
        authKey += key;
        authKey = encoder.encode(authKey.toString().getBytes());
        return authKey;
    }
}
