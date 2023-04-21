package edu.vt.middleware.crypt.pkcs;

import edu.vt.middleware.crypt.util.DERHelper;
import org.bouncycastle.asn1.DERSequence;

/**
 * Describes the PBKDF2-params type defined in PKCS#5v2.
 *
 * @author  Middleware Services
 * @version  $Revision: 1818 $
 */
public class PBKDF2Parameters extends PBEParameter {

    /** Length of derived key in number of octets (bytes). */
    private int length;

    /**
   * Creates a new PBKDF2 parameter with given values.
   *
   * @param  saltBytes  Bytes of digest salt.
   * @param  iterations  Number of iterations of mixing function.
   */
    public PBKDF2Parameters(final byte[] saltBytes, final int iterations) {
        super(saltBytes, iterations);
    }

    /**
   * Creates a new PBKDF2 parameter with given values.
   *
   * @param  saltBytes  Bytes of digest salt.
   * @param  iterations  Number of iterations of mixing function.
   * @param  keyByteLength  Size of derived key in bytes (octets).
   */
    public PBKDF2Parameters(final byte[] saltBytes, final int iterations, final int keyByteLength) {
        super(saltBytes, iterations);
        setLength(keyByteLength);
    }

    /**
   * Decodes a DER sequence of PBKDF2 parameters into an instance of this class.
   *
   * @param  params  PBKDF2 parameters as a DER sequence.
   *
   * @return  Equivalent instance of {@link PBKDF2Parameters}.
   */
    public static PBKDF2Parameters decode(final DERSequence params) {
        final DERSequence kdfSeq = (DERSequence) params.getObjectAt(1);
        final PBKDF2Parameters instance = new PBKDF2Parameters(DERHelper.asOctets(kdfSeq.getObjectAt(0)), DERHelper.asInt(kdfSeq.getObjectAt(1)));
        if (kdfSeq.size() > 2) {
            instance.setLength(DERHelper.asInt(kdfSeq.getObjectAt(2)) * 8);
        }
        return instance;
    }

    /**
   * Gets the length of the derived key.
   *
   * @return  Length of derived key in bytes (octets).
   */
    public int getLength() {
        return length;
    }

    /**
   * Sets the length of the derived key.
   *
   * @param  byteLength  Length of derived key in bytes (octets).
   */
    public void setLength(final int byteLength) {
        if (byteLength < 1) {
            throw new IllegalArgumentException("Key length must be positive integer.");
        }
        this.length = byteLength;
    }
}
