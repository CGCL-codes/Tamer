package com.chimshaw.jblogeditor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import junit.framework.TestCase;

/**
 * @author lshah
 *
 */
public class TestEncryption extends TestCase {

    @SuppressWarnings("unused")
    private String user;

    @SuppressWarnings("unused")
    private String pwd;

    @Override
    protected void setUp() throws Exception {
        user = "Lokesh";
        pwd = "mypwd";
        super.setUp();
    }

    public void testEncryption() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        SecretKey key = keyGen.generateKey();
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] clearPswd = "Password".getBytes();
        byte[] encryptedPswd = cipher.doFinal(clearPswd);
        System.out.println(new String(encryptedPswd));
        Cipher dcipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        dcipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedByte = dcipher.doFinal(encryptedPswd);
        System.out.println(new String(decryptedByte));
    }
}
