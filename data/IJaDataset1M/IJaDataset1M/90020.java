package eu.cherrytree.paj.utilities;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Encrypter {

    public static class KeyGenerationException extends Exception {

        private static final long serialVersionUID = -8817918790895626947L;

        public KeyGenerationException(String msg) {
            super("Couldn't generate secret key." + msg);
        }
    }

    public class EncryptionIntializationException extends Exception {

        private static final long serialVersionUID = 4224946478738059899L;

        public EncryptionIntializationException() {
            super("Couldn't initialize DES encryption mechanism.");
        }
    }

    public class DecryptionException extends Exception {

        private static final long serialVersionUID = -7436565476614246834L;

        public DecryptionException(String msg) {
            super("Couldn't decrypt string." + msg);
        }
    }

    public class EncryptionException extends Exception {

        private static final long serialVersionUID = -380269770134789123L;

        public EncryptionException(String msg) {
            super("Couldn't encrypt string." + msg);
        }
    }

    private Cipher ecipher;

    private Cipher dcipher;

    public Encrypter(SecretKey key) throws EncryptionIntializationException {
        try {
            ecipher = Cipher.getInstance("DES");
            dcipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            throw new EncryptionIntializationException();
        }
    }

    public String encrypt(String str) throws EncryptionException {
        try {
            return Base64.encodeBytes(ecipher.doFinal(str.getBytes("UTF8")));
        } catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }
    }

    public String decrypt(String str) throws DecryptionException {
        try {
            return new String(dcipher.doFinal(Base64.decode(str)), "UTF8");
        } catch (Exception e) {
            throw new DecryptionException(e.getMessage());
        }
    }

    public static SecretKey generateKey() throws KeyGenerationException {
        try {
            return KeyGenerator.getInstance("DES").generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new KeyGenerationException(e.getMessage());
        }
    }
}
