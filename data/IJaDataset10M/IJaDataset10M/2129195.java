package com.webstate.utils.service;

import java.security.MessageDigest;
import java.util.logging.Logger;
import sun.misc.BASE64Encoder;

public class EncryptPasswordService {

    private static EncryptPasswordService encryptPasswordService = null;

    private EncryptPasswordService() {
    }

    /***************************************************************************
	 * 
	 * @param passwordString
	 * @return
	 * @throws Exception
	 */
    public synchronized String encryptPassword(String passwordString) throws Exception {
        MessageDigest digest = null;
        digest = MessageDigest.getInstance("SHA");
        digest.update(passwordString.getBytes("UTF-8"));
        byte raw[] = digest.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }

    /***************************************************************************
	 * 
	 * @return
	 */
    public static synchronized EncryptPasswordService getInstance() {
        if (encryptPasswordService == null) {
            return new EncryptPasswordService();
        } else {
            return encryptPasswordService;
        }
    }

    /***************************************************************************
	 * THIS IS ONLY A METHOD TO EXAMINE PASSWORD ENCRYPTION
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            Logger.getAnonymousLogger().info(getInstance().encryptPassword("1234"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
