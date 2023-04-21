package net.sf.bacchus.charset;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * {@link java.nio.charset.Charset} that decodes only the characters that are
 * allowed in an ACH entry, and attempts to transliterate characters when
 * encoding into that set. THE TRANSLITERATIONS MAY RESULT IN MORE THAN ONE BYTE
 * OF OUTPUT FOR A SINGLE INPUT CHARACTER. This character set is intended for
 * internal translations. Using it directly on an output stream to a fixed-width
 * format such as an ACH record may result in exceeding the available field
 * size. The transliterations cover ligatures found in the
 * {@link java.lang.Character.UnicodeBlock#LATIN_1_SUPPLEMENT} and
 * {@link java.lang.Character.UnicodeBlock#LATIN_EXTENDED_A} ranges, and replace
 * them with two or three character equivalents. In addition, the byte order
 * marks {@code FFFE} and {@code FFFF} are suppressed, resulting in ZERO bytes
 * of output.
 * @see X_ACH_XP
 */
public class X_ACH_XL extends X_ACH_XP {

    /** the name of this character set. */
    public static final String NAME = "X-ACH-XL";

    /**
     * Encoder that attempts to map out-of-range characters to equivalents that
     * can be represented as a sequence of one or more single byte characters.
     * Characters that expand to multiple characters include the German ss,
     * ligatures, and similar.
     */
    protected static class Transliterator extends Transposer {

        /** maximum bytes emitted for one character. */
        private static final int MAX_BYTES = 3;

        /**
         * Constructs an encoder for {@code X-ACH-XL}.
         * @param cs the character set this decodes.
         */
        public Transliterator(final X_ACH_XL cs) {
            this(cs, MAX_BYTES);
        }

        /**
         * Constructs an encoder for {@code X-ACH-XT}.
         * @param cs the character set this decodes.
         * @param maxBytesPerChar the maximum number of bytes for a single
         * character.
         */
        protected Transliterator(final Charset cs, final float maxBytesPerChar) {
            super(cs, maxBytesPerChar);
        }

        /**
         * Maps characters to their single or multi-byte equivalents. The
         * {@link X_ACH_XP.Transposer#map(char, ByteBuffer) superclass
         * implementation} handles the single byte cases.
         * <p>
         * This method may also suppress generating any equivalent for a
         * character, in effect giving it a zero-byte equivalent but still
         * returning {@code true}. Suppressing a character by this means is not
         * the same as saying that it is not mappable; a suppressed character is
         * considered to have been mapped successfully. This implementation
         * suppresses the byte order marks {@code 0xFFFE} and {@code 0xFFFF}.
         * Subclasses may suppress additional characters.
         * </p>
         * @param c {@inheritDoc}
         * @param out {@inheritDoc}
         * @return {@inheritDoc}
         */
        @Override
        protected boolean map(final char c, final ByteBuffer out) {
            final boolean result;
            if (super.map(c, out)) result = true; else {
                final String mapped = mapToString(c);
                if (mapped == null) result = false; else {
                    for (final char m : mapped.toCharArray()) out.put((byte) m);
                    result = true;
                }
            }
            return result;
        }

        /**
         * Maps a single character to a {@link String}. This method handles
         * single characters that are best represented within the allowable
         * output as multiple characters. For example, the single character
         * ligature {@literal ﬂ} would maps to the two character String "
         * {@literal fl}".
         * @param c the character to map.
         * @return a {@link String} that represents the character.
         */
        protected String mapToString(final char c) {
            final String mapped;
            switch(c) {
                case 'Æ':
                    mapped = "AE";
                    break;
                case 'æ':
                    mapped = "ae";
                    break;
                case 'ß':
                    mapped = "ss";
                    break;
                case 'Ĳ':
                    mapped = "IJ";
                    break;
                case 'ĳ':
                    mapped = "ij";
                    break;
                case 'Œ':
                    mapped = "OE";
                    break;
                case 'œ':
                    mapped = "oe";
                    break;
                case 'ﬀ':
                    mapped = "ff";
                    break;
                case 'ﬁ':
                    mapped = "fi";
                    break;
                case 'ﬂ':
                    mapped = "fl";
                    break;
                case 'ﬃ':
                    mapped = "ffi";
                    break;
                case 'ﬄ':
                    mapped = "ffl";
                    break;
                case 'ﬅ':
                    mapped = "ft";
                    break;
                case 'ﬆ':
                    mapped = "st";
                    break;
                case '￾':
                case '￿':
                    mapped = "";
                    break;
                default:
                    mapped = null;
            }
            return mapped;
        }
    }

    ;

    /** Initializes a new {@code X-ACH-XL} {@link Charset}. */
    public X_ACH_XL() {
        super(NAME);
    }

    /**
     * This is a very limited {@link Charset} that is assumed only to contain
     * itself and those character sets contained by {@link X_ACH_XP}.
     * @param cs {@inheritDoc}
     * @return {@inheritDoc}
     * @see X_ACH_XP#contains(Charset)
     */
    @Override
    public boolean contains(final Charset cs) {
        return NAME.equals(cs.name()) || super.contains(cs);
    }

    /**
     * Delegates to {@link X_US_ASCII_ACH#newEncoder()}, which is a temporary
     * implementation.
     * @return {@inheritDoc}
     * @see X_US_ASCII_ACH#newEncoder()
     */
    @Override
    public CharsetEncoder newEncoder() {
        return new Transliterator(this);
    }
}
