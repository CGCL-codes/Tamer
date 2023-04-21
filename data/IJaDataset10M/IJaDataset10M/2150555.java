package com.gwtaf.security.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.gwtaf.security.shared.IHashAlgorithm;

public class MD5 implements IHashAlgorithm {

    private MessageDigest md;

    public static MD5 get() throws NoSuchAlgorithmException {
        return new MD5();
    }

    public MD5() throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance("MD5");
    }

    public String calcHash(String value) {
        md.reset();
        md.update(value.getBytes());
        byte[] digest = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
