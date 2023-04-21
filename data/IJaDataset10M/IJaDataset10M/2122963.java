package net.dadajax.model;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.apache.log4j.Logger;

/**
 * @author dadajax
 *
 */
public class SimpleCrypter implements Crypter {

    Cipher ecipher;

    Cipher dcipher;

    private static final String psw = "jkhagASDGhsahAHaydsGF";

    public SimpleCrypter() {
        byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03 };
        int iterationCount = 19;
        try {
            KeySpec keySpec = new PBEKeySpec(psw.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (Exception e) {
            Logger.getRootLogger().error("error during intitialization crypter", e);
        }
    }

    @Override
    public String decryptString(String string) {
        try {
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(string);
            byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, "UTF8");
        } catch (Exception e) {
            Logger.getRootLogger().error("String decrypting failed", e);
        }
        return null;
    }

    @Override
    public String encryptString(String string) {
        try {
            byte[] utf8 = string.getBytes("UTF-8");
            byte[] enc = ecipher.doFinal(utf8);
            return new sun.misc.BASE64Encoder().encode(enc);
        } catch (Exception e) {
            Logger.getRootLogger().error("String encrypting failed", e);
        }
        return null;
    }
}
