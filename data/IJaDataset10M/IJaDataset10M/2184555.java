package org.owasp.esapi.reference.crypto;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.EncoderConstants;
import org.owasp.esapi.Encryptor;
import org.owasp.esapi.Logger;
import org.owasp.esapi.codecs.Hex;
import org.owasp.esapi.crypto.CipherSpec;
import org.owasp.esapi.crypto.CipherText;
import org.owasp.esapi.crypto.CryptoHelper;
import org.owasp.esapi.crypto.KeyDerivationFunction;
import org.owasp.esapi.crypto.PlainText;
import org.owasp.esapi.crypto.SecurityProviderLoader;
import org.owasp.esapi.errors.ConfigurationException;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.errors.IntegrityException;
import org.owasp.esapi.reference.DefaultSecurityConfiguration;

/**
 * Reference implementation of the {@code Encryptor} interface. This implementation
 * layers on the JCE provided cryptographic package. Algorithms used are
 * configurable in the {@code ESAPI.properties} file. The main property
 * controlling the selection of this class is {@code ESAPI.Encryptor}. Most of
 * the other encryption related properties have property names that start with
 * the string "Encryptor.".
 * 
 * @author Jeff Williams (jeff.williams .at. aspectsecurity.com) <a
 *         href="http://www.aspectsecurity.com">Aspect Security</a>
 * @author kevin.w.wall@gmail.com
 * @author Chris Schmidt (chrisisbeef .at. gmail.com)
 * @since June 1, 2007; some methods since ESAPI Java 2.0
 * @see org.owasp.esapi.Encryptor
 */
public final class JavaEncryptor implements Encryptor {

    private static volatile Encryptor singletonInstance;

    public static Encryptor getInstance() throws EncryptionException {
        if (singletonInstance == null) {
            synchronized (JavaEncryptor.class) {
                if (singletonInstance == null) {
                    singletonInstance = new JavaEncryptor();
                }
            }
        }
        return singletonInstance;
    }

    private static boolean initialized = false;

    private static SecretKeySpec secretKeySpec = null;

    private static String encryptAlgorithm = "AES";

    private static String encoding = "UTF-8";

    private static int encryptionKeyLength = 128;

    private static PrivateKey privateKey = null;

    private static PublicKey publicKey = null;

    private static String signatureAlgorithm = "SHA1withDSA";

    private static String randomAlgorithm = "SHA1PRNG";

    private static int signatureKeyLength = 1024;

    private static String hashAlgorithm = "SHA-512";

    private static int hashIterations = 1024;

    private static Logger logger = ESAPI.getLogger("JavaEncryptor");

    private static int encryptCounter = 0;

    private static int decryptCounter = 0;

    private static final int logEveryNthUse = 25;

    private static final String DECRYPTION_FAILED = "Decryption failed; see logs for details.";

    private static int N_SECS = 2;

    static {
        try {
            SecurityProviderLoader.loadESAPIPreferredJCEProvider();
        } catch (NoSuchProviderException ex) {
            logger.fatal(Logger.SECURITY_FAILURE, "JavaEncryptor failed to load preferred JCE provider.", ex);
            throw new ExceptionInInitializerError(ex);
        }
        setupAlgorithms();
    }

    /**
     * Generates a new strongly random secret key and salt that can be
     * copy and pasted in the <b>ESAPI.properties</b> file.
     * 
     * @param args Set first argument to "-print" to display available algorithms on standard output.
     * @throws java.lang.Exception	To cover a multitude of sins, mostly in configuring ESAPI.properties.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Generating a new secret master key");
        if (args.length == 1 && args[0].equalsIgnoreCase("-print")) {
            System.out.println("AVAILABLE ALGORITHMS");
            Provider[] providers = Security.getProviders();
            TreeMap<String, String> tm = new TreeMap<String, String>();
            for (int i = 0; i != providers.length; i++) {
                System.out.println("===== Provider " + i + ":" + providers[i].getName() + " ======");
                Iterator<Object> it = providers[i].keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String value = providers[i].getProperty(key);
                    tm.put(key, value);
                    System.out.println("\t\t   " + key + " -> " + value);
                }
            }
            Set<Entry<String, String>> keyValueSet = tm.entrySet();
            Iterator<Entry<String, String>> it = keyValueSet.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("   " + key + " -> " + value);
            }
        } else {
            System.out.println("\tuse '-print' to also show available crypto algorithms from all the security providers");
        }
        encryptAlgorithm = ESAPI.securityConfiguration().getEncryptionAlgorithm();
        encryptionKeyLength = ESAPI.securityConfiguration().getEncryptionKeyLength();
        randomAlgorithm = ESAPI.securityConfiguration().getRandomAlgorithm();
        SecureRandom random = SecureRandom.getInstance(randomAlgorithm);
        SecretKey secretKey = CryptoHelper.generateSecretKey(encryptAlgorithm, encryptionKeyLength);
        byte[] raw = secretKey.getEncoded();
        byte[] salt = new byte[20];
        random.nextBytes(salt);
        String eol = System.getProperty("line.separator", "\n");
        System.out.println(eol + "Copy and paste these lines into your ESAPI.properties" + eol);
        System.out.println("#==============================================================");
        System.out.println("Encryptor.MasterKey=" + ESAPI.encoder().encodeForBase64(raw, false));
        System.out.println("Encryptor.MasterSalt=" + ESAPI.encoder().encodeForBase64(salt, false));
        System.out.println("#==============================================================" + eol);
    }

    /**
     * Private CTOR for {@code JavaEncryptor}, called by {@code getInstance()}.
     * @throws EncryptionException if can't construct this object for some reason.
     * 					Original exception will be attached as the 'cause'.
     */
    private JavaEncryptor() throws EncryptionException {
        byte[] salt = ESAPI.securityConfiguration().getMasterSalt();
        byte[] skey = ESAPI.securityConfiguration().getMasterKey();
        assert salt != null : "Can't obtain master salt, Encryptor.MasterSalt";
        assert salt.length >= 16 : "Encryptor.MasterSalt must be at least 16 bytes. " + "Length is: " + salt.length + " bytes.";
        assert skey != null : "Can't obtain master key, Encryptor.MasterKey";
        assert skey.length >= 7 : "Encryptor.MasterKey must be at least 7 bytes. " + "Length is: " + skey.length + " bytes.";
        synchronized (JavaEncryptor.class) {
            if (!initialized) {
                secretKeySpec = new SecretKeySpec(skey, encryptAlgorithm);
                try {
                    SecureRandom prng = SecureRandom.getInstance(randomAlgorithm);
                    byte[] seed = hash(new String(skey, encoding), new String(salt, encoding)).getBytes(encoding);
                    prng.setSeed(seed);
                    initKeyPair(prng);
                } catch (Exception e) {
                    throw new EncryptionException("Encryption failure", "Error creating Encryptor", e);
                }
                initialized = true;
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
	 * Hashes the data with the supplied salt and the number of iterations specified in
	 * the ESAPI SecurityConfiguration.
	 */
    public String hash(String plaintext, String salt) throws EncryptionException {
        return hash(plaintext, salt, hashIterations);
    }

    /**
     * {@inheritDoc}
     * 
	 * Hashes the data using the specified algorithm and the Java MessageDigest class. This method
	 * first adds the salt, a separator (":"), and the data, and then rehashes the specified number of iterations
	 * in order to help strengthen weak passwords.
	 */
    public String hash(String plaintext, String salt, int iterations) throws EncryptionException {
        byte[] bytes = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(hashAlgorithm);
            digest.reset();
            digest.update(ESAPI.securityConfiguration().getMasterSalt());
            digest.update(salt.getBytes(encoding));
            digest.update(plaintext.getBytes(encoding));
            bytes = digest.digest();
            for (int i = 0; i < iterations; i++) {
                digest.reset();
                bytes = digest.digest(bytes);
            }
            String encoded = ESAPI.encoder().encodeForBase64(bytes, false);
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("Internal error", "Can't find hash algorithm " + hashAlgorithm, e);
        } catch (UnsupportedEncodingException ex) {
            throw new EncryptionException("Internal error", "Can't find encoding for " + encoding, ex);
        }
    }

    /**
	 * Convenience method that encrypts plaintext strings the new way (default
	 * is CBC mode and PKCS5 padding). This encryption method uses the master
	 * encryption key specified by the {@code Encryptor.MasterKey} property
	 * in {@code ESAPI.properties}.
	 * 
	 * @param plaintext	A String to be encrypted
	 * @return	A base64-encoded combination of IV + raw ciphertext
	 * @throws EncryptionException	Thrown when something goes wrong with the
	 * 								encryption.
	 * 
	 * @see org.owasp.esapi.Encryptor#encrypt(PlainText)
	 */
    @Deprecated
    public String encrypt(String plaintext) throws EncryptionException {
        logWarning("encrypt", "Calling deprecated encrypt() method.");
        CipherText ct = null;
        ct = encrypt(new PlainText(plaintext));
        return ct.getEncodedIVCipherText();
    }

    /**
	* {@inheritDoc}
	*/
    public CipherText encrypt(PlainText plaintext) throws EncryptionException {
        return encrypt(secretKeySpec, plaintext);
    }

    /**
	  * {@inheritDoc}
	  */
    public CipherText encrypt(SecretKey key, PlainText plain) throws EncryptionException {
        byte[] plaintext = plain.asBytes();
        boolean overwritePlaintext = ESAPI.securityConfiguration().overwritePlainText();
        assert key != null : "(Master) encryption key may not be null";
        boolean success = false;
        String xform = null;
        int keySize = key.getEncoded().length * 8;
        try {
            xform = ESAPI.securityConfiguration().getCipherTransformation();
            String[] parts = xform.split("/");
            assert parts.length == 3 : "Malformed cipher transformation: " + xform;
            String cipherMode = parts[1];
            if (!CryptoHelper.isAllowedCipherMode(cipherMode)) {
                throw new EncryptionException("Encryption failure: invalid cipher mode ( " + cipherMode + ") for encryption", "Encryption failure: Cipher transformation " + xform + " specifies invalid " + "cipher mode " + cipherMode);
            }
            Cipher encrypter = Cipher.getInstance(xform);
            String cipherAlg = encrypter.getAlgorithm();
            int keyLen = ESAPI.securityConfiguration().getEncryptionKeyLength();
            if (keySize != keyLen) {
                logger.warning(Logger.SECURITY_FAILURE, "Encryption key length mismatch. ESAPI.EncryptionKeyLength is " + keyLen + " bits, but length of actual encryption key is " + keySize + " bits.  Did you remember to regenerate your master key (if that is what you are using)???");
            }
            if (keySize < keyLen) {
                logger.warning(Logger.SECURITY_FAILURE, "Actual key size of " + keySize + " bits SMALLER THAN specified " + "encryption key length (ESAPI.EncryptionKeyLength) of " + keyLen + " bits with cipher algorithm " + cipherAlg);
            }
            if (keySize < 112) {
                logger.warning(Logger.SECURITY_FAILURE, "Potentially unsecure encryption. Key size of " + keySize + "bits " + "not sufficiently long for " + cipherAlg + ". Should use appropriate algorithm with key size " + "of *at least* 112 bits except when required by legacy apps. See NIST Special Pub 800-57.");
            }
            String skeyAlg = key.getAlgorithm();
            if (!(cipherAlg.startsWith(skeyAlg + "/") || cipherAlg.equals(skeyAlg))) {
                logger.warning(Logger.SECURITY_FAILURE, "Encryption mismatch between cipher algorithm (" + cipherAlg + ") and SecretKey algorithm (" + skeyAlg + "). Cipher will use algorithm " + cipherAlg);
            }
            byte[] ivBytes = null;
            CipherSpec cipherSpec = new CipherSpec(encrypter, keySize);
            boolean preferredCipherMode = CryptoHelper.isCombinedCipherMode(cipherMode);
            SecretKey encKey = null;
            if (preferredCipherMode) {
                encKey = key;
            } else {
                encKey = computeDerivedKey(KeyDerivationFunction.kdfVersion, getDefaultPRF(), key, keySize, "encryption");
            }
            if (cipherSpec.requiresIV()) {
                String ivType = ESAPI.securityConfiguration().getIVType();
                IvParameterSpec ivSpec = null;
                if (ivType.equalsIgnoreCase("random")) {
                    ivBytes = ESAPI.randomizer().getRandomBytes(encrypter.getBlockSize());
                } else if (ivType.equalsIgnoreCase("fixed")) {
                    String fixedIVAsHex = ESAPI.securityConfiguration().getFixedIV();
                    ivBytes = Hex.decode(fixedIVAsHex);
                } else {
                    throw new ConfigurationException("Property Encryptor.ChooseIVMethod must be set to 'random' or 'fixed'");
                }
                ivSpec = new IvParameterSpec(ivBytes);
                cipherSpec.setIV(ivBytes);
                encrypter.init(Cipher.ENCRYPT_MODE, encKey, ivSpec);
            } else {
                encrypter.init(Cipher.ENCRYPT_MODE, encKey);
            }
            logger.debug(Logger.EVENT_SUCCESS, "Encrypting with " + cipherSpec);
            byte[] raw = encrypter.doFinal(plaintext);
            CipherText ciphertext = new CipherText(cipherSpec, raw);
            if (!preferredCipherMode) {
                SecretKey authKey = computeDerivedKey(KeyDerivationFunction.kdfVersion, getDefaultPRF(), key, keySize, "authenticity");
                ciphertext.computeAndStoreMAC(authKey);
            }
            logger.debug(Logger.EVENT_SUCCESS, "JavaEncryptor.encrypt(SecretKey,byte[],boolean,boolean) -- success!");
            success = true;
            return ciphertext;
        } catch (InvalidKeyException ike) {
            throw new EncryptionException("Encryption failure: Invalid key exception.", "Requested key size: " + keySize + "bits greater than 128 bits. Must install unlimited strength crypto extension from Sun: " + ike.getMessage(), ike);
        } catch (ConfigurationException cex) {
            throw new EncryptionException("Encryption failure: Configuration error. Details in log.", "Key size mismatch or unsupported IV method. " + "Check encryption key size vs. ESAPI.EncryptionKeyLength or Encryptor.ChooseIVMethod property.", cex);
        } catch (InvalidAlgorithmParameterException e) {
            throw new EncryptionException("Encryption failure (invalid IV)", "Encryption problem: Invalid IV spec: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            throw new EncryptionException("Encryption failure (no padding used; invalid input size)", "Encryption problem: Invalid input size without padding (" + xform + "). " + e.getMessage(), e);
        } catch (BadPaddingException e) {
            throw new EncryptionException("Encryption failure", "[Note: Should NEVER happen in encryption mode.] Encryption problem: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("Encryption failure (unavailable cipher requested)", "Encryption problem: specified algorithm in cipher xform " + xform + " not available: " + e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptionException("Encryption failure (unavailable padding scheme requested)", "Encryption problem: specified padding scheme in cipher xform " + xform + " not available: " + e.getMessage(), e);
        } finally {
            if (success && overwritePlaintext) {
                plain.overwrite();
            }
        }
    }

    /**
	  * Convenience method that decrypts previously encrypted plaintext strings
	  * that were encrypted using the new encryption mechanism (with CBC mode and
	  * PKCS5 padding by default).  This decryption method uses the master
	  * encryption key specified by the {@code Encryptor.MasterKey} property
	  * in {@code ESAPI.properties}.
	  * 
	  * @param b64IVCiphertext	A base64-encoded representation of the
	  * 							IV + raw ciphertext string to be decrypted with
	  * 							the default master key.
	  * @return	The plaintext string prior to encryption.
	  * @throws EncryptionException When something fails with the decryption.
	  * 
	  * @see org.owasp.esapi.Encryptor#decrypt(CipherText)
	  */
    @Deprecated
    public String decrypt(String b64IVCiphertext) throws EncryptionException {
        logWarning("decrypt", "Calling deprecated decrypt() method.");
        CipherText ct = null;
        try {
            ct = new CipherText();
            byte[] ivPlusRawCipherText = ESAPI.encoder().decodeFromBase64(b64IVCiphertext);
            int blockSize = ct.getBlockSize();
            byte[] iv = new byte[blockSize];
            CryptoHelper.copyByteArray(ivPlusRawCipherText, iv, blockSize);
            int cipherTextSize = ivPlusRawCipherText.length - blockSize;
            byte[] rawCipherText = new byte[cipherTextSize];
            System.arraycopy(ivPlusRawCipherText, blockSize, rawCipherText, 0, cipherTextSize);
            ct.setIVandCiphertext(iv, rawCipherText);
            PlainText plaintext = decrypt(ct);
            return plaintext.toString();
        } catch (UnsupportedEncodingException e) {
            logger.error(Logger.SECURITY_FAILURE, "UTF-8 encoding not available! Decryption failed.", e);
            return null;
        } catch (IOException e) {
            logger.error(Logger.SECURITY_FAILURE, "Base64 decoding of IV+ciphertext failed. Decryption failed.", e);
            return null;
        }
    }

    /**
	* {@inheritDoc}
	*/
    public PlainText decrypt(CipherText ciphertext) throws EncryptionException {
        return decrypt(secretKeySpec, ciphertext);
    }

    /**
	 * {@inheritDoc}
	 */
    public PlainText decrypt(SecretKey key, CipherText ciphertext) throws EncryptionException, IllegalArgumentException {
        long start = System.nanoTime();
        if (key == null) {
            throw new IllegalArgumentException("SecretKey arg may not be null");
        }
        if (ciphertext == null) {
            throw new IllegalArgumentException("Ciphertext may arg not be null");
        }
        if (!CryptoHelper.isAllowedCipherMode(ciphertext.getCipherMode())) {
            throw new EncryptionException(DECRYPTION_FAILED, "Invalid cipher mode " + ciphertext.getCipherMode() + " not permitted for decryption or encryption operations.");
        }
        logger.debug(Logger.EVENT_SUCCESS, "Args valid for JavaEncryptor.decrypt(SecretKey,CipherText): " + ciphertext);
        PlainText plaintext = null;
        boolean caughtException = false;
        int progressMark = 0;
        try {
            boolean valid = CryptoHelper.isCipherTextMACvalid(key, ciphertext);
            if (!valid) {
                try {
                    handleDecryption(key, ciphertext);
                } catch (Exception ex) {
                    ;
                }
                throw new EncryptionException(DECRYPTION_FAILED, "Decryption failed because MAC invalid for " + ciphertext);
            }
            progressMark++;
            plaintext = handleDecryption(key, ciphertext);
            progressMark++;
        } catch (EncryptionException ex) {
            caughtException = true;
            String logMsg = null;
            switch(progressMark) {
                case 1:
                    logMsg = "Decryption failed because MAC invalid. See logged exception for details.";
                    break;
                case 2:
                    logMsg = "Decryption failed because handleDecryption() failed. See logged exception for details.";
                    break;
                default:
                    logMsg = "Programming error: unexpected progress mark == " + progressMark;
                    break;
            }
            logger.error(Logger.SECURITY_FAILURE, logMsg);
            throw ex;
        } finally {
            if (caughtException) {
                long now = System.nanoTime();
                long elapsed = now - start;
                final long NANOSECS_IN_SEC = 1000000000L;
                long nSecs = N_SECS * NANOSECS_IN_SEC;
                if (elapsed < nSecs) {
                    long extraSleep = nSecs - elapsed;
                    long millis = extraSleep / 1000000L;
                    long nanos = (extraSleep - (millis * 1000000L));
                    assert nanos >= 0 && nanos <= Integer.MAX_VALUE : "Nanosecs out of bounds; nanos = " + nanos;
                    try {
                        Thread.sleep(millis, (int) nanos);
                    } catch (InterruptedException ex) {
                        ;
                    }
                }
            }
        }
        return plaintext;
    }

    private PlainText handleDecryption(SecretKey key, CipherText ciphertext) throws EncryptionException {
        int keySize = 0;
        try {
            Cipher decrypter = Cipher.getInstance(ciphertext.getCipherTransformation());
            keySize = key.getEncoded().length * 8;
            boolean preferredCipherMode = CryptoHelper.isCombinedCipherMode(ciphertext.getCipherMode());
            SecretKey encKey = null;
            if (preferredCipherMode) {
                encKey = key;
            } else {
                encKey = computeDerivedKey(ciphertext.getKDFVersion(), ciphertext.getKDF_PRF(), key, keySize, "encryption");
            }
            if (ciphertext.requiresIV()) {
                decrypter.init(Cipher.DECRYPT_MODE, encKey, new IvParameterSpec(ciphertext.getIV()));
            } else {
                decrypter.init(Cipher.DECRYPT_MODE, encKey);
            }
            byte[] output = decrypter.doFinal(ciphertext.getRawCipherText());
            return new PlainText(output);
        } catch (InvalidKeyException ike) {
            throw new EncryptionException(DECRYPTION_FAILED, "Must install JCE Unlimited Strength Jurisdiction Policy Files from Sun", ike);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException(DECRYPTION_FAILED, "Invalid algorithm for available JCE providers - " + ciphertext.getCipherTransformation() + ": " + e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptionException(DECRYPTION_FAILED, "Invalid padding scheme (" + ciphertext.getPaddingScheme() + ") for cipher transformation " + ciphertext.getCipherTransformation() + ": " + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new EncryptionException(DECRYPTION_FAILED, "Decryption problem: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            throw new EncryptionException(DECRYPTION_FAILED, "Decryption problem: " + e.getMessage(), e);
        } catch (BadPaddingException e) {
            SecretKey authKey;
            try {
                authKey = computeDerivedKey(ciphertext.getKDFVersion(), ciphertext.getKDF_PRF(), key, keySize, "authenticity");
            } catch (Exception e1) {
                throw new EncryptionException(DECRYPTION_FAILED, "Decryption problem -- failed to compute derived key for authenticity: " + e1.getMessage(), e1);
            }
            boolean success = ciphertext.validateMAC(authKey);
            if (success) {
                throw new EncryptionException(DECRYPTION_FAILED, "Decryption problem: " + e.getMessage(), e);
            } else {
                throw new EncryptionException(DECRYPTION_FAILED, "Decryption problem: WARNING: Adversary may have tampered with " + "CipherText object orCipherText object mangled in transit: " + e.getMessage(), e);
            }
        }
    }

    /**
	* {@inheritDoc}
	*/
    public String sign(String data) throws EncryptionException {
        try {
            Signature signer = Signature.getInstance(signatureAlgorithm);
            signer.initSign(privateKey);
            signer.update(data.getBytes(encoding));
            byte[] bytes = signer.sign();
            return ESAPI.encoder().encodeForBase64(bytes, false);
        } catch (InvalidKeyException ike) {
            throw new EncryptionException("Encryption failure", "Must install unlimited strength crypto extension from Sun", ike);
        } catch (Exception e) {
            throw new EncryptionException("Signature failure", "Can't find signature algorithm " + signatureAlgorithm, e);
        }
    }

    /**
	* {@inheritDoc}
	*/
    public boolean verifySignature(String signature, String data) {
        try {
            byte[] bytes = ESAPI.encoder().decodeFromBase64(signature);
            Signature signer = Signature.getInstance(signatureAlgorithm);
            signer.initVerify(publicKey);
            signer.update(data.getBytes(encoding));
            return signer.verify(bytes);
        } catch (Exception e) {
            new EncryptionException("Invalid signature", "Problem verifying signature: " + e.getMessage(), e);
            return false;
        }
    }

    /**
	* {@inheritDoc}
     *
     * @param expiration
     * @throws IntegrityException
     */
    public String seal(String data, long expiration) throws IntegrityException {
        if (data == null) {
            throw new IllegalArgumentException("Data to be sealed may not be null.");
        }
        try {
            String b64data = null;
            try {
                b64data = ESAPI.encoder().encodeForBase64(data.getBytes("UTF-8"), false);
            } catch (UnsupportedEncodingException e) {
                ;
            }
            String nonce = ESAPI.randomizer().getRandomString(10, EncoderConstants.CHAR_ALPHANUMERICS);
            String plaintext = expiration + ":" + nonce + ":" + b64data;
            String sig = this.sign(plaintext);
            CipherText ciphertext = this.encrypt(new PlainText(plaintext + ":" + sig));
            String sealedData = ESAPI.encoder().encodeForBase64(ciphertext.asPortableSerializedByteArray(), false);
            return sealedData;
        } catch (EncryptionException e) {
            throw new IntegrityException(e.getUserMessage(), e.getLogMessage(), e);
        }
    }

    /**
	* {@inheritDoc}
	*/
    public String unseal(String seal) throws EncryptionException {
        PlainText plaintext = null;
        try {
            byte[] encryptedBytes = ESAPI.encoder().decodeFromBase64(seal);
            CipherText cipherText = null;
            try {
                cipherText = CipherText.fromPortableSerializedBytes(encryptedBytes);
            } catch (AssertionError e) {
                throw new EncryptionException("Invalid seal", "Seal passed garbarge data resulting in AssertionError: " + e);
            }
            plaintext = this.decrypt(cipherText);
            String[] parts = plaintext.toString().split(":");
            if (parts.length != 4) {
                throw new EncryptionException("Invalid seal", "Seal was not formatted properly.");
            }
            String timestring = parts[0];
            long now = new Date().getTime();
            long expiration = Long.parseLong(timestring);
            if (now > expiration) {
                throw new EncryptionException("Invalid seal", "Seal expiration date of " + new Date(expiration) + " has past.");
            }
            String nonce = parts[1];
            String b64data = parts[2];
            String sig = parts[3];
            if (!this.verifySignature(sig, timestring + ":" + nonce + ":" + b64data)) {
                throw new EncryptionException("Invalid seal", "Seal integrity check failed");
            }
            return new String(ESAPI.encoder().decodeFromBase64(b64data), "UTF-8");
        } catch (EncryptionException e) {
            throw e;
        } catch (Exception e) {
            throw new EncryptionException("Invalid seal", "Invalid seal:" + e.getMessage(), e);
        }
    }

    /**
	* {@inheritDoc}
	*/
    public boolean verifySeal(String seal) {
        try {
            unseal(seal);
            return true;
        } catch (EncryptionException e) {
            return false;
        }
    }

    /**
	* {@inheritDoc}
	*/
    public long getTimeStamp() {
        return new Date().getTime();
    }

    /**
	* {@inheritDoc}
	*/
    public long getRelativeTimeStamp(long offset) {
        return new Date().getTime() + offset;
    }

    /**
     * Log a security warning every Nth time one of the deprecated encrypt or
     * decrypt methods are called. ('N' is hard-coded to be 25 by default, but
     * may be changed via the system property
     * {@code ESAPI.Encryptor.warnEveryNthUse}.) In other words, we nag
     * them until the give in and change it. ;-)
     * 
     * @param where The string "encrypt" or "decrypt", corresponding to the
     *              method that is being logged.
     * @param msg   The message to log.
     */
    private void logWarning(String where, String msg) {
        int counter = 0;
        if (where.equals("encrypt")) {
            counter = encryptCounter++;
            where = "JavaEncryptor.encrypt(): [count=" + counter + "]";
        } else if (where.equals("decrypt")) {
            counter = decryptCounter++;
            where = "JavaEncryptor.decrypt(): [count=" + counter + "]";
        } else {
            where = "JavaEncryptor: Unknown method: ";
        }
        if ((counter % logEveryNthUse) == 0) {
            logger.warning(Logger.SECURITY_FAILURE, where + msg);
        }
    }

    private KeyDerivationFunction.PRF_ALGORITHMS getPRF(String name) {
        String prfName = null;
        if (name == null) {
            prfName = ESAPI.securityConfiguration().getKDFPseudoRandomFunction();
        } else {
            prfName = name;
        }
        KeyDerivationFunction.PRF_ALGORITHMS prf = KeyDerivationFunction.convertNameToPRF(prfName);
        return prf;
    }

    private KeyDerivationFunction.PRF_ALGORITHMS getDefaultPRF() {
        String prfName = ESAPI.securityConfiguration().getKDFPseudoRandomFunction();
        return getPRF(prfName);
    }

    private SecretKey computeDerivedKey(int kdfVersion, KeyDerivationFunction.PRF_ALGORITHMS prf, SecretKey kdk, int keySize, String purpose) throws NoSuchAlgorithmException, InvalidKeyException, EncryptionException {
        assert prf != null : "Pseudo Random Function for KDF cannot be null";
        assert kdk != null : "Key derivation key cannot be null.";
        assert keySize >= 56 : "Key has size of " + keySize + ", which is less than minimum of 56-bits.";
        assert (keySize % 8) == 0 : "Key size (" + keySize + ") must be a even multiple of 8-bits.";
        assert purpose != null : "Purpose cannot be null. Should be 'encryption' or 'authenticity'.";
        assert purpose.equals("encryption") || purpose.equals("authenticity") : "Purpose must be \"encryption\" or \"authenticity\".";
        KeyDerivationFunction kdf = new KeyDerivationFunction(prf);
        if (kdfVersion != 0) {
            kdf.setVersion(kdfVersion);
        }
        return kdf.computeDerivedKey(kdk, keySize, purpose);
    }

    private static void setupAlgorithms() {
        encryptAlgorithm = ESAPI.securityConfiguration().getEncryptionAlgorithm();
        signatureAlgorithm = ESAPI.securityConfiguration().getDigitalSignatureAlgorithm();
        randomAlgorithm = ESAPI.securityConfiguration().getRandomAlgorithm();
        hashAlgorithm = ESAPI.securityConfiguration().getHashAlgorithm();
        hashIterations = ESAPI.securityConfiguration().getHashIterations();
        encoding = ESAPI.securityConfiguration().getCharacterEncoding();
        encryptionKeyLength = ESAPI.securityConfiguration().getEncryptionKeyLength();
        signatureKeyLength = ESAPI.securityConfiguration().getDigitalSignatureKeyLength();
    }

    private static void initKeyPair(SecureRandom prng) throws NoSuchAlgorithmException {
        String sigAlg = signatureAlgorithm.toLowerCase();
        if (sigAlg.endsWith("withdsa")) {
            sigAlg = "DSA";
        } else if (sigAlg.endsWith("withrsa")) {
            sigAlg = "RSA";
        }
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(sigAlg);
        keyGen.initialize(signatureKeyLength, prng);
        KeyPair pair = keyGen.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
    }
}
