package com.ballroomregistrar.compinabox.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1TokenGenerator implements TokenGenerator {

    public String generateToken(String code) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(code.getBytes());
            byte[] bytes = md.digest();
            return toHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA1 missing");
        }
    }

    private String toHex(final byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}
