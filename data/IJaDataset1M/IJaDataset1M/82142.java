package ca.uwaterloo.crysp.otr.crypt.rim;

import net.rim.device.api.crypto.AESKey;

/**
 * Wrapper class for the AES key provided by RIM
 * 
 */
public class RIMAESKey extends ca.uwaterloo.crysp.otr.crypt.AESKey {

    AESKey secretKey;

    /**
     * Constructs the wrapping instance of the given AES key using the RIM
     * provider.
     * 
     * @param secretKey
     *            the AES key.
     */
    public RIMAESKey(AESKey secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Constructs an AES key from a byte-array.
     * 
     * @param encodedKey
     *            The encoded key.
     */
    public RIMAESKey(byte[] encodedKey) {
        secretKey = new AESKey(encodedKey);
    }

    /**
     * Returns the RIM instance of the secret key.
     * 
     * @return the RIM instance of the secret key.
     */
    public AESKey getSecretKey() {
        return secretKey;
    }

    public String toString() {
        return secretKey.toString();
    }

    public byte[] getEncoded() {
        try {
            return secretKey.getData();
        } catch (Exception e) {
        }
        return null;
    }
}
