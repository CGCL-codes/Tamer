package za.co.modobo.libs;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * This class has some basic crypto and encoding functions
 *
 * @author Nico Coetzee <nicc777@gmail.com>
 */
public class cryptoMagic {

    public cryptoMagic() {
    }

    /**
     * With this method you can encrypt/decrypt any string. For encryption requests, a BASE64 encoded string is returned
     * <p>
     * Reference: http://www.java2s.com/Tutorial/Java/0490__Security/EncryptingwithDESUsingaPassPhrase.htm
     * @param secretKey The secret key used by Blowfish
     * @param data String of data
     * @param action What to do: encrypt (default) or decrypt
     * @param logger A copy of the log4j object to write to syslog
     * @return A string
     */
    public String DES(String secretKey, String data, String action, syslogLogger logger) {
        byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };
        int iterationCount = 2;
        KeySpec keySpec = null;
        SecretKey key = null;
        try {
            keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
            key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        } catch (Exception e) {
            logger.log(this.getClass().toString() + "::DES() Exception", "error");
            logger.log(e.getMessage(), "error");
        }
        if (action.compareTo("decrypt") != 0) {
            action = "encrypt";
        }
        logger.log(this.getClass().toString() + "::DES() action='" + action + "'", "info");
        if (action.compareTo("encrypt") == 0) {
            Cipher ecipher;
            try {
                ecipher = Cipher.getInstance(key.getAlgorithm());
                AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
                ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
                return new BASE64Encoder().encode(ecipher.doFinal(data.getBytes()));
            } catch (Exception e) {
                logger.log(this.getClass().toString() + "::DES() Exception", "error");
                logger.log(e.getMessage(), "error");
            }
        } else {
            Cipher dcipher;
            try {
                dcipher = Cipher.getInstance(key.getAlgorithm());
                AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
                dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
                return new String(dcipher.doFinal(new BASE64Decoder().decodeBuffer(data)));
            } catch (Exception e) {
                logger.log(this.getClass().toString() + "::DES() Exception", "error");
                logger.log(e.getMessage(), "error");
            }
        }
        return "";
    }
}
