package cdc.standard.twofish;

import java.io.*;
import javax.crypto.SecretKey;

/**
 * TWOFISHKey is used to store a symmetric key for TWOFISH
 * encryption/decryption.<p>
 *
 * @author  Katja Rauch
 * @version 1.0
 */
public class TWOFISHKey implements SecretKey, Externalizable {

    /**
   * This array is used to store the key data
   */
    private byte[] key;

    /**
   * The constructor only calls super() and then quits.
   */
    public TWOFISHKey() {
        super();
        return;
    }

    /**
   * This constructor builds a new key using the passed
   * data.
   *
   * @param b contains the data for the new key.
   */
    public TWOFISHKey(byte[] b) {
        key = new byte[b.length];
        System.arraycopy(b, 0, key, 0, b.length);
    }

    /**
   * This procedure returns the name of the algorithm the
   * key is used for.
   *
   * @result The result is the name of the algorithm the key
   *         is used for as a String.
   */
    public String getAlgorithm() {
        return "TWOFISH";
    }

    /**
   * This method will return the format of the key
   * as a String.
   *
   * @result The format of the stored key as a String.
   */
    public String getFormat() {
        return "RAW";
    }

    /**
   * This function will return a copy of the stored key.
   *
   * @result The result is the stored key in a new array.
   */
    public byte[] getEncoded() {
        byte[] tmp;
        tmp = new byte[key.length];
        System.arraycopy(key, 0, tmp, 0, key.length);
        return tmp;
    }

    /**
   * This method will write the keys length and the key itself
   * to the passed output object.
   *
   * @serialData
   * @param out The ObjectOutput the Keydata is supposed to be
   *            written to.
   * @exception IOException will be raised if any problem occured
   *                        writing the key
   */
    public void writeExternal(ObjectOutput out) throws IOException {
        out.write((byte) key.length);
        out.write(key);
    }

    /**
   * readExternal will read a key from an external ObjectInput.
   *
   * @param in The ObjectInput that is to be used for reading
   *           the key data.
   */
    public void readExternal(ObjectInput in) throws IOException {
        int len;
        len = in.read();
        key = new byte[len];
        in.readFully(key, 0, len);
    }
}
