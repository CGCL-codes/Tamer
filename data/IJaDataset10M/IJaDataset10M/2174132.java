package com.ekeymanlib.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class KeyUtils {

    public static byte[] generateKey(int keyBytesLength) {
        return generateBytes(keyBytesLength);
    }

    public static String generateAppDeviceKey() {
        UUID vendorKey = UUID.randomUUID();
        return vendorKey.toString();
    }

    public static byte[] generateSalt(int saltLength) {
        return generateBytes(saltLength);
    }

    public static byte[] generateIv(int ivLength) {
        return generateBytes(ivLength);
    }

    private static byte[] generateBytes(int bytesLength) {
        byte[] bytes;
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            bytes = new byte[bytesLength];
            random.nextBytes(bytes);
        } catch (NoSuchAlgorithmException x) {
            bytes = null;
        }
        return bytes;
    }

    public static int generateIterationCount(int maxCount) {
        int i = 0;
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            i = random.nextInt(maxCount);
        } catch (NoSuchAlgorithmException x) {
            i = 0;
        }
        return i;
    }
}
