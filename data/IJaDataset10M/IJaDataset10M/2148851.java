package org.jopen.aws.s3;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.jopen.aws.IAWSConstants;

/**
 * Implements a cipher for encrypting and decrypting objects stored on S3.  The cipher uses the AES encryption standard, with 128 bit
 * keys, in 8 bit CFB mode (streaming) with no padding.  A user-supplied string is used to generate the key, and the initialization
 * vector is derived from the object's name and length.  
 * 
 * @author Tom Dilatush
 */
public class S3Cipher implements IAWSConstants {

    private static final String CIPHER = "AES";

    private static final String CIPHER_MODE = "CFB8";

    private static final String CIPHER_PADDING = "NoPadding";

    private static final String CIPHER_SPEC = CIPHER + "/" + CIPHER_MODE + "/" + CIPHER_PADDING;

    private static final String HASH_ALGORITHM = "HmacMD5";

    private static final byte[] SALT = { -121, 22, 67, -53, -11, -3, 51, 17, -92, 101, 12, -79, -33, 81, 27 };

    private static final int ROUNDS = 1500;

    private Logger mLogger;

    /**
	 * Creates a new instance of this class.
	 */
    public S3Cipher() {
        mLogger = Logger.getLogger(getClass().getCanonicalName());
    }

    /**
	 * Returns a filter stream that presents an input stream that wraps and encrypts the given input stream.  The given password
	 * is used to construct the encryption key, and the given object's key and length are used to create the initialization vector
	 * for the cipher.
	 * 
	 * @param pObject The <code>S3Object</code> whose key and length are used for the cipher's initialization vector.
	 * @param pPassword The <code>String</code> password that creates the key used for the cipher.
	 * @param pSource The <code>InputStream</code> that is the source of data for the stream encrypted and returned.
	 * @return An <code>InputStream</code> that filters ghe given input stream, encrypting it.
	 * @throws S3Exception on any problem.
	 */
    public InputStream encrypt(S3Object pObject, String pPassword, InputStream pSource) throws S3Exception {
        return codec(pObject, pPassword, pSource, Cipher.ENCRYPT_MODE);
    }

    /**
	 * Returns a filter stream that presents an input stream that wraps and decrypts the given input stream.  The given password
	 * is used to construct the encryption key, and the given object's key and length are used to create the initialization vector
	 * for the cipher.
	 * 
	 * @param pObject The <code>S3Object</code> whose key and length are used for the cipher's initialization vector.
	 * @param pPassword The <code>String</code> password that creates the key used for the cipher.
	 * @param pSource The <code>InputStream</code> that is the source of data for the stream decrypted and returned.
	 * @return An <code>InputStream</code> that filters ghe given input stream, decrypting it.
	 * @throws S3Exception on any problem.
	 */
    public InputStream decrypt(S3Object pObject, String pPassword, InputStream pSource) throws S3Exception {
        return codec(pObject, pPassword, pSource, Cipher.DECRYPT_MODE);
    }

    /**
	 * Returns a filter stream that presents an input stream that wraps and decrypts or encrypts the given input stream.  The given password
	 * is used to construct the encryption key, and the given object's key and length are used to create the initialization vector
	 * for the cipher.
	 * 
	 * @param pObject The <code>S3Object</code> whose key and length are used for the cipher's initialization vector.
	 * @param pPassword The <code>String</code> password that creates the key used for the cipher.
	 * @param pSource The <code>InputStream</code> that is the source of data for the stream decrypted or encrypted and returned.
	 * @param pMode The <code>int</code> mode (either <code>Cipher.ENCRYPT_MODE</code> or <code>Cipher.DECRYPT_MODE</code>.
	 * @return An <code>InputStream</code> that filters ghe given input stream, decrypting or encrypting it.
	 * @throws S3Exception on any problem.
	 */
    private InputStream codec(S3Object pObject, String pPassword, InputStream pSource, int pMode) throws S3Exception {
        InputStream result = null;
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_SPEC);
            IvParameterSpec ivSpec = getIV(pObject);
            SecretKey key = makeKey(pPassword);
            cipher.init(pMode, key, ivSpec);
            result = new CipherInputStream(pSource, cipher);
        } catch (S3Exception e) {
            throw e;
        } catch (Exception e) {
            String report = "Problem encrypting! " + e.getMessage();
            mLogger.log(Level.SEVERE, report, e);
            throw new S3Exception(report, e);
        }
        return result;
    }

    /**
	 * Returns an initialization vector created from the given object's key and length.
	 * 
	 * @param pObject The <code>S3Object</code> whose key and length make up the initialization vector.
	 * @return The <code>IvParameterSpec</code> created.
	 * @throws S3Exception on any problem.
	 */
    private IvParameterSpec getIV(S3Object pObject) throws S3Exception {
        if (pObject == null) throw new IllegalArgumentException("Parameter pObject may not be null!");
        IvParameterSpec result = null;
        try {
            byte[] pwb = (pObject.getKey() + Long.toHexString(pObject.getLength())).getBytes(BODY_ENCODING);
            SecretKeySpec signingKey = new SecretKeySpec(SALT, HASH_ALGORITHM);
            Mac mac = Mac.getInstance(HASH_ALGORITHM);
            mac.init(signingKey);
            mac.update(pwb);
            byte[] key = mac.doFinal();
            result = new IvParameterSpec(key);
        } catch (Exception e) {
            String report = "Problem making key! " + e.getMessage();
            mLogger.log(Level.SEVERE, report, e);
            throw new S3Exception(report, e);
        }
        return result;
    }

    /**
	 * Make our encryption key from the given password.
	 * 
	 * @param pPassword The <code>String</code> password to make a key from.
	 * @return The <code>SecretKey</code> key.
	 * @throws S3Exception on any problem...
	 */
    private SecretKey makeKey(String pPassword) throws S3Exception {
        if (pPassword == null) throw new IllegalArgumentException("Parameter pPassword may not be null!");
        SecretKey result = null;
        try {
            Mac mac = Mac.getInstance(HASH_ALGORITHM);
            SecretKeySpec salt = new SecretKeySpec(SALT, HASH_ALGORITHM);
            mac.init(salt);
            byte[] pwb = pPassword.getBytes(BODY_ENCODING);
            for (int i = 0; i < ROUNDS; i++) mac.update(pwb);
            byte[] key = mac.doFinal();
            result = new SecretKeySpec(key, CIPHER);
        } catch (Exception e) {
            String report = "Problem making key! " + e.getMessage();
            mLogger.log(Level.SEVERE, report, e);
            throw new S3Exception(report, e);
        }
        return result;
    }
}
