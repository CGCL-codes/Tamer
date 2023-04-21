package de.mud.ssh;

/**
 * Cipher class is the type for all other ciphers.
 * @author Marcus Meissner
 * @version $Id: Cipher.java 499 2005-09-29 08:24:54Z leo $
 */
public abstract class Cipher {

    public static Cipher getInstance(String algorithm) {
        Class c;
        try {
            c = Class.forName("de.mud.ssh." + algorithm);
            return (Cipher) c.newInstance();
        } catch (Throwable t) {
            System.err.println("Cipher: unable to load instance of '" + algorithm + "'");
            return null;
        }
    }

    /**
   * Encrypt source byte array using the instantiated algorithm.
   */
    public byte[] encrypt(byte[] src) {
        byte[] dest = new byte[src.length];
        encrypt(src, 0, dest, 0, src.length);
        return dest;
    }

    /**
   * The actual encryption takes place here.
   */
    public abstract void encrypt(byte[] src, int srcOff, byte[] dest, int destOff, int len);

    /**
   * Decrypt source byte array using the instantiated algorithm.
   */
    public byte[] decrypt(byte[] src) {
        byte[] dest = new byte[src.length];
        decrypt(src, 0, dest, 0, src.length);
        return dest;
    }

    /**
   * The actual decryption takes place here.
   */
    public abstract void decrypt(byte[] src, int srcOff, byte[] dest, int destOff, int len);

    public abstract void setKey(byte[] key);

    public void setKey(String key) {
        setKey(key.getBytes());
    }
}
