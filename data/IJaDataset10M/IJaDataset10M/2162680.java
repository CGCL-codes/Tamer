package org.apache.shiro.util;

/**
 * A {@code ByteSource} wraps a byte array and provides additional encoding operations.  Most users will find
 * that the {@link SimpleByteSource SimpleByteSource} implementation meets most needs.
 *
 * @since 1.0
 * @see SimpleByteSource
 */
public interface ByteSource {

    /**
     * Returns the wrapped byte array.
     *
     * @return the wrapped byte array.
     */
    public byte[] getBytes();

    /**
     * Returns the <a href="http://en.wikipedia.org/wiki/Hexadecimal">Hex</a>-formatted String representation of the
     * underlying wrapped byte array.
     *
     * @return the <a href="http://en.wikipedia.org/wiki/Hexadecimal">Hex</a>-formatted String representation of the
     *         underlying wrapped byte array.
     */
    public String toHex();

    /**
     * Returns the <a href="http://en.wikipedia.org/wiki/Base64">Base 64</a>-formatted String representation of the
     * underlying wrapped byte array.
     *
     * @return the <a href="http://en.wikipedia.org/wiki/Base64">Base 64</a>-formatted String representation of the
     *         underlying wrapped byte array.
     */
    public String toBase64();
}
