package org.icepdf.core.pobjects.security;

import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.StringObject;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * PDF�s standard security handler allows access permissions and up to two passwords
 * to be specified for a document.  The purpose of this class is to encapsulate
 * the algorithms used by the Standard Security Handler.
 * <p/>
 * All of the algorithms used for encryption related calculations are based
 * on the suto code described in the Adobe PDF Specification 1.5.
 *
 * @since 1.1
 */
class StandardEncryption {

    private static final Logger logger = Logger.getLogger(StandardEncryption.class.toString());

    /**
     * Padding String used in PDF encryption related algorithms
     * < 28 BF 4E 5E 4E 75 8A 41 64 00 4E 56 FF FA 01 08
     * 2E 2E 00 B6 D0 68 3E 80 2F 0C A9 FE 64 53 69 7A >
     */
    private static final byte[] PADDING = { (byte) 0x28, (byte) 0xBF, (byte) 0x4E, (byte) 0x5E, (byte) 0x4E, (byte) 0x75, (byte) 0x8A, (byte) 0x41, (byte) 0x64, (byte) 0x00, (byte) 0x4E, (byte) 0x56, (byte) 0xFF, (byte) 0xFA, (byte) 0x01, (byte) 0x08, (byte) 0x2E, (byte) 0x2E, (byte) 0x00, (byte) 0xB6, (byte) 0xD0, (byte) 0x68, (byte) 0x3E, (byte) 0x80, (byte) 0x2F, (byte) 0x0C, (byte) 0xA9, (byte) 0xFE, (byte) 0x64, (byte) 0x53, (byte) 0x69, (byte) 0x7A };

    private EncryptionDictionary encryptionDictionary;

    private byte[] encryptionKey;

    private Reference objectReference;

    private byte[] rc4Key = null;

    private String userPassword = "";

    private String ownerPassword = "";

    /**
     * Create a new instance of the StandardEncryption object.
     *
     * @param encryptionDictionary standard encryption dictionary values
     */
    public StandardEncryption(EncryptionDictionary encryptionDictionary) {
        this.encryptionDictionary = encryptionDictionary;
    }

    /**
     * General encryption algorithm 3.1 for encryption of data using an
     * encryption key.
     */
    public byte[] generalEncryptionAlgorithm(Reference objectReference, byte[] encryptionKey, byte[] inputData) {
        if (objectReference == null || encryptionKey == null || inputData == null) {
        }
        if (rc4Key == null || this.encryptionKey != encryptionKey || this.objectReference != objectReference) {
            this.objectReference = objectReference;
            byte[] step3Bytes = resetObjectReference(objectReference);
            int n = encryptionKey.length;
            rc4Key = new byte[Math.min(n + 5, 16)];
            System.arraycopy(step3Bytes, 0, rc4Key, 0, rc4Key.length);
        }
        byte[] finalData = null;
        try {
            SecretKeySpec key = new SecretKeySpec(rc4Key, "RC4");
            Cipher rc4 = Cipher.getInstance("RC4");
            rc4.init(Cipher.DECRYPT_MODE, key);
            finalData = rc4.doFinal(inputData);
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", ex);
        } catch (IllegalBlockSizeException ex) {
            logger.log(Level.FINE, "IllegalBlockSizeException.", ex);
        } catch (BadPaddingException ex) {
            logger.log(Level.FINE, "BadPaddingException.", ex);
        } catch (NoSuchPaddingException ex) {
            logger.log(Level.FINE, "NoSuchPaddingException.", ex);
        } catch (InvalidKeyException ex) {
            logger.log(Level.FINE, "InvalidKeyException.", ex);
        }
        return finalData;
    }

    /**
     * General encryption algorithm 3.1 for encryption of data using an
     * encryption key.
     */
    public InputStream generalEncryptionInputStream(Reference objectReference, byte[] encryptionKey, InputStream input) {
        if (objectReference == null || encryptionKey == null || input == null) {
            return null;
        }
        if (rc4Key == null || this.encryptionKey != encryptionKey || this.objectReference != objectReference) {
            this.objectReference = objectReference;
            byte[] step3Bytes = resetObjectReference(objectReference);
            int n = encryptionKey.length;
            rc4Key = new byte[Math.min(n + 5, 16)];
            System.arraycopy(step3Bytes, 0, rc4Key, 0, rc4Key.length);
        }
        try {
            SecretKeySpec key = new SecretKeySpec(rc4Key, "RC4");
            Cipher rc4 = Cipher.getInstance("RC4");
            rc4.init(Cipher.DECRYPT_MODE, key);
            CipherInputStream cin = new CipherInputStream(input, rc4);
            return cin;
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", ex);
        } catch (NoSuchPaddingException ex) {
            logger.log(Level.FINE, "NoSuchPaddingException.", ex);
        } catch (InvalidKeyException ex) {
            logger.log(Level.FINE, "InvalidKeyException.", ex);
        }
        return null;
    }

    /**
     * Step 1-3 of the general encryption algorithm 3.1.  The procedure
     * is as follows:
     * <ul>
     * Treat the object number and generation number as binary integers, extend
     * the original n-byte encryption key to n + 5 bytes by appending the
     * low-order 3 bytes of the object number and the low-order 2 bytes of the
     * generation number in that order, low-order byte first. (n is 5 unless
     * the value of V in the encryption dictionary is greater than 1, in which
     * case the n is the value of Length divided by 8.)
     * </ul>
     *
     * @param objectReference pdf object reference or the identifier of the
     *                        inderect object in the case of a string.
     * @return Byte [] manipulated as specified.
     */
    public byte[] resetObjectReference(Reference objectReference) {
        int objectNumber = objectReference.getObjectNumber();
        int generationNumber = objectReference.getGenerationNumber();
        int n = encryptionKey.length;
        byte[] step2Bytes = new byte[n + 5];
        System.arraycopy(encryptionKey, 0, step2Bytes, 0, n);
        step2Bytes[n] = (byte) (objectNumber & 0xff);
        step2Bytes[n + 1] = (byte) (objectNumber >> 8 & 0xff);
        step2Bytes[n + 2] = (byte) (objectNumber >> 16 & 0xff);
        step2Bytes[n + 3] = (byte) (generationNumber & 0xff);
        step2Bytes[n + 4] = (byte) (generationNumber >> 8 & 0xff);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException builtin) {
        }
        md5.update(step2Bytes);
        return md5.digest();
    }

    /**
     * Encryption key algorithm 3.2 for computing an encryption key given
     * a password string.
     */
    public byte[] encryptionKeyAlgorithm(String password, int keyLength) {
        byte[] paddedPassword = padPassword(password);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", ex);
        }
        md5.update(paddedPassword);
        String tmp = encryptionDictionary.getBigO();
        byte[] bigO = new byte[tmp.length()];
        for (int i = 0; i < tmp.length(); i++) {
            bigO[i] = (byte) tmp.charAt(i);
        }
        md5.update(bigO);
        for (int i = 0, p = encryptionDictionary.getPermissions(); i < 4; i++, p >>= 8) {
            md5.update((byte) p);
        }
        String firstFileID = ((StringObject) encryptionDictionary.getFileID().elementAt(0)).getLiteralString();
        byte[] fileID = new byte[firstFileID.length()];
        for (int i = 0; i < firstFileID.length(); i++) {
            fileID[i] = (byte) firstFileID.charAt(i);
        }
        paddedPassword = md5.digest(fileID);
        if (encryptionDictionary.getRevisionNumber() == 3) {
            for (int i = 0; i < 50; i++) {
                paddedPassword = md5.digest(paddedPassword);
            }
        }
        byte[] out = null;
        int n = 5;
        if (encryptionDictionary.getRevisionNumber() == 2) {
            out = new byte[n];
        } else if (encryptionDictionary.getRevisionNumber() == 3) {
            n = keyLength / 8;
            out = new byte[n];
        }
        System.arraycopy(paddedPassword, 0, out, 0, n);
        encryptionKey = out;
        return out;
    }

    /**
     * ToDo: xjava.security.Padding,  look at class for interface to see
     * if PDFPadding class could/should be built
     * <p/>
     * Pad or truncate the password string to exactly 32 bytes.  If the
     * password is more than 32 bytes long, use only its first 32 bytes; if it
     * is less than 32 bytes long, pad it by appending the required number of
     * additional bytes from the beginning of the PADDING string.
     * <p/>
     * NOTE: This is algorithm is the <b>1st</b> step of <b>algorithm 3.2</b>
     * and is commonly used by other methods in this class
     *
     * @param password password to padded
     * @return returned updated password with appropriate padding applied
     */
    protected static byte[] padPassword(String password) {
        byte[] paddedPassword = new byte[32];
        if (password == null || "".equals(password)) {
            return PADDING;
        }
        int passwordLength = Math.min(password.length(), 32);
        byte[] bytePassword = new byte[password.length()];
        for (int i = 0; i < password.length(); i++) {
            bytePassword[i] = (byte) password.charAt(i);
        }
        System.arraycopy(bytePassword, 0, paddedPassword, 0, passwordLength);
        System.arraycopy(PADDING, 0, paddedPassword, passwordLength, 32 - passwordLength);
        return paddedPassword;
    }

    /**
     * Computing Owner password value, Algorithm 3.3.
     *
     * @param ownerPassword    owner pasword string. If there is no owner,
     *                         password use the user password instead.
     * @param userPassword     user password.
     * @param isAuthentication if true, only steps 1-4 of the algorithm will be
     *                         completed.  If false, all 8 steps of the algorithm will be
     *                         completed
     *                         <b>Note : </b><br />
     *                         There may be a bug in this algorithm when all 8 steps are called.
     *                         1-4 are work properly, but 1-8 can not generate an O value that is
     *                         the same as the orgional documents O.  This is not a currently a
     *                         problem as we do not author PDF documents.
     */
    public byte[] calculateOwnerPassword(String ownerPassword, String userPassword, boolean isAuthentication) {
        if ("".equals(ownerPassword) && !"".equals(userPassword)) {
            ownerPassword = userPassword;
        }
        byte[] paddedOwnerPassword = padPassword(ownerPassword);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.FINE, "Could not fint MD5 Digest", e);
        }
        paddedOwnerPassword = md5.digest(paddedOwnerPassword);
        if (encryptionDictionary.getRevisionNumber() == 3) {
            for (int i = 0; i < 50; i++) {
                paddedOwnerPassword = md5.digest(paddedOwnerPassword);
            }
        }
        int dataSize = 5;
        if (encryptionDictionary.getRevisionNumber() == 3) {
            dataSize = encryptionDictionary.getKeyLength() / 8;
        }
        byte[] encryptionKey = new byte[dataSize];
        System.arraycopy(paddedOwnerPassword, 0, encryptionKey, 0, dataSize);
        if (isAuthentication) {
            return encryptionKey;
        }
        byte[] paddedUserPassword = padPassword(userPassword);
        byte[] finalData = null;
        try {
            SecretKeySpec key = new SecretKeySpec(encryptionKey, "RC4");
            Cipher rc4 = Cipher.getInstance("RC4");
            rc4.init(Cipher.ENCRYPT_MODE, key);
            finalData = rc4.update(paddedUserPassword);
            if (encryptionDictionary.getRevisionNumber() == 3) {
                byte[] indexedKey = new byte[encryptionKey.length];
                for (int i = 1; i <= 19; i++) {
                    for (int j = 0; j < encryptionKey.length; j++) {
                        indexedKey[j] = (byte) (encryptionKey[j] ^ i);
                    }
                    key = new SecretKeySpec(indexedKey, "RC4");
                    rc4.init(Cipher.ENCRYPT_MODE, key);
                    finalData = rc4.update(finalData);
                }
            }
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", ex);
        } catch (NoSuchPaddingException ex) {
            logger.log(Level.FINE, "NoSuchPaddingException.", ex);
        } catch (InvalidKeyException ex) {
            logger.log(Level.FINE, "InvalidKeyException.", ex);
        }
        return finalData;
    }

    /**
     * Computing Owner password value, Algorithm 3.4 is respected for
     * Revision = 2 and Algorithm 3.5 is respected for Revisison = 3, null
     * otherwise.
     *
     * @param userPassword user password.
     * @return byte array representing the U value for the encryption dictionary
     */
    public byte[] calculateUserPassword(String userPassword) {
        byte[] encryptionKey = encryptionKeyAlgorithm(userPassword, encryptionDictionary.getKeyLength());
        if (encryptionDictionary.getRevisionNumber() == 2) {
            byte[] paddedUserPassword = PADDING.clone();
            byte[] finalData = null;
            try {
                SecretKeySpec key = new SecretKeySpec(encryptionKey, "RC4");
                Cipher rc4 = Cipher.getInstance("RC4");
                rc4.init(Cipher.ENCRYPT_MODE, key);
                finalData = rc4.doFinal(paddedUserPassword);
            } catch (NoSuchAlgorithmException ex) {
                logger.log(Level.FINE, "NoSuchAlgorithmException.", ex);
            } catch (IllegalBlockSizeException ex) {
                logger.log(Level.FINE, "IllegalBlockSizeException.", ex);
            } catch (BadPaddingException ex) {
                logger.log(Level.FINE, "BadPaddingException.", ex);
            } catch (NoSuchPaddingException ex) {
                logger.log(Level.FINE, "NoSuchPaddingException.", ex);
            } catch (InvalidKeyException ex) {
                logger.log(Level.FINE, "InvalidKeyException.", ex);
            }
            return finalData;
        } else if (encryptionDictionary.getRevisionNumber() == 3) {
            byte[] paddedUserPassword = PADDING.clone();
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                logger.log(Level.FINE, "MD5 digester could not be found", e);
            }
            md5.update(paddedUserPassword);
            String firstFileID = ((StringObject) encryptionDictionary.getFileID().elementAt(0)).getLiteralString();
            byte[] fileID = new byte[firstFileID.length()];
            for (int i = 0; i < firstFileID.length(); i++) {
                fileID[i] = (byte) firstFileID.charAt(i);
            }
            byte[] encryptData = md5.digest(fileID);
            try {
                SecretKeySpec key = new SecretKeySpec(encryptionKey, "RC4");
                Cipher rc4 = Cipher.getInstance("RC4");
                rc4.init(Cipher.ENCRYPT_MODE, key);
                encryptData = rc4.update(encryptData);
                byte[] indexedKey = new byte[encryptionKey.length];
                for (int i = 1; i <= 19; i++) {
                    for (int j = 0; j < encryptionKey.length; j++) {
                        indexedKey[j] = (byte) (encryptionKey[j] ^ (byte) i);
                    }
                    key = new SecretKeySpec(indexedKey, "RC4");
                    rc4.init(Cipher.ENCRYPT_MODE, key);
                    encryptData = rc4.update(encryptData);
                }
            } catch (NoSuchAlgorithmException ex) {
                logger.log(Level.FINE, "NoSuchAlgorithmException.", ex);
            } catch (NoSuchPaddingException ex) {
                logger.log(Level.FINE, "NoSuchPaddingException.", ex);
            } catch (InvalidKeyException ex) {
                logger.log(Level.FINE, "InvalidKeyException.", ex);
            }
            byte[] finalData = new byte[32];
            System.arraycopy(encryptData, 0, finalData, 0, 16);
            System.arraycopy(PADDING, 0, finalData, 16, 16);
            return finalData;
        } else {
            return null;
        }
    }

    /**
     * Authenticating the user password,  algorithm 3.6
     *
     * @param userPassword user password to check for authenticity
     * @return true if the userPassword matches the value the encryption
     *         dictionary U value, false otherwise.
     */
    public boolean authenticateUserPassword(String userPassword) {
        byte[] tmpUValue = calculateUserPassword(userPassword);
        String tmp = encryptionDictionary.getBigU();
        byte[] bigU = new byte[tmp.length()];
        for (int i = 0; i < tmp.length(); i++) {
            bigU[i] = (byte) tmp.charAt(i);
        }
        byte[] trunkUValue;
        if (encryptionDictionary.getRevisionNumber() == 2) {
            trunkUValue = new byte[32];
            System.arraycopy(tmpUValue, 0, trunkUValue, 0, trunkUValue.length);
        } else {
            trunkUValue = new byte[16];
            System.arraycopy(tmpUValue, 0, trunkUValue, 0, trunkUValue.length);
        }
        boolean found = true;
        for (int i = 0; i < trunkUValue.length; i++) {
            if (trunkUValue[i] != bigU[i]) {
                found = false;
                break;
            }
        }
        return found;
    }

    /**
     * Authenticating the owner password,  algorithm 3.7
     */
    public boolean authenticateOwnerPassword(String ownerPassword) {
        byte[] encryptionKey = calculateOwnerPassword(ownerPassword, "", true);
        byte[] decryptedO = null;
        try {
            String tmp = encryptionDictionary.getBigO();
            byte[] bigO = new byte[tmp.length()];
            for (int i = 0; i < tmp.length(); i++) {
                bigO[i] = (byte) tmp.charAt(i);
            }
            if (encryptionDictionary.getRevisionNumber() == 2) {
                SecretKeySpec key = new SecretKeySpec(encryptionKey, "RC4");
                Cipher rc4 = Cipher.getInstance("RC4");
                rc4.init(Cipher.DECRYPT_MODE, key);
                decryptedO = rc4.doFinal(bigO);
            } else {
                byte[] indexedKey = new byte[encryptionKey.length];
                decryptedO = bigO;
                for (int i = 19; i >= 0; i--) {
                    for (int j = 0; j < indexedKey.length; j++) {
                        indexedKey[j] = (byte) (encryptionKey[j] ^ (byte) i);
                    }
                    SecretKeySpec key = new SecretKeySpec(indexedKey, "RC4");
                    Cipher rc4 = Cipher.getInstance("RC4");
                    rc4.init(Cipher.ENCRYPT_MODE, key);
                    decryptedO = rc4.update(decryptedO);
                }
            }
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", ex);
        } catch (IllegalBlockSizeException ex) {
            logger.log(Level.FINE, "IllegalBlockSizeException.", ex);
        } catch (BadPaddingException ex) {
            logger.log(Level.FINE, "BadPaddingException.", ex);
        } catch (NoSuchPaddingException ex) {
            logger.log(Level.FINE, "NoSuchPaddingException.", ex);
        } catch (InvalidKeyException ex) {
            logger.log(Level.FINE, "InvalidKeyException.", ex);
        }
        String tmpUserPassword = "";
        for (byte aDecryptedO : decryptedO) {
            tmpUserPassword += (char) aDecryptedO;
        }
        boolean isValid = authenticateUserPassword(tmpUserPassword);
        if (isValid) {
            userPassword = tmpUserPassword;
            this.ownerPassword = ownerPassword;
        }
        return isValid;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getOwnerPassword() {
        return ownerPassword;
    }
}
