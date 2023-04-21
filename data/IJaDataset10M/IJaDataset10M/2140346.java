package cdc.standard.mac.hmac;

import javax.crypto.SecretKey;
import javax.crypto.spec.*;
import java.security.spec.*;
import java.security.*;

/**
 * A simple class representing a key for HMac. The Key is derived by a
 * Key Derivation Function using a hash function (SHA1 or MD5). This class
 * is provided for <a href="http://www.rsasecurity.com/rsalabs/pkcs/pkcs-12/index.html">
 * PKCS#12</a>. A PFX PDU may be password-integrity-protected. The key for
 * HmacSHA1 computation shall be derived by a Key Derivation Function PBKDF1
 * as described in PKCS #5.
 * <p>
 * @author
 * Michele Boivin
 * @version 0.1
 */
public class HMacKey extends Object implements SecretKey {

    /**
     * This array is used to store the key data.
     * @serial
     */
    private byte[] b_key_ = null;

    private char[] c_key_ = null;

    private boolean b_key_is_used = false;

    /**
     * This constructor generates a new HMacKey with the specified parameters.
     * <p>
     * @param params the parameters used to derive a Key.
     */
    public HMacKey(byte[] b) {
        b_key_ = b;
        b_key_is_used = true;
    }

    /**
     * This constructor generates a new HMacKey with the specified parameters.
     * <p>
     * @param params the parameters used to derive a Key.
     */
    public HMacKey(char[] b) {
        c_key_ = b;
        b_key_is_used = false;
    }

    /**
     * This procedure returns the name of the Algorithm the
     * key is used for (Hmac).
     * <p>
     *
     * @return The result is the name of the Algorithm the key
     *         is used for as a String.
     */
    public String getAlgorithm() {
        return "Hmac";
    }

    /**
     * This function returns null.
     * <p>
     * @return null.
     */
    public byte[] getEncoded() {
        return getKeyBytes();
    }

    /**
     * This method will return the Format of the key
     * as a String.
     * <p>
     * @return the Format.
     */
    public String getFormat() {
        return "RAW-BMP";
    }

    /**
     * Return the passphrase.
     * <p>
     * @return the key.
     */
    public char[] getKey() {
        if (!b_key_is_used) return c_key_; else {
            char[] ret = new char[b_key_.length];
            for (int i = 0; i < ret.length; i++) ret[i] = (char) b_key_[i];
            return ret;
        }
    }

    private byte[] getKeyBytes() {
        if (b_key_is_used) return b_key_;
        byte[] out = new byte[c_key_.length];
        for (int i = 0; i < c_key_.length; i++) {
            out[i] = (byte) c_key_[i];
        }
        int length = out.length * 2;
        byte[] ret = new byte[length + 2];
        int j = 0;
        for (int i = 0; i < out.length; i++) {
            j = i * 2;
            ret[j] = 0;
            ret[j + 1] = out[i];
        }
        ret[length] = 0;
        ret[length + 1] = 0;
        return ret;
    }
}
