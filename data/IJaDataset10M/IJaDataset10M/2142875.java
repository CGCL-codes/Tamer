package eu.cherrytree.paj.utilities;

/**
 * <p>Encodes and decodes to and from Base64 notation.</p>
 * <p>Homepage: <a href="http://iharder.net/base64">http://iharder.net/base64</a>.</p>
 * 
 * <p>
 * I am placing this code in the Public Domain. Do with it as you will.
 * This software comes with no guarantees or warranties but with
 * plenty of well-wishing instead!
 * Please visit <a href="http://iharder.net/base64">http://iharder.net/base64</a>
 * periodically to check for updates or to contribute improvements.
 * </p>
 *
 * @author Robert Harder
 * @author rob@iharder.net
 * @version 2.3.7
 */
public class Base64 {

    /** No options specified. Value is zero. */
    public static final int NO_OPTIONS = 0;

    /** Specify encoding in first bit. Value is one. */
    public static final int ENCODE = 1;

    /** Specify decoding in first bit. Value is zero. */
    public static final int DECODE = 0;

    /** Specify that data should be gzip-compressed in second bit. Value is two. */
    public static final int GZIP = 2;

    /** Specify that gzipped data should <em>not</em> be automatically gunzipped. */
    public static final int DONT_GUNZIP = 4;

    /** Do break lines when encoding. Value is 8. */
    public static final int DO_BREAK_LINES = 8;

    /** 
     * Encode using Base64-like encoding that is URL- and Filename-safe as described
     * in Section 4 of RFC3548: 
     * <a href="http://www.faqs.org/rfcs/rfc3548.html">http://www.faqs.org/rfcs/rfc3548.html</a>.
     * It is important to note that data encoded this way is <em>not</em> officially valid Base64, 
     * or at the very least should not be called Base64 without also specifying that is
     * was encoded using the URL- and Filename-safe dialect.
     */
    public static final int URL_SAFE = 16;

    /**
      * Encode using the special "ordered" dialect of Base64 described here:
      * <a href="http://www.faqs.org/qa/rfcc-1940.html">http://www.faqs.org/qa/rfcc-1940.html</a>.
      */
    public static final int ORDERED = 32;

    /** Maximum line length (76) of Base64 output. */
    private static final int MAX_LINE_LENGTH = 76;

    /** The equals sign (=) as a byte. */
    private static final byte EQUALS_SIGN = (byte) '=';

    /** The new line character (\n) as a byte. */
    private static final byte NEW_LINE = (byte) '\n';

    /** Preferred encoding. */
    private static final String PREFERRED_ENCODING = "US-ASCII";

    private static final byte WHITE_SPACE_ENC = -5;

    private static final byte EQUALS_SIGN_ENC = -1;

    private static final byte[] _STANDARD_ALPHABET = { (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '+', (byte) '/' };

    /** 
     * Translates a Base64 value to either its 6-bit reconstruction value
     * or a negative number indicating some other meaning.
     **/
    private static final byte[] _STANDARD_DECODABET = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };

    /**
     * Used in the URL- and Filename-safe dialect described in Section 4 of RFC3548: 
     * <a href="http://www.faqs.org/rfcs/rfc3548.html">http://www.faqs.org/rfcs/rfc3548.html</a>.
     * Notice that the last two bytes become "hyphen" and "underscore" instead of "plus" and "slash."
     */
    private static final byte[] _URL_SAFE_ALPHABET = { (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '-', (byte) '_' };

    /**
     * Used in decoding URL- and Filename-safe dialects of Base64.
     */
    private static final byte[] _URL_SAFE_DECODABET = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };

    /**
     * I don't get the point of this technique, but someone requested it,
     * and it is described here:
     * <a href="http://www.faqs.org/qa/rfcc-1940.html">http://www.faqs.org/qa/rfcc-1940.html</a>.
     */
    private static final byte[] _ORDERED_ALPHABET = { (byte) '-', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) '_', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z' };

    /**
     * Used in decoding the "ordered" dialect of Base64.
     */
    private static final byte[] _ORDERED_DECODABET = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };

    /**
     * Returns one of the _SOMETHING_ALPHABET byte arrays depending on
     * the options specified.
     * It's possible, though silly, to specify ORDERED <b>and</b> URLSAFE
     * in which case one of them will be picked, though there is
     * no guarantee as to which one will be picked.
     */
    private static final byte[] getAlphabet(int options) {
        if ((options & URL_SAFE) == URL_SAFE) {
            return _URL_SAFE_ALPHABET;
        } else if ((options & ORDERED) == ORDERED) {
            return _ORDERED_ALPHABET;
        } else {
            return _STANDARD_ALPHABET;
        }
    }

    /**
     * Returns one of the _SOMETHING_DECODABET byte arrays depending on
     * the options specified.
     * It's possible, though silly, to specify ORDERED and URL_SAFE
     * in which case one of them will be picked, though there is
     * no guarantee as to which one will be picked.
     */
    private static final byte[] getDecodabet(int options) {
        if ((options & URL_SAFE) == URL_SAFE) {
            return _URL_SAFE_DECODABET;
        } else if ((options & ORDERED) == ORDERED) {
            return _ORDERED_DECODABET;
        } else {
            return _STANDARD_DECODABET;
        }
    }

    /** Defeats instantiation. */
    private Base64() {
    }

    /**
     * Encodes up to the first three bytes of array <var>threeBytes</var>
     * and returns a four-byte array in Base64 notation.
     * The actual number of significant bytes in your array is
     * given by <var>numSigBytes</var>.
     * The array <var>threeBytes</var> needs only be as big as
     * <var>numSigBytes</var>.
     * Code can reuse a byte array by passing a four-byte array as <var>b4</var>.
     *
     * @param b4 A reusable byte array to reduce array instantiation
     * @param threeBytes the array to convert
     * @param numSigBytes the number of significant bytes in your array
     * @return four byte array in Base64 notation.
     * @since 1.5.1
     */
    private static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes, int options) {
        encode3to4(threeBytes, 0, numSigBytes, b4, 0, options);
        return b4;
    }

    /**
     * <p>Encodes up to three bytes of the array <var>source</var>
     * and writes the resulting four Base64 bytes to <var>destination</var>.
     * The source and destination arrays can be manipulated
     * anywhere along their length by specifying 
     * <var>srcOffset</var> and <var>destOffset</var>.
     * This method does not check to make sure your arrays
     * are large enough to accomodate <var>srcOffset</var> + 3 for
     * the <var>source</var> array or <var>destOffset</var> + 4 for
     * the <var>destination</var> array.
     * The actual number of significant bytes in your array is
     * given by <var>numSigBytes</var>.</p>
	 * <p>This is the lowest level of the encoding methods with
	 * all possible parameters.</p>
     *
     * @param source the array to convert
     * @param srcOffset the index where conversion begins
     * @param numSigBytes the number of significant bytes in your array
     * @param destination the array to hold the conversion
     * @param destOffset the index where output will be put
     * @return the <var>destination</var> array
     * @since 1.3
     */
    private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, int options) {
        byte[] ALPHABET = getAlphabet(options);
        int inBuff = (numSigBytes > 0 ? ((source[srcOffset] << 24) >>> 8) : 0) | (numSigBytes > 1 ? ((source[srcOffset + 1] << 24) >>> 16) : 0) | (numSigBytes > 2 ? ((source[srcOffset + 2] << 24) >>> 24) : 0);
        switch(numSigBytes) {
            case 3:
                destination[destOffset] = ALPHABET[(inBuff >>> 18)];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
                destination[destOffset + 3] = ALPHABET[(inBuff) & 0x3f];
                return destination;
            case 2:
                destination[destOffset] = ALPHABET[(inBuff >>> 18)];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
                destination[destOffset + 3] = EQUALS_SIGN;
                return destination;
            case 1:
                destination[destOffset] = ALPHABET[(inBuff >>> 18)];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
                destination[destOffset + 2] = EQUALS_SIGN;
                destination[destOffset + 3] = EQUALS_SIGN;
                return destination;
            default:
                return destination;
        }
    }

    /**
     * Performs Base64 encoding on the <code>raw</code> ByteBuffer,
     * writing it to the <code>encoded</code> ByteBuffer.
     * This is an experimental feature. Currently it does not
     * pass along any options (such as {@link #DO_BREAK_LINES}
     * or {@link #GZIP}.
     *
     * @param raw input buffer
     * @param encoded output buffer
     * @since 2.3
     */
    public static void encode(java.nio.ByteBuffer raw, java.nio.ByteBuffer encoded) {
        byte[] raw3 = new byte[3];
        byte[] enc4 = new byte[4];
        while (raw.hasRemaining()) {
            int rem = Math.min(3, raw.remaining());
            raw.get(raw3, 0, rem);
            Base64.encode3to4(enc4, raw3, rem, Base64.NO_OPTIONS);
            encoded.put(enc4);
        }
    }

    /**
     * Performs Base64 encoding on the <code>raw</code> ByteBuffer,
     * writing it to the <code>encoded</code> CharBuffer.
     * This is an experimental feature. Currently it does not
     * pass along any options (such as {@link #DO_BREAK_LINES}
     * or {@link #GZIP}.
     *
     * @param raw input buffer
     * @param encoded output buffer
     * @since 2.3
     */
    public static void encode(java.nio.ByteBuffer raw, java.nio.CharBuffer encoded) {
        byte[] raw3 = new byte[3];
        byte[] enc4 = new byte[4];
        while (raw.hasRemaining()) {
            int rem = Math.min(3, raw.remaining());
            raw.get(raw3, 0, rem);
            Base64.encode3to4(enc4, raw3, rem, Base64.NO_OPTIONS);
            for (int i = 0; i < 4; i++) {
                encoded.put((char) (enc4[i] & 0xFF));
            }
        }
    }

    /**
     * Serializes an object and returns the Base64-encoded
     * version of that serialized object.  
     *  
     * <p>As of v 2.3, if the object
     * cannot be serialized or there is another error,
     * the method will throw an java.io.IOException. <b>This is new to v2.3!</b>
     * In earlier versions, it just returned a null value, but
     * in retrospect that's a pretty poor way to handle it.</p>
     * 
     * The object is not GZip-compressed before being encoded.
     *
     * @param serializableObject The object to encode
     * @return The Base64-encoded object
     * @throws java.io.IOException if there is an error
     * @throws NullPointerException if serializedObject is null
     * @since 1.4
     */
    public static String encodeObject(java.io.Serializable serializableObject) throws java.io.IOException {
        return encodeObject(serializableObject, NO_OPTIONS);
    }

    /**
     * Serializes an object and returns the Base64-encoded
     * version of that serialized object.
     *  
     * <p>As of v 2.3, if the object
     * cannot be serialized or there is another error,
     * the method will throw an java.io.IOException. <b>This is new to v2.3!</b>
     * In earlier versions, it just returned a null value, but
     * in retrospect that's a pretty poor way to handle it.</p>
     * 
     * The object is not GZip-compressed before being encoded.
     * <p>
     * Example options:<pre>
     *   GZIP: gzip-compresses object before encoding it.
     *   DO_BREAK_LINES: break lines at 76 characters
     * </pre>
     * <p>
     * Example: <code>encodeObject( myObj, Base64.GZIP )</code> or
     * <p>
     * Example: <code>encodeObject( myObj, Base64.GZIP | Base64.DO_BREAK_LINES )</code>
     *
     * @param serializableObject The object to encode
     * @param options Specified options
     * @return The Base64-encoded object
     * @see Base64#GZIP
     * @see Base64#DO_BREAK_LINES
     * @throws java.io.IOException if there is an error
     * @since 2.0
     */
    public static String encodeObject(java.io.Serializable serializableObject, int options) throws java.io.IOException {
        if (serializableObject == null) {
            throw new NullPointerException("Cannot serialize a null object.");
        }
        java.io.ByteArrayOutputStream baos = null;
        java.io.OutputStream b64os = null;
        java.util.zip.GZIPOutputStream gzos = null;
        java.io.ObjectOutputStream oos = null;
        try {
            baos = new java.io.ByteArrayOutputStream();
            b64os = new Base64.OutputStream(baos, ENCODE | options);
            if ((options & GZIP) != 0) {
                gzos = new java.util.zip.GZIPOutputStream(b64os);
                oos = new java.io.ObjectOutputStream(gzos);
            } else {
                oos = new java.io.ObjectOutputStream(b64os);
            }
            oos.writeObject(serializableObject);
        } catch (java.io.IOException e) {
            throw e;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                gzos.close();
            } catch (Exception e) {
            }
            try {
                b64os.close();
            } catch (Exception e) {
            }
            try {
                baos.close();
            } catch (Exception e) {
            }
        }
        try {
            return new String(baos.toByteArray(), PREFERRED_ENCODING);
        } catch (java.io.UnsupportedEncodingException uue) {
            return new String(baos.toByteArray());
        }
    }

    /**
     * Encodes a byte array into Base64 notation.
     * Does not GZip-compress data.
     *  
     * @param source The data to convert
     * @return The data in Base64-encoded form
     * @throws NullPointerException if source array is null
     * @since 1.4
     */
    public static String encodeBytes(byte[] source) {
        String encoded = null;
        try {
            encoded = encodeBytes(source, 0, source.length, NO_OPTIONS);
        } catch (java.io.IOException ex) {
            assert false : ex.getMessage();
        }
        assert encoded != null;
        return encoded;
    }

    /**
     * Encodes a byte array into Base64 notation.
     * <p>
     * Example options:<pre>
     *   GZIP: gzip-compresses object before encoding it.
     *   DO_BREAK_LINES: break lines at 76 characters
     *     <i>Note: Technically, this makes your encoding non-compliant.</i>
     * </pre>
     * <p>
     * Example: <code>encodeBytes( myData, Base64.GZIP )</code> or
     * <p>
     * Example: <code>encodeBytes( myData, Base64.GZIP | Base64.DO_BREAK_LINES )</code>
     *
     *  
     * <p>As of v 2.3, if there is an error with the GZIP stream,
     * the method will throw an java.io.IOException. <b>This is new to v2.3!</b>
     * In earlier versions, it just returned a null value, but
     * in retrospect that's a pretty poor way to handle it.</p>
     * 
     *
     * @param source The data to convert
     * @param options Specified options
     * @return The Base64-encoded data as a String
     * @see Base64#GZIP
     * @see Base64#DO_BREAK_LINES
     * @throws java.io.IOException if there is an error
     * @throws NullPointerException if source array is null
     * @since 2.0
     */
    public static String encodeBytes(byte[] source, int options) throws java.io.IOException {
        return encodeBytes(source, 0, source.length, options);
    }

    /**
     * Encodes a byte array into Base64 notation.
     * Does not GZip-compress data.
     *  
     * <p>As of v 2.3, if there is an error,
     * the method will throw an java.io.IOException. <b>This is new to v2.3!</b>
     * In earlier versions, it just returned a null value, but
     * in retrospect that's a pretty poor way to handle it.</p>
     * 
     *
     * @param source The data to convert
     * @param off Offset in array where conversion should begin
     * @param len Length of data to convert
     * @return The Base64-encoded data as a String
     * @throws NullPointerException if source array is null
     * @throws IllegalArgumentException if source array, offset, or length are invalid
     * @since 1.4
     */
    public static String encodeBytes(byte[] source, int off, int len) {
        String encoded = null;
        try {
            encoded = encodeBytes(source, off, len, NO_OPTIONS);
        } catch (java.io.IOException ex) {
            assert false : ex.getMessage();
        }
        assert encoded != null;
        return encoded;
    }

    /**
     * Encodes a byte array into Base64 notation.
     * <p>
     * Example options:<pre>
     *   GZIP: gzip-compresses object before encoding it.
     *   DO_BREAK_LINES: break lines at 76 characters
     *     <i>Note: Technically, this makes your encoding non-compliant.</i>
     * </pre>
     * <p>
     * Example: <code>encodeBytes( myData, Base64.GZIP )</code> or
     * <p>
     * Example: <code>encodeBytes( myData, Base64.GZIP | Base64.DO_BREAK_LINES )</code>
     *
     *  
     * <p>As of v 2.3, if there is an error with the GZIP stream,
     * the method will throw an java.io.IOException. <b>This is new to v2.3!</b>
     * In earlier versions, it just returned a null value, but
     * in retrospect that's a pretty poor way to handle it.</p>
     * 
     *
     * @param source The data to convert
     * @param off Offset in array where conversion should begin
     * @param len Length of data to convert
     * @param options Specified options
     * @return The Base64-encoded data as a String
     * @see Base64#GZIP
     * @see Base64#DO_BREAK_LINES
     * @throws java.io.IOException if there is an error
     * @throws NullPointerException if source array is null
     * @throws IllegalArgumentException if source array, offset, or length are invalid
     * @since 2.0
     */
    public static String encodeBytes(byte[] source, int off, int len, int options) throws java.io.IOException {
        byte[] encoded = encodeBytesToBytes(source, off, len, options);
        try {
            return new String(encoded, PREFERRED_ENCODING);
        } catch (java.io.UnsupportedEncodingException uue) {
            return new String(encoded);
        }
    }

    /**
     * Similar to {@link #encodeBytes(byte[])} but returns
     * a byte array instead of instantiating a String. This is more efficient
     * if you're working with I/O streams and have large data sets to encode.
     *
     *
     * @param source The data to convert
     * @return The Base64-encoded data as a byte[] (of ASCII characters)
     * @throws NullPointerException if source array is null
     * @since 2.3.1
     */
    public static byte[] encodeBytesToBytes(byte[] source) {
        byte[] encoded = null;
        try {
            encoded = encodeBytesToBytes(source, 0, source.length, Base64.NO_OPTIONS);
        } catch (java.io.IOException ex) {
            assert false : "IOExceptions only come from GZipping, which is turned off: " + ex.getMessage();
        }
        return encoded;
    }

    /**
     * Similar to {@link #encodeBytes(byte[], int, int, int)} but returns
     * a byte array instead of instantiating a String. This is more efficient
     * if you're working with I/O streams and have large data sets to encode.
     *
     *
     * @param source The data to convert
     * @param off Offset in array where conversion should begin
     * @param len Length of data to convert
     * @param options Specified options
     * @return The Base64-encoded data as a String
     * @see Base64#GZIP
     * @see Base64#DO_BREAK_LINES
     * @throws java.io.IOException if there is an error
     * @throws NullPointerException if source array is null
     * @throws IllegalArgumentException if source array, offset, or length are invalid
     * @since 2.3.1
     */
    public static byte[] encodeBytesToBytes(byte[] source, int off, int len, int options) throws java.io.IOException {
        if (source == null) {
            throw new NullPointerException("Cannot serialize a null array.");
        }
        if (off < 0) {
            throw new IllegalArgumentException("Cannot have negative offset: " + off);
        }
        if (len < 0) {
            throw new IllegalArgumentException("Cannot have length offset: " + len);
        }
        if (off + len > source.length) {
            throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", off, len, source.length));
        }
        if ((options & GZIP) != 0) {
            java.io.ByteArrayOutputStream baos = null;
            java.util.zip.GZIPOutputStream gzos = null;
            Base64.OutputStream b64os = null;
            try {
                baos = new java.io.ByteArrayOutputStream();
                b64os = new Base64.OutputStream(baos, ENCODE | options);
                gzos = new java.util.zip.GZIPOutputStream(b64os);
                gzos.write(source, off, len);
                gzos.close();
            } catch (java.io.IOException e) {
                throw e;
            } finally {
                try {
                    gzos.close();
                } catch (Exception e) {
                }
                try {
                    b64os.close();
                } catch (Exception e) {
                }
                try {
                    baos.close();
                } catch (Exception e) {
                }
            }
            return baos.toByteArray();
        } else {
            boolean breakLines = (options & DO_BREAK_LINES) != 0;
            int encLen = (len / 3) * 4 + (len % 3 > 0 ? 4 : 0);
            if (breakLines) {
                encLen += encLen / MAX_LINE_LENGTH;
            }
            byte[] outBuff = new byte[encLen];
            int d = 0;
            int e = 0;
            int len2 = len - 2;
            int lineLength = 0;
            for (; d < len2; d += 3, e += 4) {
                encode3to4(source, d + off, 3, outBuff, e, options);
                lineLength += 4;
                if (breakLines && lineLength >= MAX_LINE_LENGTH) {
                    outBuff[e + 4] = NEW_LINE;
                    e++;
                    lineLength = 0;
                }
            }
            if (d < len) {
                encode3to4(source, d + off, len - d, outBuff, e, options);
                e += 4;
            }
            if (e <= outBuff.length - 1) {
                byte[] finalOut = new byte[e];
                System.arraycopy(outBuff, 0, finalOut, 0, e);
                return finalOut;
            } else {
                return outBuff;
            }
        }
    }

    /**
     * Decodes four bytes from array <var>source</var>
     * and writes the resulting bytes (up to three of them)
     * to <var>destination</var>.
     * The source and destination arrays can be manipulated
     * anywhere along their length by specifying 
     * <var>srcOffset</var> and <var>destOffset</var>.
     * This method does not check to make sure your arrays
     * are large enough to accomodate <var>srcOffset</var> + 4 for
     * the <var>source</var> array or <var>destOffset</var> + 3 for
     * the <var>destination</var> array.
     * This method returns the actual number of bytes that 
     * were converted from the Base64 encoding.
	 * <p>This is the lowest level of the decoding methods with
	 * all possible parameters.</p>
     * 
     *
     * @param source the array to convert
     * @param srcOffset the index where conversion begins
     * @param destination the array to hold the conversion
     * @param destOffset the index where output will be put
	 * @param options alphabet type is pulled from this (standard, url-safe, ordered)
     * @return the number of decoded bytes converted
     * @throws NullPointerException if source or destination arrays are null
     * @throws IllegalArgumentException if srcOffset or destOffset are invalid
     *         or there is not enough room in the array.
     * @since 1.3
     */
    private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset, int options) {
        if (source == null) {
            throw new NullPointerException("Source array was null.");
        }
        if (destination == null) {
            throw new NullPointerException("Destination array was null.");
        }
        if (srcOffset < 0 || srcOffset + 3 >= source.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", source.length, srcOffset));
        }
        if (destOffset < 0 || destOffset + 2 >= destination.length) {
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", destination.length, destOffset));
        }
        byte[] DECODABET = getDecodabet(options);
        if (source[srcOffset + 2] == EQUALS_SIGN) {
            int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12);
            destination[destOffset] = (byte) (outBuff >>> 16);
            return 1;
        } else if (source[srcOffset + 3] == EQUALS_SIGN) {
            int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12) | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6);
            destination[destOffset] = (byte) (outBuff >>> 16);
            destination[destOffset + 1] = (byte) (outBuff >>> 8);
            return 2;
        } else {
            int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12) | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6) | ((DECODABET[source[srcOffset + 3]] & 0xFF));
            destination[destOffset] = (byte) (outBuff >> 16);
            destination[destOffset + 1] = (byte) (outBuff >> 8);
            destination[destOffset + 2] = (byte) (outBuff);
            return 3;
        }
    }

    /**
     * Low-level access to decoding ASCII characters in
     * the form of a byte array. <strong>Ignores GUNZIP option, if
     * it's set.</strong> This is not generally a recommended method,
     * although it is used internally as part of the decoding process.
     * Special case: if len = 0, an empty array is returned. Still,
     * if you need more speed and reduced memory footprint (and aren't
     * gzipping), consider this method.
     *
     * @param source The Base64 encoded data
     * @return decoded data
     * @since 2.3.1
     */
    public static byte[] decode(byte[] source) throws java.io.IOException {
        byte[] decoded = null;
        decoded = decode(source, 0, source.length, Base64.NO_OPTIONS);
        return decoded;
    }

    /**
     * Low-level access to decoding ASCII characters in
     * the form of a byte array. <strong>Ignores GUNZIP option, if
     * it's set.</strong> This is not generally a recommended method,
     * although it is used internally as part of the decoding process.
     * Special case: if len = 0, an empty array is returned. Still,
     * if you need more speed and reduced memory footprint (and aren't
     * gzipping), consider this method.
     *
     * @param source The Base64 encoded data
     * @param off    The offset of where to begin decoding
     * @param len    The length of characters to decode
     * @param options Can specify options such as alphabet type to use
     * @return decoded data
     * @throws java.io.IOException If bogus characters exist in source data
     * @since 1.3
     */
    public static byte[] decode(byte[] source, int off, int len, int options) throws java.io.IOException {
        if (source == null) {
            throw new NullPointerException("Cannot decode null source array.");
        }
        if (off < 0 || off + len > source.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", source.length, off, len));
        }
        if (len == 0) {
            return new byte[0];
        } else if (len < 4) {
            throw new IllegalArgumentException("Base64-encoded string must have at least four characters, but length specified was " + len);
        }
        byte[] DECODABET = getDecodabet(options);
        int len34 = len * 3 / 4;
        byte[] outBuff = new byte[len34];
        int outBuffPosn = 0;
        byte[] b4 = new byte[4];
        int b4Posn = 0;
        int i = 0;
        byte sbiDecode = 0;
        for (i = off; i < off + len; i++) {
            sbiDecode = DECODABET[source[i] & 0xFF];
            if (sbiDecode >= WHITE_SPACE_ENC) {
                if (sbiDecode >= EQUALS_SIGN_ENC) {
                    b4[b4Posn++] = source[i];
                    if (b4Posn > 3) {
                        outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, options);
                        b4Posn = 0;
                        if (source[i] == EQUALS_SIGN) {
                            break;
                        }
                    }
                }
            } else {
                throw new java.io.IOException(String.format("Bad Base64 input character decimal %d in array position %d", ((int) source[i]) & 0xFF, i));
            }
        }
        byte[] out = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }

    /**
     * Decodes data from Base64 notation, automatically
     * detecting gzip-compressed data and decompressing it.
     *
     * @param s the string to decode
     * @return the decoded data
     * @throws java.io.IOException If there is a problem
     * @since 1.4
     */
    public static byte[] decode(String s) throws java.io.IOException {
        return decode(s, NO_OPTIONS);
    }

    /**
     * Decodes data from Base64 notation, automatically
     * detecting gzip-compressed data and decompressing it.
     *
     * @param s the string to decode
     * @param options encode options such as URL_SAFE
     * @return the decoded data
     * @throws java.io.IOException if there is an error
     * @throws NullPointerException if <tt>s</tt> is null
     * @since 1.4
     */
    public static byte[] decode(String s, int options) throws java.io.IOException {
        if (s == null) {
            throw new NullPointerException("Input string was null.");
        }
        byte[] bytes;
        try {
            bytes = s.getBytes(PREFERRED_ENCODING);
        } catch (java.io.UnsupportedEncodingException uee) {
            bytes = s.getBytes();
        }
        bytes = decode(bytes, 0, bytes.length, options);
        boolean dontGunzip = (options & DONT_GUNZIP) != 0;
        if ((bytes != null) && (bytes.length >= 4) && (!dontGunzip)) {
            int head = ((int) bytes[0] & 0xff) | ((bytes[1] << 8) & 0xff00);
            if (java.util.zip.GZIPInputStream.GZIP_MAGIC == head) {
                java.io.ByteArrayInputStream bais = null;
                java.util.zip.GZIPInputStream gzis = null;
                java.io.ByteArrayOutputStream baos = null;
                byte[] buffer = new byte[2048];
                int length = 0;
                try {
                    baos = new java.io.ByteArrayOutputStream();
                    bais = new java.io.ByteArrayInputStream(bytes);
                    gzis = new java.util.zip.GZIPInputStream(bais);
                    while ((length = gzis.read(buffer)) >= 0) {
                        baos.write(buffer, 0, length);
                    }
                    bytes = baos.toByteArray();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        baos.close();
                    } catch (Exception e) {
                    }
                    try {
                        gzis.close();
                    } catch (Exception e) {
                    }
                    try {
                        bais.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return bytes;
    }

    /**
     * Attempts to decode Base64 data and deserialize a Java
     * Object within. Returns <tt>null</tt> if there was an error.
     *
     * @param encodedObject The Base64 data to decode
     * @return The decoded and deserialized object
     * @throws NullPointerException if encodedObject is null
     * @throws java.io.IOException if there is a general error
     * @throws ClassNotFoundException if the decoded object is of a
     *         class that cannot be found by the JVM
     * @since 1.5
     */
    public static Object decodeToObject(String encodedObject) throws java.io.IOException, java.lang.ClassNotFoundException {
        return decodeToObject(encodedObject, NO_OPTIONS, null);
    }

    /**
     * Attempts to decode Base64 data and deserialize a Java
     * Object within. Returns <tt>null</tt> if there was an error.
     * If <tt>loader</tt> is not null, it will be the class loader
     * used when deserializing.
     *
     * @param encodedObject The Base64 data to decode
     * @param options Various parameters related to decoding
     * @param loader Optional class loader to use in deserializing classes.
     * @return The decoded and deserialized object
     * @throws NullPointerException if encodedObject is null
     * @throws java.io.IOException if there is a general error
     * @throws ClassNotFoundException if the decoded object is of a 
     *         class that cannot be found by the JVM
     * @since 2.3.4
     */
    public static Object decodeToObject(String encodedObject, int options, final ClassLoader loader) throws java.io.IOException, java.lang.ClassNotFoundException {
        byte[] objBytes = decode(encodedObject, options);
        java.io.ByteArrayInputStream bais = null;
        java.io.ObjectInputStream ois = null;
        Object obj = null;
        try {
            bais = new java.io.ByteArrayInputStream(objBytes);
            if (loader == null) {
                ois = new java.io.ObjectInputStream(bais);
            } else {
                ois = new java.io.ObjectInputStream(bais) {

                    @Override
                    public Class<?> resolveClass(java.io.ObjectStreamClass streamClass) throws java.io.IOException, ClassNotFoundException {
                        Class c = Class.forName(streamClass.getName(), false, loader);
                        if (c == null) {
                            return super.resolveClass(streamClass);
                        } else {
                            return c;
                        }
                    }
                };
            }
            obj = ois.readObject();
        } catch (java.io.IOException e) {
            throw e;
        } catch (java.lang.ClassNotFoundException e) {
            throw e;
        } finally {
            try {
                bais.close();
            } catch (Exception e) {
            }
            try {
                ois.close();
            } catch (Exception e) {
            }
        }
        return obj;
    }

    /**
     * Convenience method for encoding data to a file.
     *
     * <p>As of v 2.3, if there is a error,
     * the method will throw an java.io.IOException. <b>This is new to v2.3!</b>
     * In earlier versions, it just returned false, but
     * in retrospect that's a pretty poor way to handle it.</p>
     * 
     * @param dataToEncode byte array of data to encode in base64 form
     * @param filename Filename for saving encoded data
     * @throws java.io.IOException if there is an error
     * @throws NullPointerException if dataToEncode is null
     * @since 2.1
     */
    public static void encodeToFile(byte[] dataToEncode, String filename) throws java.io.IOException {
        if (dataToEncode == null) {
            throw new NullPointerException("Data to encode was null.");
        }
        Base64.OutputStream bos = null;
        try {
            bos = new Base64.OutputStream(new java.io.FileOutputStream(filename), Base64.ENCODE);
            bos.write(dataToEncode);
        } catch (java.io.IOException e) {
            throw e;
        } finally {
            try {
                bos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Convenience method for decoding data to a file.
     *
     * <p>As of v 2.3, if there is a error,
     * the method will throw an java.io.IOException. <b>This is new to v2.3!</b>
     * In earlier versions, it just returned false, but
     * in retrospect that's a pretty poor way to handle it.</p>
     * 
     * @param dataToDecode Base64-encoded data as a string
     * @param filename Filename for saving decoded data
     * @throws java.io.IOException if there is an error
     * @since 2.1
     */
    public static void decodeToFile(String dataToDecode, String filename) throws java.io.IOException {
        Base64.OutputStream bos = null;
        try {
            bos = new Base64.OutputStream(new java.io.FileOutputStream(filename), Base64.DECODE);
            bos.write(dataToDecode.getBytes(PREFERRED_ENCODING));
        } catch (java.io.IOException e) {
            throw e;
        } finally {
            try {
                bos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Convenience method for reading a base64-encoded
     * file and decoding it.
     *
     * <p>As of v 2.3, if there is a error,
     * the method will throw an java.io.IOException. <b>This is new to v2.3!</b>
     * In earlier versions, it just returned false, but
     * in retrospect that's a pretty poor way to handle it.</p>
     * 
     * @param filename Filename for reading encoded data
     * @return decoded byte array
     * @throws java.io.IOException if there is an error
     * @since 2.1
     */
    public static byte[] decodeFromFile(String filename) throws java.io.IOException {
        byte[] decodedData = null;
        Base64.InputStream bis = null;
        try {
            java.io.File file = new java.io.File(filename);
            byte[] buffer = null;
            int length = 0;
            int numBytes = 0;
            if (file.length() > Integer.MAX_VALUE) {
                throw new java.io.IOException("File is too big for this convenience method (" + file.length() + " bytes).");
            }
            buffer = new byte[(int) file.length()];
            bis = new Base64.InputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(file)), Base64.DECODE);
            while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
                length += numBytes;
            }
            decodedData = new byte[length];
            System.arraycopy(buffer, 0, decodedData, 0, length);
        } catch (java.io.IOException e) {
            throw e;
        } finally {
            try {
                bis.close();
            } catch (Exception e) {
            }
        }
        return decodedData;
    }

    /**
     * Convenience method for reading a binary file
     * and base64-encoding it.
     *
     * <p>As of v 2.3, if there is a error,
     * the method will throw an java.io.IOException. <b>This is new to v2.3!</b>
     * In earlier versions, it just returned false, but
     * in retrospect that's a pretty poor way to handle it.</p>
     * 
     * @param filename Filename for reading binary data
     * @return base64-encoded string
     * @throws java.io.IOException if there is an error
     * @since 2.1
     */
    public static String encodeFromFile(String filename) throws java.io.IOException {
        String encodedData = null;
        Base64.InputStream bis = null;
        try {
            java.io.File file = new java.io.File(filename);
            byte[] buffer = new byte[Math.max((int) (file.length() * 1.4 + 1), 40)];
            int length = 0;
            int numBytes = 0;
            bis = new Base64.InputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(file)), Base64.ENCODE);
            while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
                length += numBytes;
            }
            encodedData = new String(buffer, 0, length, Base64.PREFERRED_ENCODING);
        } catch (java.io.IOException e) {
            throw e;
        } finally {
            try {
                bis.close();
            } catch (Exception e) {
            }
        }
        return encodedData;
    }

    /**
     * Reads <tt>infile</tt> and encodes it to <tt>outfile</tt>.
     *
     * @param infile Input file
     * @param outfile Output file
     * @throws java.io.IOException if there is an error
     * @since 2.2
     */
    public static void encodeFileToFile(String infile, String outfile) throws java.io.IOException {
        String encoded = Base64.encodeFromFile(infile);
        java.io.OutputStream out = null;
        try {
            out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outfile));
            out.write(encoded.getBytes("US-ASCII"));
        } catch (java.io.IOException e) {
            throw e;
        } finally {
            try {
                out.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Reads <tt>infile</tt> and decodes it to <tt>outfile</tt>.
     *
     * @param infile Input file
     * @param outfile Output file
     * @throws java.io.IOException if there is an error
     * @since 2.2
     */
    public static void decodeFileToFile(String infile, String outfile) throws java.io.IOException {
        byte[] decoded = Base64.decodeFromFile(infile);
        java.io.OutputStream out = null;
        try {
            out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outfile));
            out.write(decoded);
        } catch (java.io.IOException e) {
            throw e;
        } finally {
            try {
                out.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * A {@link Base64.InputStream} will read data from another
     * <tt>java.io.InputStream</tt>, given in the constructor,
     * and encode/decode to/from Base64 notation on the fly.
     *
     * @see Base64
     * @since 1.3
     */
    public static class InputStream extends java.io.FilterInputStream {

        private boolean encode;

        private int position;

        private byte[] buffer;

        private int bufferLength;

        private int numSigBytes;

        private int lineLength;

        private boolean breakLines;

        private int options;

        private byte[] decodabet;

        /**
         * Constructs a {@link Base64.InputStream} in DECODE mode.
         *
         * @param in the <tt>java.io.InputStream</tt> from which to read data.
         * @since 1.3
         */
        public InputStream(java.io.InputStream in) {
            this(in, DECODE);
        }

        /**
         * Constructs a {@link Base64.InputStream} in
         * either ENCODE or DECODE mode.
         * <p>
         * Valid options:<pre>
         *   ENCODE or DECODE: Encode or Decode as data is read.
         *   DO_BREAK_LINES: break lines at 76 characters
         *     (only meaningful when encoding)</i>
         * </pre>
         * <p>
         * Example: <code>new Base64.InputStream( in, Base64.DECODE )</code>
         *
         *
         * @param in the <tt>java.io.InputStream</tt> from which to read data.
         * @param options Specified options
         * @see Base64#ENCODE
         * @see Base64#DECODE
         * @see Base64#DO_BREAK_LINES
         * @since 2.0
         */
        public InputStream(java.io.InputStream in, int options) {
            super(in);
            this.options = options;
            this.breakLines = (options & DO_BREAK_LINES) > 0;
            this.encode = (options & ENCODE) > 0;
            this.bufferLength = encode ? 4 : 3;
            this.buffer = new byte[bufferLength];
            this.position = -1;
            this.lineLength = 0;
            this.decodabet = getDecodabet(options);
        }

        /**
         * Reads enough of the input stream to convert
         * to/from Base64 and returns the next byte.
         *
         * @return next byte
         * @since 1.3
         */
        @Override
        public int read() throws java.io.IOException {
            if (position < 0) {
                if (encode) {
                    byte[] b3 = new byte[3];
                    int numBinaryBytes = 0;
                    for (int i = 0; i < 3; i++) {
                        int b = in.read();
                        if (b >= 0) {
                            b3[i] = (byte) b;
                            numBinaryBytes++;
                        } else {
                            break;
                        }
                    }
                    if (numBinaryBytes > 0) {
                        encode3to4(b3, 0, numBinaryBytes, buffer, 0, options);
                        position = 0;
                        numSigBytes = 4;
                    } else {
                        return -1;
                    }
                } else {
                    byte[] b4 = new byte[4];
                    int i = 0;
                    for (i = 0; i < 4; i++) {
                        int b = 0;
                        do {
                            b = in.read();
                        } while (b >= 0 && decodabet[b & 0x7f] <= WHITE_SPACE_ENC);
                        if (b < 0) {
                            break;
                        }
                        b4[i] = (byte) b;
                    }
                    if (i == 4) {
                        numSigBytes = decode4to3(b4, 0, buffer, 0, options);
                        position = 0;
                    } else if (i == 0) {
                        return -1;
                    } else {
                        throw new java.io.IOException("Improperly padded Base64 input.");
                    }
                }
            }
            if (position >= 0) {
                if (position >= numSigBytes) {
                    return -1;
                }
                if (encode && breakLines && lineLength >= MAX_LINE_LENGTH) {
                    lineLength = 0;
                    return '\n';
                } else {
                    lineLength++;
                    int b = buffer[position++];
                    if (position >= bufferLength) {
                        position = -1;
                    }
                    return b & 0xFF;
                }
            } else {
                throw new java.io.IOException("Error in Base64 code reading stream.");
            }
        }

        /**
         * Calls {@link #read()} repeatedly until the end of stream
         * is reached or <var>len</var> bytes are read.
         * Returns number of bytes read into array or -1 if
         * end of stream is encountered.
         *
         * @param dest array to hold values
         * @param off offset for array
         * @param len max number of bytes to read into array
         * @return bytes read into array or -1 if end of stream is encountered.
         * @since 1.3
         */
        @Override
        public int read(byte[] dest, int off, int len) throws java.io.IOException {
            int i;
            int b;
            for (i = 0; i < len; i++) {
                b = read();
                if (b >= 0) {
                    dest[off + i] = (byte) b;
                } else if (i == 0) {
                    return -1;
                } else {
                    break;
                }
            }
            return i;
        }
    }

    /**
     * A {@link Base64.OutputStream} will write data to another
     * <tt>java.io.OutputStream</tt>, given in the constructor,
     * and encode/decode to/from Base64 notation on the fly.
     *
     * @see Base64
     * @since 1.3
     */
    public static class OutputStream extends java.io.FilterOutputStream {

        private boolean encode;

        private int position;

        private byte[] buffer;

        private int bufferLength;

        private int lineLength;

        private boolean breakLines;

        private byte[] b4;

        private boolean suspendEncoding;

        private int options;

        private byte[] decodabet;

        /**
         * Constructs a {@link Base64.OutputStream} in ENCODE mode.
         *
         * @param out the <tt>java.io.OutputStream</tt> to which data will be written.
         * @since 1.3
         */
        public OutputStream(java.io.OutputStream out) {
            this(out, ENCODE);
        }

        /**
         * Constructs a {@link Base64.OutputStream} in
         * either ENCODE or DECODE mode.
         * <p>
         * Valid options:<pre>
         *   ENCODE or DECODE: Encode or Decode as data is read.
         *   DO_BREAK_LINES: don't break lines at 76 characters
         *     (only meaningful when encoding)</i>
         * </pre>
         * <p>
         * Example: <code>new Base64.OutputStream( out, Base64.ENCODE )</code>
         *
         * @param out the <tt>java.io.OutputStream</tt> to which data will be written.
         * @param options Specified options.
         * @see Base64#ENCODE
         * @see Base64#DECODE
         * @see Base64#DO_BREAK_LINES
         * @since 1.3
         */
        public OutputStream(java.io.OutputStream out, int options) {
            super(out);
            this.breakLines = (options & DO_BREAK_LINES) != 0;
            this.encode = (options & ENCODE) != 0;
            this.bufferLength = encode ? 3 : 4;
            this.buffer = new byte[bufferLength];
            this.position = 0;
            this.lineLength = 0;
            this.suspendEncoding = false;
            this.b4 = new byte[4];
            this.options = options;
            this.decodabet = getDecodabet(options);
        }

        /**
         * Writes the byte to the output stream after
         * converting to/from Base64 notation.
         * When encoding, bytes are buffered three
         * at a time before the output stream actually
         * gets a write() call.
         * When decoding, bytes are buffered four
         * at a time.
         *
         * @param theByte the byte to write
         * @since 1.3
         */
        @Override
        public void write(int theByte) throws java.io.IOException {
            if (suspendEncoding) {
                this.out.write(theByte);
                return;
            }
            if (encode) {
                buffer[position++] = (byte) theByte;
                if (position >= bufferLength) {
                    this.out.write(encode3to4(b4, buffer, bufferLength, options));
                    lineLength += 4;
                    if (breakLines && lineLength >= MAX_LINE_LENGTH) {
                        this.out.write(NEW_LINE);
                        lineLength = 0;
                    }
                    position = 0;
                }
            } else {
                if (decodabet[theByte & 0x7f] > WHITE_SPACE_ENC) {
                    buffer[position++] = (byte) theByte;
                    if (position >= bufferLength) {
                        int len = Base64.decode4to3(buffer, 0, b4, 0, options);
                        out.write(b4, 0, len);
                        position = 0;
                    }
                } else if (decodabet[theByte & 0x7f] != WHITE_SPACE_ENC) {
                    throw new java.io.IOException("Invalid character in Base64 data.");
                }
            }
        }

        /**
         * Calls {@link #write(int)} repeatedly until <var>len</var> 
         * bytes are written.
         *
         * @param theBytes array from which to read bytes
         * @param off offset for array
         * @param len max number of bytes to read into array
         * @since 1.3
         */
        @Override
        public void write(byte[] theBytes, int off, int len) throws java.io.IOException {
            if (suspendEncoding) {
                this.out.write(theBytes, off, len);
                return;
            }
            for (int i = 0; i < len; i++) {
                write(theBytes[off + i]);
            }
        }

        /**
         * Method added by PHIL. [Thanks, PHIL. -Rob]
         * This pads the buffer without closing the stream.
         * @throws java.io.IOException  if there's an error.
         */
        public void flushBase64() throws java.io.IOException {
            if (position > 0) {
                if (encode) {
                    out.write(encode3to4(b4, buffer, position, options));
                    position = 0;
                } else {
                    throw new java.io.IOException("Base64 input not properly padded.");
                }
            }
        }

        /** 
         * Flushes and closes (I think, in the superclass) the stream. 
         *
         * @since 1.3
         */
        @Override
        public void close() throws java.io.IOException {
            flushBase64();
            super.close();
            buffer = null;
            out = null;
        }

        /**
         * Suspends encoding of the stream.
         * May be helpful if you need to embed a piece of
         * base64-encoded data in a stream.
         *
         * @throws java.io.IOException  if there's an error flushing
         * @since 1.5.1
         */
        public void suspendEncoding() throws java.io.IOException {
            flushBase64();
            this.suspendEncoding = true;
        }

        /**
         * Resumes encoding of the stream.
         * May be helpful if you need to embed a piece of
         * base64-encoded data in a stream.
         *
         * @since 1.5.1
         */
        public void resumeEncoding() {
            this.suspendEncoding = false;
        }
    }
}
