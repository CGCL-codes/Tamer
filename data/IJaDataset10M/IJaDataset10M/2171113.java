package org.apache.harmony.security.asn1;

import java.io.IOException;
import java.math.BigInteger;

/**
 * This class represents ASN.1 Integer type.
 * 
 * @see http://asn1.elibel.tm.fr/en/standards/index.htm
 */
public class ASN1Integer extends ASN1Primitive {

    private static final ASN1Integer ASN1 = new ASN1Integer();

    /**
     * Constructs ASN.1 Integer type
     * 
     * The constructor is provided for inheritance purposes
     * when there is a need to create a custom ASN.1 Integer type.
     * To get a default implementation it is recommended to use
     * getInstance() method.
     */
    public ASN1Integer() {
        super(TAG_INTEGER);
    }

    /**
     * Returns ASN.1 Integer type default implementation
     * 
     * The default implementation works with encoding
     * that is represented as byte array in two's-complement notation.
     *
     * @return ASN.1 Integer type default implementation
     */
    public static ASN1Integer getInstance() {
        return ASN1;
    }

    public Object decode(BerInputStream in) throws IOException {
        in.readInteger();
        if (in.isVerify) {
            return null;
        }
        return getDecodedObject(in);
    }

    /**
     * Extracts array of bytes from BER input stream.
     *
     * @param in - BER input stream
     * @return array of bytes
     */
    public Object getDecodedObject(BerInputStream in) throws IOException {
        byte[] bytesEncoded = new byte[in.length];
        System.arraycopy(in.buffer, in.contentOffset, bytesEncoded, 0, in.length);
        return bytesEncoded;
    }

    public void encodeContent(BerOutputStream out) {
        out.encodeInteger();
    }

    public void setEncodingContent(BerOutputStream out) {
        out.length = ((byte[]) out.content).length;
    }

    /**
     * Converts decoded ASN.1 Integer to int value.
     *
     * @param decoded a decoded object corresponding to {@link #asn1 this type}
     * @return decoded int value.
     */
    public static int toIntValue(Object decoded) {
        return new BigInteger((byte[]) decoded).intValue();
    }

    /**
     * Converts primitive int value to a form most suitable for encoding.
     *
     * @param value primitive value to be encoded
     * @return object suitable for encoding
     */
    public static Object fromIntValue(int value) {
        return BigInteger.valueOf(value).toByteArray();
    }
}
