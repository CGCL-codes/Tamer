package ursus.io.crypto;

import java.security.PublicKey;

/**
 * KeyExchanger implementations provide a utility class for setting up encrypted
 * sessions with a public key exchange.
 *
 * @author Anthony
 */
public interface KeyExchanger {

    /**
     * Sets the JCE provider to be used for a KeyExchanger instance.
     *
     * @param provider The short name for the provider to be used.
     */
    public void setProvider(String provider);

    /**
     * Sets the Parameter size in bits for generating new key sets.
     *
     * @param bits The parameter size in bits.
     */
    public void setParameterSize(int bits);

    /**
     * Returns the parameter size in bits for generating new key sets.
     *
     * @return The parameter size in bits.
     */
    public int getParameterSize();

    /**
     * Returns the provider to be used for a KeyExchanger instance.
     *
     * @return The provider to be used.
     */
    public String getProvider();

    /**
     * This is the first method called after creating a new instance of a KeyExchanger.
     * This returns a public key of the size that this KeyExchanger was set up for.
     * 
     * For example if you have a new instance of a KeyExchanger and you<br><br>
     *<code>
     * setParameterSize(512);<br>
     * bytes[] key = generatePublicKey();<br><br>
     *</code>
     * key.length will be equal to 512.
     */
    public byte[] generatePublicKey();

    /**
     * This method takes an encoded key created in the byte[] generatePublicKey() method
     * and creates another PublicKey object based off the specs of param key.
     * What this method basically does is take's your friends public key and 
     * creates you a new public key based off your friends that can be returned
     * to your friend for a final "phase".
     *
     * @param key The key to be speced in order to create a new public key.
     */
    public PublicKey generatePublicKey(byte[] key);

    /**
     * Once you receive your friends public key this method will take that 
     * key and return a new shared secret key in the form of an encoded byte
     * array.  The byte array returned can be plugged directly into an object
     * that implements Encryption.  The generatePublicKey method must be called
     * before this method in order for it to work properly!
     *
     * @param key The PublicKey to base the creation of a new private key off of.
     * @see Encryption
     */
    public byte[] generatePrivateKey(PublicKey key);

    /**
     * Utility method needed in order to convert your friends encoded public
     * key into a PublicKey object suitable for passing as a parameter to the
     * generatePrivateKey(PublicKey) method.
     *
     * @param encoded The encoded PublicKey.
     */
    public PublicKey getPublicKeyFromEncoded(byte[] encoded);
}
