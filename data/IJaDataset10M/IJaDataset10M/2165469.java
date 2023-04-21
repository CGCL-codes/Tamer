package crypto.password;

import crypto.password.ByteConversion;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    public static String generateMD5(String Passphrase) {
        try {
            return generateMD5(Passphrase.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String generateMD5(byte[] HashBytes) {
        MessageDigest MDigest = null;
        try {
            MDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        MDigest.update(HashBytes);
        return ByteConversion.convertToHex(MDigest.digest());
    }

    public static String generateSHA256(String Passphrase) {
        try {
            return generateSHA256(Passphrase.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String generateSHA256(byte[] HashBytes) {
        MessageDigest MDigest = null;
        try {
            MDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        MDigest.update(HashBytes);
        return ByteConversion.convertToHex(MDigest.digest());
    }

    public static String generateSHA512(String Passphrase) {
        try {
            return generateSHA512(Passphrase.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String generateSHA512(byte[] HashBytes) {
        MessageDigest MDigest = null;
        try {
            MDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        MDigest.update(HashBytes);
        return ByteConversion.convertToHex(MDigest.digest());
    }
}
