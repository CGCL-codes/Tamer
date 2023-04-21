package cdc.standard.pbe;

import javax.crypto.SecretKey;
import javax.crypto.spec.*;

/**
* A simple class representing a key for PBE (Passphrase Based Encryption).
* <p>
* @author
* <a href="mailto:twahren@cdc.informatik.tu-darmstadt.de">Thomas Wahrenbruch</a>
* @version 0.1
*/
public class PBEBMPKey implements SecretKey {

    /**
	 * This array is used to store the key data.
	 * @serial
	 */
    private char[] key_ = null;

    /**
	* The default constructor does nothing.
	*/
    public PBEBMPKey() {
    }

    /**
	* This constructor generates a new PBEKey with the specified chars.
	* <p>
	* @param b the key.
	*/
    public PBEBMPKey(char[] b) {
        key_ = b;
    }

    /**
	 * This procedure returns the name of the Algorithm the
	 * key is used for (PBE).
	 * <p>
	 *
	 * @return The result is the name of the Algorithm the key
	 *         is used for as a String.
	 */
    public String getAlgorithm() {
        return "PBE";
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
        return key_;
    }

    /**
	* Returns the key.
	* <p>
	* @return the key.
	*/
    public byte[] getKeyBytes() {
        byte[] out = new byte[key_.length];
        for (int i = 0; i < key_.length; i++) {
            out[i] = (byte) key_[i];
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
