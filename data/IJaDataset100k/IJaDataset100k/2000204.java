package org.sepp.security;

import iaik.utils.CryptoUtils;
import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sepp.exceptions.SecurityServiceException;

public class SecurityPrimitives {

    public static String RSA_ECB_PKCS1_TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    public static String RSA_ECB_OAEP_TRANSFORMATION = "RSA/ECB/OAEP";

    public static String RSA_ECB_OAEP_SHA256_MGF1_TRANSFORMATION = "RSA/ECB/OAEPWithSHA256AndMGF1Padding";

    public static String AES_CBC_PKCS5_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public static String TRIPLEDES_CBC_PKCS5_TRANSFORMATION = "3DES/CBC/PKCS5Padding";

    public static String RC6_CBC_PKCS5_TRANSFORMATION = "RC6/CBC/PKCS5Padding";

    public static String RC5_CBC_PKCS5_TRANSFORMATION = "RC5/CBC/PKCS5Padding";

    public static String RC2_CBC_PKCS5_TRANSFORMATION = "RC2/CBC/PKCS5Padding";

    public static String HMAC_MD5 = "HMAC/MD5";

    public static String HMAC_SHA1 = "HMAC/SHA-1";

    public static String HMAC_SHA224 = "HMAC/SHA-224";

    public static String HMAC_SHA256 = "HMAC/SHA-256";

    public static String HMAC_SHA384 = "HMAC/SHA-384";

    public static String HMAC_SHA512 = "HMAC/SHA-512";

    public static String HMAC_RIPEMD128 = "HMAC/RIPEMD128";

    public static String HMAC_RIPEMD160 = "HMAC/RIPEMD160";

    public static String MD5 = "MD5";

    public static String SHA = "SHA";

    private Log log;

    private byte[] currentIV;

    public SecurityPrimitives() {
        log = LogFactory.getLog(this.getClass());
    }

    public byte[] createSignature(byte[] data, String algorithm, PrivateKey privateKey) throws SecurityServiceException {
        try {
            Signature signer = Signature.getInstance(algorithm, "IAIK");
            signer.initSign(privateKey);
            signer.update(data);
            byte[] signed = signer.sign();
            log.debug("Signature created successfully but will not be verified because no public key is specified!");
            return signed;
        } catch (Exception e) {
            throw new SecurityServiceException("Couldn't sign data. Reason: " + e.getMessage());
        }
    }

    public byte[] createSignature(byte[] data, String algorithm, PrivateKey privateKey, PublicKey publicKey) throws SecurityServiceException {
        try {
            Signature signer = Signature.getInstance(algorithm, "IAIK");
            signer.initSign(privateKey);
            signer.update(data);
            byte[] signed = signer.sign();
            log.debug("Signature created successfully now it must be verified to be correct!");
            signer.initVerify(publicKey);
            signer.update(data);
            if (signer.verify(signed)) {
                log.debug("Signature also verified correctly therefore the created signature is correct!");
                return signed;
            } else {
                throw new SignatureException("Couldn't verify signed data therefore signing failed!");
            }
        } catch (Exception e) {
            throw new SecurityServiceException("Couldn't sign data. Reason: " + e.getMessage());
        }
    }

    public boolean verifySignature(byte[] data, byte[] signature, String algorithm, PublicKey publicKey) throws SecurityServiceException {
        try {
            Signature verifier = Signature.getInstance(algorithm, "IAIK");
            verifier.initVerify(publicKey);
            verifier.update(data);
            return verifier.verify(signature);
        } catch (Exception e) {
            throw new SecurityServiceException("Couldn't verify signature. Reason: " + e.getMessage());
        }
    }

    /**
	 * Computes a HMAC on the given data and compares if the result is the same.
	 * 
	 * @param algorithm
	 *            The HMAC algorithm to be used, e.g. "HMAC/SHA-1"
	 * @param data
	 *            The data on which the MAC shall be computed
	 * @param key
	 *            The secret key which should be used to create the MAC.
	 * 
	 * @return <code>true</code> if the test succeeds, <code>false</code>
	 *         otherwise
	 */
    public byte[] createHMAC(String algorithm, byte[] data, SecretKey key) throws SecurityServiceException {
        try {
            Mac mac = Mac.getInstance(algorithm, "IAIK");
            mac.init(key);
            byte[] result = mac.doFinal(data);
            return result;
        } catch (Exception e) {
            throw new SecurityServiceException("Couldn't create MAC from provided data. Reason: " + e.getMessage());
        }
    }

    /**
	 * Computes a HMAC on the given data and compares if the result is the same.
	 * 
	 * @param algorithm
	 *            The HMAC algorithm to be used, e.g. "HMAC/SHA-1".
	 * @param data
	 *            The data on which the MAC has been computed.
	 * @param mac
	 *            The MAC of the data.
	 * @param key
	 *            The secret key which should be used to create the MAC.
	 * 
	 * @return <code>true</code> if the test succeeds, <code>false</code>
	 *         otherwise
	 */
    public boolean verifyHMAC(String algorithm, byte[] data, byte[] mac, SecretKey key) throws SecurityServiceException {
        try {
            Mac verifyMAC = Mac.getInstance(algorithm, "IAIK");
            verifyMAC.init(key);
            byte[] result = verifyMAC.doFinal(data);
            return CryptoUtils.equalsBlock(result, mac);
        } catch (Exception nsae) {
            throw new SecurityServiceException("Couldn't create MAC from provided data. Reason: " + nsae.getMessage());
        }
    }

    /**
	 * Returns the initialization vector which has been used in the last
	 * encryption process.
	 * 
	 * @return The initialization vector as byte array.
	 */
    public byte[] getCurrentIV() {
        return currentIV;
    }

    /**
	 * Sets the initialization vector which can then be used for the next
	 * decryption process.
	 * 
	 * @param currentIV
	 *            The initialization vector as byte array.
	 */
    public void setCurrentIV(byte[] currentIV) {
        this.currentIV = currentIV;
    }

    /**
	 * 
	 * @param plainText
	 * @param transformation
	 * @param key
	 * @return
	 * @throws SecurityServiceException
	 */
    public byte[] encrypt(byte[] plainText, String transformation, Key key) throws SecurityServiceException {
        try {
            Cipher cipher = Cipher.getInstance(transformation, "IAIK");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            if (key instanceof SecretKey) currentIV = cipher.getIV();
            return cipher.doFinal(plainText);
        } catch (Exception e) {
            throw new SecurityServiceException("Couldn't encrypt provided data. Reason: " + e.getMessage());
        }
    }

    /**
	 * 
	 * @param cipherText
	 * @param transformation
	 * @param key
	 * @return
	 * @throws SecurityServiceException
	 */
    public byte[] decrypt(byte[] cipherText, String transformation, Key key) throws SecurityServiceException {
        try {
            Cipher cipher = Cipher.getInstance(transformation, "IAIK");
            if (key instanceof SecretKey && currentIV instanceof byte[] && currentIV.length > 0) cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(currentIV)); else cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(cipherText);
        } catch (Exception e) {
            throw new SecurityServiceException("Couldn't decrypt provided data. Reason: " + e.getMessage());
        }
    }

    /**
	 * Creates an {@link SecretKey} using the {@link KeyGenerator} from the IAIK
	 * JCE library. The returned {@link SecretKey} is generated using the
	 * specified algorithm.
	 * 
	 * @param algorithm
	 *            The algorithm which should be used to create the secret key.
	 * @return The newly created {@link SecretKey}.
	 * @throws SecurityServiceException
	 *             Thrown if anything goes wrong.
	 */
    public SecretKey createSecretKey(String algorithm) throws SecurityServiceException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, "IAIK");
            return keyGenerator.generateKey();
        } catch (Exception e) {
            throw new SecurityServiceException("Couldn't create secret key. Reason: " + e.getMessage());
        }
    }

    /**
	 * Generates an hash value from the provided data using the specified hash
	 * algorithm. The returned value is the hash value as byte array with the
	 * size of the used algorithm.
	 * 
	 * @param algorithm
	 *            The hash algorithm to use.
	 * @param data
	 *            The data which should be hashed.
	 * @return The hash value of the data using the specified algorithm.
	 * @throws Exception
	 */
    public byte[] createHash(String algorithm, byte[] data) throws SecurityServiceException {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm, "IAIK");
            return digest.digest(data);
        } catch (Exception e) {
            throw new SecurityServiceException("Couldn't create hash value provided data. Reason: " + e.getMessage());
        }
    }
}
