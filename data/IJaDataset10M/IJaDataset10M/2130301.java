package org.makagiga.pim;

import java.io.UnsupportedEncodingException;

/**
 * Converts String to and from bytes using the encodings required by the Java specification. These encodings are specified in <a
 * href="http://download.oracle.com/javase/1.5.0/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
 * 
 * @see <a href="http://download.oracle.com/javase/1.5.0/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
 * @author <a href="mailto:ggregory@seagullsw.com">Gary Gregory</a>
 * @author kdt (a lightweight version for Makagiga)
 * @version $Id: StringUtils.java 1170351 2011-09-13 21:09:09Z ggregory $
 * @since 1.4
 */
class StringUtils {

    /**
     * Encodes the given string into a sequence of bytes using the US-ASCII charset, storing the result into a new byte
     * array.
     * 
     * @param string
     *            the String to encode, may be <code>null</code>
     * @return encoded bytes, or <code>null</code> if the input string was <code>null</code>
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://download.oracle.com/javase/1.5.0/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUsAscii(String string) {
        return StringUtils.getBytesUnchecked(string, "US-ASCII");
    }

    /**
     * Encodes the given string into a sequence of bytes using the named charset, storing the result into a new byte
     * array.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and rethrows it as {@link IllegalStateException}, which
     * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
     * </p>
     * 
     * @param string
     *            the String to encode, may be <code>null</code>
     * @param charsetName
     *            The name of a required {@link java.nio.charset.Charset}
     * @return encoded bytes, or <code>null</code> if the input string was <code>null</code>
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen for a
     *             required charset name.
     * @see String#getBytes(String)
     */
    public static byte[] getBytesUnchecked(String string, String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw StringUtils.newIllegalStateException(charsetName, e);
        }
    }

    private static IllegalStateException newIllegalStateException(String charsetName, UnsupportedEncodingException e) {
        return new IllegalStateException(charsetName + ": " + e);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the given charset.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and re-throws it as {@link IllegalStateException}, which
     * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
     * </p>
     * 
     * @param bytes
     *            The bytes to be decoded into characters, may be <code>null</code>
     * @param charsetName
     *            The name of a required {@link java.nio.charset.Charset}
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset,
     *         or <code>null</code> if the input byte array was <code>null</code>.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen for a
     *             required charset name.
     * @see String#String(byte[], String)
     */
    public static String newString(byte[] bytes, String charsetName) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw StringUtils.newIllegalStateException(charsetName, e);
        }
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the US-ASCII charset.
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the US-ASCII charset,
     *         or <code>null</code> if the input byte array was <code>null</code>.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static String newStringUsAscii(byte[] bytes) {
        return StringUtils.newString(bytes, "US-ASCII");
    }
}
