package ca.uwaterloo.crysp.otr.crypt.jca;

import javax.crypto.spec.SecretKeySpec;

/**
 * Abstract class for a HMAC tagging or verification key.
 * 
 * @author Can Tang (c24tang@gmail.com)
 */
public class JCAHMACKey extends ca.uwaterloo.crysp.otr.crypt.HMACKey {

    private java.security.Key key;

    /**
	 * Constructs the wrapping instance of the given HMAC key using the JCA provider.
	 * 
	 * @param k the HMAC key.
	 */
    public JCAHMACKey(java.security.Key k) {
        this.key = k;
    }

    /**
	 * Constructs an HMAC key from a byte-array.
	 * @param encodedKey The encoded key.
	 */
    public JCAHMACKey(byte[] encodedKey) {
        SecretKeySpec sKeySpec = new SecretKeySpec(encodedKey, "ANYTHING");
        key = sKeySpec;
    }

    /**
	 * Returns the JCA instance of the HMAC key.
	 * @return the JCA instance of the HMAC key.
	 */
    public java.security.Key getHMACKey() {
        return key;
    }

    public String toString() {
        return getValue();
    }

    /**
	 * Returns the value of the HMAC key.
	 * 
	 * @return the value of the HMAC key.
	 */
    public String getValue() {
        return key.toString();
    }

    public byte[] getEncoded() {
        return null;
    }
}
