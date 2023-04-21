package org.hourglassstudios.tempuspre.server.authenticator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {

    public static String EncryptString(String method, String input) {
        MessageDigest md = null;
        byte[] byteHash = null;
        StringBuffer resultString = new StringBuffer();
        if (method.equals("SHA1") || method.equals("MD5")) {
            try {
                md = MessageDigest.getInstance(method);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("NoSuchAlgorithmException caught!");
                return null;
            }
        } else {
            return null;
        }
        md.reset();
        md.update(input.getBytes());
        byteHash = md.digest();
        for (int i = 0; i < byteHash.length; i++) {
            String tmp = Integer.toHexString(0xff & byteHash[i]);
            if (tmp.length() < 2) tmp = "0" + tmp;
            resultString.append(tmp);
        }
        return (resultString.toString());
    }

    public static String EncryptStringSHA1(String input) {
        return EncryptString("SHA1", input);
    }

    public static String EncryptStringMD5(String input) {
        return EncryptString("MD5", input);
    }

    public static String WoltlabBurningBoard3Standard(String pass, String salt) {
        return EncryptStringSHA1(salt + EncryptStringSHA1(salt + EncryptStringSHA1(pass)));
    }
}
