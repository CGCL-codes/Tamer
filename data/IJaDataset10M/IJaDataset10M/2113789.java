package com.jawise.serviceadapter.sec;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

public class LicenseValidator {

    private static Logger logger = Logger.getLogger(LicenseValidator.class);

    private Cipher encryptCipher;

    private MessageDigest md5;

    private MessageDigest sh1;

    private Cipher decryptCipher;

    public LicenseValidator() throws Exception {
        try {
            encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            md5 = MessageDigest.getInstance("MD5");
            sh1 = MessageDigest.getInstance("SHA1");
            encryptCipher.init(Cipher.ENCRYPT_MODE, getLocalSecretKey());
            decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, getLocalSecretKey());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    private synchronized SecretKeySpec getLocalSecretKey() throws InvalidKeySpecException, UnsupportedEncodingException {
        String ck = new String(md5.digest("$b1ame$is$n0game$".getBytes("ISO-8859-1")), "ISO-8859-1") + new String(sh1.digest("$b1ame$is$n0game$".getBytes("ISO-8859-1")), "ISO-8859-1");
        byte[] key = new byte[16];
        System.arraycopy(ck.getBytes("ISO-8859-1"), 0, key, 0, 16);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        return skeySpec;
    }

    private String decrypt(String ciphertext) throws Exception {
        try {
            BinaryDecoder decoder = new Hex();
            byte[] cleartext = null;
            synchronized (decryptCipher) {
                byte[] decoded = decoder.decode(ciphertext.getBytes("ISO-8859-1"));
                cleartext = decryptCipher.doFinal(decoded);
            }
            return new String(cleartext, "ISO-8859-1");
        } catch (Exception ex) {
            throw ex;
        }
    }

    public boolean isValid(String user, String etc, String licekey) throws Exception {
        String data = decrypt(licekey);
        if (user != null && user.length() >= 8 && etc != null & etc.length() >= 6) {
            if (data.indexOf(user) >= 0 && data.indexOf(etc) >= 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
