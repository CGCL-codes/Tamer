package com.ehs.auth;

import com.ehs.pm.beans.User;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;

/**
 *
 * @author E15567
 */
public class SecurityManager {

    private static SecurityManager securityManager;

    private User user;

    static {
        securityManager = new SecurityManager();
    }

    public static synchronized SecurityManager getInstance() {
        if (securityManager == null) {
            securityManager = new SecurityManager();
        }
        return securityManager;
    }

    private SecurityManager() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static String encrypt(String plainText) throws Exception {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(e.getMessage());
        }
        try {
            md.update(plainText.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e.getMessage());
        }
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }
}
