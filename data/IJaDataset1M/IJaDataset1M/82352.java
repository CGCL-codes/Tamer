package org.jcvi.glk.encoders;

import java.nio.CharBuffer;

/**
 * A <code>PositionsEncoder</code> is responsible for encoding an array of 
 * position values into a {@link String} which is easily storable within a
 * database or other text storage mechanism.  This avoids the difficulty
 * of trying to store raw binary data in a way that is easily read by other
 * algorithms.
 * 
 * @author jsitz
 * @author dkatzel
 */
public interface PositionsEncoder {

    /**
     * Encodes an array of position values to a database storable {@link String}.
     * The resulting String contains only printable characters and should not
     * contain any punctuation which has special meaning in SQL.
     * 
     * @param positions An array of positions values as <code>short</code>s.
     * @return A {@link String} containing an encoded version of the position
     * value array given.
     */
    String encode(short[] positions);

    /**
     * Decodes an encoded position string into an array of raw position values.
     * <p>
     * This allows the decoding of {@link String}s and {@link StringBuilder}s
     * as well as read-only and direct {@link CharBuffer}s.
     * 
     * @param encodedString A {@link CharSequence} containing string-encoded
     * position values as produced by {@link #encode(short[])} or one of its 
     * siblings.
     * @return An array of <code>short</code>s containing raw, numerical 
     * position values.
     * @throws IllegalEncodedValueException If the encoded string contains a 
     * value outside of the allowed range.
     */
    short[] decode(CharSequence encodedString) throws IllegalEncodedValueException;
}
