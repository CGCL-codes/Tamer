package cdc.standard.spec;

import java.security.spec.KeySpec;
import java.security.InvalidKeyException;

/**
 * This class provides a specification for a RIJNDAEL key.
 *
 * @see KeySpec
 *
 * @author  Katja Rauch
 * @version 1.0
 */
public class RIJNDAELKeySpec implements KeySpec {

    private byte[] RIJNDAELKey;

    /**
   * Uses the first 16 bytes in key as the RIJNDAEL key.
   *
   * @param key   the bytes making up the key.
   * @exception InvalidKeyException if the key material is too short.
  */
    public RIJNDAELKeySpec(byte[] key) throws InvalidKeyException {
        this(key, 0, 16);
    }

    /**
   * Uses the first len bytes in key as the RIJNDAEL key
   *
   * @param key   the bytes making up the key.
   * @param len   the size (in bytes) of the key.
   * @exception InvalidKeyException if the key material is too short.
  */
    public RIJNDAELKeySpec(byte[] key, int len) throws InvalidKeyException {
        this(key, 0, len);
    }

    /**
   * Uses the first len bytes in key, beginning at offset, as the RIJNDAEL key
   *
   * @param key           the bytes making up the key.
   * @param offset        the offset to start copying the key material.
   * @param len           the size (in bytes) of the key.
   * @exception InvalidKeyException if the key material is too short.
  */
    public RIJNDAELKeySpec(byte[] key, int offset, int len) throws InvalidKeyException {
        if (key.length - offset < len) {
            throw new InvalidKeyException("Key too short");
        }
        RIJNDAELKey = new byte[len];
        System.arraycopy(key, offset, RIJNDAELKey, 0, len);
    }

    /**
   * Returns the RIJNDAEL key.
   *
   * @return the bytes making up the key.
  */
    public byte[] getKey() {
        return RIJNDAELKey;
    }
}
