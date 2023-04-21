package controlClasses;

import sun.misc.BASE64Encoder;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordService {

    private static PasswordService instance;

    private PasswordService() {
    }

    public synchronized String encrypt(String plaintext) throws Exception {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(e.getMessage());
        }
        try {
            md.update(plaintext.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e.getMessage());
        }
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }

    public static synchronized PasswordService getInstance() {
        if (instance == null) {
            instance = new PasswordService();
        }
        return instance;
    }

    public static String encryptPassword(String password) {
        String encryptedPassword = "";
        try {
            encryptedPassword = PasswordService.getInstance().encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedPassword;
    }
}
