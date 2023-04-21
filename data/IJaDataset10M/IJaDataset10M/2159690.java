package net.sourceforge.jaulp.crypto.interfaces;

/**
 * Interface for Decryptor objects.
 * 
 * @version 1.0
 * @author Asterios Raptis
 */
public interface Decryptor extends SecretCryptor {

    /**
     * Decrpyt the donated String.
     * 
     * @param string
     *            The String to decrypt.
     * @return The decrypted String.s
     */
    public String decrypt(String string);
}
