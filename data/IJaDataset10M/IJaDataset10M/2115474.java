package org.rapla.plugin.exchangeconnector.datastorage;

import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class is used for en- and decrypting the password of a user.<br>
 * <b>Note: </b>The String in <i>PEPPER</i> should be changed to a random string before building
 * the application for a productive environment!
 * 
 * @author lutz
 *
 */
public class CryptoHandler {

    private static final String ENCODING = "ISO-8859-1";

    private static final String PEPPER = new String("ru0938iuew8rsinep094834");

    /**
	 * This method encrypts a passed {@link String}
	 * 
	 * @param toBeEncrypted : {@link String} which needs to be encrypted
	 * @param additive : {@link String} which is permanently associated with toBeEncrypted
	 * @return {@link String}
	 * @throws Exception
	 */
    public static String encrypt(String toBeEncrypted, String additive) throws Exception {
        return crypt(toBeEncrypted, additive, Cipher.ENCRYPT_MODE);
    }

    /**
	 * This method decrypts a passed {@link String}
	 * 
	 * @param toDeEncrypted : {@link String} which needs to be decrypted
	 * @param additive : {@link String} which is permanently associated with toDeEncrypted
	 * @return {@link String}
	 * @throws Exception
	 */
    public static String decrypt(String toBeDecrypted, String additive) throws Exception {
        return crypt(toBeDecrypted, additive, Cipher.DECRYPT_MODE);
    }

    /**
	 * This method contains the en- and decryption logic - it uses <b>AES-128Bit</b>
	 * 
	 * @param toBeEncrypted : {@link String}
	 * @param additive : {@link String}
	 * @param cryptMode : {@link Integer}
	 * @return {@link String}
	 * @throws Exception
	 */
    private static String crypt(String toBeEncrypted, String additive, int cryptMode) throws Exception {
        String returnVal = null;
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = PEPPER.getBytes(ENCODING);
        byte[] additiveBytes = additive.getBytes(ENCODING);
        key = sha.digest(key);
        additiveBytes = sha.digest(additiveBytes);
        byte[] sessionKey = Arrays.copyOf(key, 16);
        byte[] iv = Arrays.copyOf(additiveBytes, 16);
        byte[] plaintext = toBeEncrypted.getBytes(ENCODING);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(cryptMode, new SecretKeySpec(sessionKey, "AES"), new IvParameterSpec(iv));
        byte[] resultBytes = cipher.doFinal(plaintext);
        returnVal = new String(resultBytes, ENCODING);
        return returnVal;
    }
}
