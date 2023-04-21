package net.community.chest.io.encode.hex;

import java.io.IOException;
import java.io.OutputStream;
import net.community.chest.reflect.ClassUtil;

/**
 * <P>Copyright 2007 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Aug 22, 2007 12:06:45 PM
 */
public final class Hex {

    private Hex() {
    }

    /**
	 * Lowercase hexadecimal characters as a string
	 */
    public static final String lowerHexDigitsString = "0123456789abcdef";

    /**
	 * Lowercase hexadecimal characters used to encode an index/value in range 0-15
	 */
    public static final char[] lowerHex = lowerHexDigitsString.toCharArray();

    /**
	 * Uppercase hexadecimal characters as a string
	 */
    public static final String upperHexDigitsString = lowerHexDigitsString.toUpperCase();

    /**
	 * Uppercase hexadecimal characters used to encode an index/value in range 0-15
	 */
    public static final char[] upperHex = upperHexDigitsString.toCharArray();

    public static final boolean isHexDigit(char c) {
        return (((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'f')) || ((c >= 'A') && (c <= 'F')));
    }

    public static final boolean isHexDigitValue(byte b) {
        return (((b >= '0') && (b <= '9')) || ((b >= 'a') && (b <= 'f')) || ((b >= 'A') && (b <= 'F')));
    }

    public static final int getHexSequenceLength(final CharSequence cs, final int offset, final int len, final char sep) {
        final int csLen = (null == cs) ? 0 : cs.length(), maxPos = offset + len;
        if ((offset < 0) || (len < 0) || (maxPos > csLen)) return Integer.MIN_VALUE + 1;
        if (0 == len) return 0;
        if (null == cs) return Integer.MIN_VALUE + 2;
        if ('\0' == sep) {
            if ((len & 0x01) != 0) return (0 - len - 1);
        } else {
            if (((len + 1) % 3) != 0) return (0 - len - 1);
        }
        for (int hPos = offset, lPos = hPos + 1; hPos < maxPos; lPos = hPos + 1) {
            final char hi = cs.charAt(hPos), lo = (lPos < maxPos) ? cs.charAt(lPos) : '*';
            if (!isHexDigit(hi)) return (0 - hPos - 1);
            if (!isHexDigit(lo)) return (0 - lPos - 1);
            hPos += 2;
            if (sep != '\0') {
                if (hPos >= maxPos) break;
                final char sc = cs.charAt(hPos);
                if (sc != sep) return (0 - hPos - 1);
                hPos++;
                if (hPos >= maxPos) return (0 - len);
            }
        }
        return ('\0' == sep) ? (len / 2) : ((len + 1) / 3);
    }

    /**
	 * @param cs A {@link CharSequence} that contains a HEX sequence and an
	 * (optional) separator character
	 * @param sep The separator character - '\0' signals no separator
	 * @return The number of <U>bytes</U> that can be represented if
	 * converting this sequence (e.g., via call to {@link #toByteArray(CharSequence, char)}).
	 * Negative if error (e.g., invalid HEX sequence or incomplete
	 * length to form valid bytes values). The negative value represents the
	 * <U>index-1</U> of the bad character (i.e., in order to get the index
	 * simply call <code>0 - value - 1</code>). <B>Note:</B> if bad parameters
	 * to start with then the (absolute) negative value is greater than the
	 * {@link CharSequence#length()} value
	 */
    public static final int getHexSequenceLength(final CharSequence cs, final char sep) {
        return getHexSequenceLength(cs, 0, (null == cs) ? 0 : cs.length(), sep);
    }

    public static final int getHexSequenceLength(final CharSequence cs, final int offset, final int len) {
        return getHexSequenceLength(cs, offset, len, '\0');
    }

    /**
	 * @param cs A {@link CharSequence} that contains a HEX sequence
	 * @return The number of <U>bytes</U> that can be represented if
	 * converting this sequence (e.g., via call to {@link #toByteArray(CharSequence, char)}).
	 * Negative if error (e.g., invalid HEX sequence or incomplete
	 * length to form valid bytes values). The negative value represents the
	 * <U>index-1</U> of the bad character (i.e., in order to get the index
	 * simply call <code>0 - value - 1</code>). If the entire length is not
	 * even, then the {@link CharSequence#length()} (negative) value is
	 * returned as the "index".
	 */
    public static final int getHexSequenceLength(final CharSequence cs) {
        return getHexSequenceLength(cs, '\0');
    }

    public static final int write(byte b, OutputStream os, boolean useUppercase) throws IOException {
        final char[] hexDigits = useUppercase ? upperHex : lowerHex;
        os.write(hexDigits[(b >> 4) & 0x0F]);
        os.write(hexDigits[b & 0x0F]);
        return 2;
    }

    public static final int write(byte[] b, OutputStream os, boolean useUppercase) throws IOException {
        if (null == b) return 0;
        for (int i = 0; i < b.length; i++) write(b[i], os, useUppercase);
        return b.length * 2;
    }

    /**
	 * @param <A> The {@link Appendable} generic type
	 * @param sb {@link Appendable} instance to append to - may NOT be null
	 * @param b value to be appended
     * @param useUppercase if TRUE then uses uppercase HEX digits
	 * @return same as input {@link Appendable} instance
	 * @throws IOException if failed to append or bad parameters
	 */
    public static final <A extends Appendable> A appendHex(final A sb, final byte b, final boolean useUppercase) throws IOException {
        if (null == sb) throw new IOException(ClassUtil.getArgumentsExceptionLocation(Hex.class, "appendHex", Byte.valueOf(b), Boolean.valueOf(useUppercase)) + " no " + Appendable.class.getName() + " instance");
        final char[] hexDigits = useUppercase ? upperHex : lowerHex, valChars = { hexDigits[(b >> 4) & 0x0F], hexDigits[b & 0x0F] };
        sb.append(valChars[0]).append(valChars[1]);
        return sb;
    }

    /**
     * Appends specified array in HEX format
	 * @param <A> The {@link Appendable} generic type
	 * @param sb {@link Appendable} instance to append to - may NOT be null
     * @param a data array to be appended
     * @param nOffset offset within data array from where data is to be appended
     * @param nLen number of elements to be appended starting at specified offset
     * @param delim delimiter to be used between successive values (if '\0' then no delimiting)
     * @param upperCase if TRUE then uses uppercase HEX digits
	 * @return same as input {@link Appendable} instance
	 * @throws IOException if failed to append or bad parameters
     */
    public static final <A extends Appendable> A appendHex(final A sb, final byte[] a, final int nOffset, final int nLen, final char delim, final boolean upperCase) throws IOException {
        for (int idx = 0, pos = nOffset; idx < nLen; idx++, pos++) {
            if ((idx > 0) && (delim != '\0')) sb.append(delim);
            appendHex(sb, a[pos], upperCase);
        }
        return sb;
    }

    /**
	 * Appends specified array in HEX format
	 * @param <A> The {@link Appendable} generic type
	 * @param sb {@link Appendable} instance to append to - may NOT be null
	 * @param a array data to be appended (may be NULL/empty)
	 * @param delim delimiter to be used between successive values (if '\0' then no delimiting)
	 * @param upperCase if TRUE then uses uppercase HEX digits
	 * @return same as input {@link Appendable} instance
	 * @throws IOException if failed to append or bad parameters
	 */
    public static final <A extends Appendable> A appendHex(final A sb, final byte[] a, final char delim, final boolean upperCase) throws IOException {
        return appendHex(sb, a, 0, (null == a) ? 0 : a.length, delim, upperCase);
    }

    /**
     * Appends specified value in HEX format
	 * @param <A> The {@link Appendable} generic type
	 * @param sb {@link Appendable} instance to append to - may NOT be null
     * @param s value to be appended
     * @param upperCase if TRUE then uses uppercase HEX digits
	 * @return same as input {@link Appendable} instance
	 * @throws IOException if failed to append or bad parameters
     */
    public static final <A extends Appendable> A appendHex(final A sb, final short s, final boolean upperCase) throws IOException {
        appendHex(sb, (byte) ((s >> 8) & 0x00FF), upperCase);
        appendHex(sb, (byte) (s & 0x00FF), upperCase);
        return sb;
    }

    /**
	 * Appends specified array in HEX format
	 * @param <A> The {@link Appendable} generic type
	 * @param sb {@link Appendable} instance to append to - may NOT be null
	 * @param a data array to be appended
	 * @param nOffset offsrt within data array from where data is to be appended
	 * @param nLen number of elements to be appended starting at specified offset
	 * @param delim delimiter to be used between successive values (if '\0' then no delimiting)
	 * @param upperCase if TRUE then uses uppercase HEX digits
	 * @return same as input {@link Appendable} instance
	 * @throws IOException if failed to append or bad parameters
	 */
    public static final <A extends Appendable> A appendHex(final A sb, final short[] a, final int nOffset, final int nLen, final char delim, final boolean upperCase) throws IOException {
        for (int idx = 0, pos = nOffset; idx < nLen; idx++, pos++) {
            if ((idx > 0) && (delim != '\0')) sb.append(delim);
            appendHex(sb, a[pos], upperCase);
        }
        return sb;
    }

    /**
	 * Appends specified array in HEX format
	 * @param <A> The {@link Appendable} generic type
	 * @param sb {@link Appendable} instance to append to - may NOT be null
	 * @param a array data to be appended (may be NULL/empty)
	 * @param delim delimiter to be used between successive values (if '\0' then no delimiting)
	 * @param upperCase if TRUE then uses uppercase HEX digits
	 * @return same as input {@link Appendable} instance
	 * @throws IOException if failed to append or bad parameters
	 */
    public static final <A extends Appendable> A appendHex(final A sb, final short[] a, final char delim, final boolean upperCase) throws IOException {
        return appendHex(sb, a, 0, (null == a) ? 0 : a.length, delim, upperCase);
    }

    /**
	 * Appends specified value in HEX format
	 * @param <A> The {@link Appendable} generic type
	 * @param sb {@link Appendable} instance to append to - may NOT be null
	 * @param i value to be appended
	 * @param upperCase if TRUE then uses uppercase HEX digits
	 * @return same as input {@link Appendable} instance
	 * @throws IOException if failed to append or bad parameters
	 */
    public static final <A extends Appendable> A appendHex(final A sb, final int i, final boolean upperCase) throws IOException {
        appendHex(sb, (short) ((i >> 16) & 0x0000FFFF), upperCase);
        appendHex(sb, (short) (i & 0x0000FFFF), upperCase);
        return sb;
    }

    /**
	 * Appends specified array in HEX format
	 * @param <A> The {@link Appendable} generic type
	 * @param sb {@link Appendable} instance to append to - may NOT be null
	 * @param a data array to be appended
	 * @param nOffset offset within data array from where data is to be appended
	 * @param nLen number of elements to be appended starting at specified offset
	 * @param delim delimiter to be used between successive values (if '\0' then no delimiting)
	 * @param upperCase if TRUE then uses uppercase HEX digits
	 * @return same as input {@link Appendable} instance
	 * @throws IOException if failed to append or bad parameters
	 */
    public static final <A extends Appendable> A appendHex(final A sb, final int[] a, final int nOffset, final int nLen, final char delim, final boolean upperCase) throws IOException {
        for (int idx = 0, pos = nOffset; idx < nLen; idx++, pos++) {
            if ((idx > 0) && (delim != '\0')) sb.append(delim);
            appendHex(sb, a[pos], upperCase);
        }
        return sb;
    }

    /**
	 * Appends specified array in HEX format
	 * @param <A> The {@link Appendable} generic type
	 * @param sb {@link Appendable} instance to append to - may NOT be null
	 * @param a array data to be appended (may be NULL/empty)
	 * @param delim delimiter to be used between successive values (if '\0' then no delimiting)
	 * @param upperCase if TRUE then uses uppercase HEX digits
	 * @return same as input {@link Appendable} instance
	 * @throws IOException if failed to append or bad parameters
	 */
    public static final <A extends Appendable> A appendHex(final A sb, final int[] a, final char delim, final boolean upperCase) throws IOException {
        return appendHex(sb, a, 0, (null == a) ? 0 : a.length, delim, upperCase);
    }

    /**
	 * Appends specified value in HEX format
	 * @param <A> The {@link Appendable} generic type
	 * @param sb {@link Appendable} instance to append to - may NOT be null
	 * @param l value to be appended
	 * @param upperCase if TRUE then uses uppercase HEX digits
	 * @return same as input {@link Appendable} instance
	 * @throws IOException if failed to append or bad parameters
	 */
    public static final <A extends Appendable> A appendHex(final A sb, final long l, final boolean upperCase) throws IOException {
        appendHex(sb, (int) ((l >> 32) & 0x00000000FFFFFFFF), upperCase);
        appendHex(sb, (int) (l & 0x00000000FFFFFFFF), upperCase);
        return sb;
    }

    /**
	 * Appends specified array in HEX format
	 * @param <A> The {@link Appendable} generic type
	 * @param sb {@link Appendable} instance to append to - may NOT be null
	 * @param a data array to be appended
	 * @param nOffset offsrt within data array from where data is to be appended
	 * @param nLen number of elements to be appended starting at specified offset
	 * @param delim delimiter to be used between successive values (if '\0' then no delimiting)
	 * @param upperCase if TRUE then uses uppercase HEX digits
	 * @return same as input {@link Appendable} instance
	 * @throws IOException if failed to append or bad parameters
	 */
    public static final <A extends Appendable> A appendHex(final A sb, final long[] a, final int nOffset, final int nLen, final char delim, final boolean upperCase) throws IOException {
        for (int idx = 0, pos = nOffset; idx < nLen; idx++, pos++) {
            if ((idx > 0) && (delim != '\0')) sb.append(delim);
            appendHex(sb, a[pos], upperCase);
        }
        return sb;
    }

    /**
	 * Appends specified array in HEX format
	 * @param <A> The {@link Appendable} generic type
	 * @param sb {@link Appendable} instance to append to - may NOT be null
	 * @param a array data to be appended (may be NULL/empty)
	 * @param delim delimiter to be used between successive values (if '\0' then no delimiting)
	 * @param upperCase if TRUE then uses uppercase HEX digits
	 * @return same as input {@link Appendable} instance
	 * @throws IOException if failed to append or bad parameters
	 */
    public static final <A extends Appendable> A appendHex(final A sb, final long[] a, final char delim, final boolean upperCase) throws IOException {
        return appendHex(sb, a, 0, (null == a) ? 0 : a.length, delim, upperCase);
    }

    /**
	 * Converts array into HEX string (no spaces)
	 * @param b buffer to be converted - may NOT be null/empty
	 * @param startOffset offset from which to start - may NOT be negative
	 * @param len number of bytes to convert - MUST be POSITIVE
	 * @param delim delimiter to use - '\0' if none
	 * @param useUppercase if TRUE then uppercase HEX characters are used
	 * @return string conversion
	 * @throws IllegalArgumentException if bad/illegal buffer
	 * @throws IllegalStateException if unable to convert to HEX
	 */
    public static final String toString(byte[] b, int startOffset, int len, char delim, boolean useUppercase) throws IllegalArgumentException, IllegalStateException {
        if ((null == b) || (startOffset < 0) || (len <= 0) || ((startOffset + len) > b.length)) throw new IllegalArgumentException("Bad/Illegal input buffer");
        try {
            final StringBuilder sb = appendHex(new StringBuilder((len + 1) * 2), b, startOffset, len, delim, useUppercase);
            return sb.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to append HEX values", e);
        }
    }

    /**
	 * Converts array into HEX string (no spaces)
	 * @param b buffer to be converted - may NOT be null/empty
	 * @param delim delimiter to use - '\0' if none
	 * @param useUppercase if TRUE then uppercase HEX characters are used
	 * @return string conversion
	 * @throws IllegalArgumentException if bad/illegal buffer
	 * @throws IllegalStateException if unable to convert to HEX
	 * @see #toString(byte[], int, int, char, boolean)
	 */
    public static final String toString(byte[] b, char delim, boolean useUppercase) throws IllegalArgumentException, IllegalStateException {
        return toString(b, 0, (null == b) ? 0 : b.length, delim, useUppercase);
    }

    public static final void append(byte b, char[] ca, int startIndex, boolean useUppercase) {
        final char[] hexDigits = useUppercase ? upperHex : lowerHex;
        ca[startIndex] = hexDigits[(b >> 4) & 0x0F];
        ca[startIndex + 1] = hexDigits[b & 0x0F];
    }

    public static final void append(byte b, char[] ca, boolean useUppercase) {
        append(b, ca, 0, useUppercase);
    }

    public static final int MAX_HEX_DIGITS_PER_BYTE = 2;

    public static final char[] getChars(byte b, boolean useUppercase) {
        final char[] ca = new char[MAX_HEX_DIGITS_PER_BYTE];
        append(b, ca, useUppercase);
        return ca;
    }

    public static final String toString(byte b, boolean useUppercase) {
        return new String(getChars(b, useUppercase));
    }

    public static final String toString(Byte b, boolean useUpperCase) {
        return (null == b) ? null : toString(b.byteValue(), useUpperCase);
    }

    public static final byte rebuild(char hiChar, char loChar) throws NumberFormatException {
        final int hiPosUpper = upperHexDigitsString.indexOf(hiChar), hiPos = (hiPosUpper < 0) ? lowerHexDigitsString.indexOf(hiChar) : hiPosUpper, loPosUpper = upperHexDigitsString.indexOf(loChar), loPos = (loPosUpper < 0) ? lowerHexDigitsString.indexOf(loChar) : loPosUpper;
        if ((hiPos < 0) || (loPos < 0)) throw new NumberFormatException("Not HEX characters");
        final int nVal = ((hiPos << 4) & 0x00F0) + (loPos & 0x000F);
        return (byte) nVal;
    }

    public static final byte fromString(CharSequence cs, int offset, int len) throws NumberFormatException {
        if ((len < 2) || (offset < 0)) throw new NumberFormatException("fromString(" + cs + ")[" + offset + "/" + len + "] bad/Illegal input to convert to HEX");
        return rebuild(cs.charAt(offset), cs.charAt(offset + 1));
    }

    public static final byte fromString(CharSequence cs) throws NumberFormatException {
        return fromString(cs, 0, (null == cs) ? 0 : cs.length());
    }

    public static final byte[] toByteArray(final CharSequence cs, final int offset, final int len, final char delim) throws NumberFormatException {
        final int maxPos = offset + len;
        if ((offset < 0) || (len <= 0)) throw new NumberFormatException("toByteArray(" + cs + ")[" + offset + "-" + maxPos + "] Illegal range to decode");
        if (0 == len) return null;
        if ((null == cs) || ((maxPos > cs.length()))) throw new NumberFormatException("toByteArray(" + cs + ")[" + offset + "-" + maxPos + "] Illegal data values to decode");
        final int seqLen = getHexSequenceLength(cs, offset, len, delim);
        if (seqLen < 0) throw new NumberFormatException("toByteArray(" + cs + ")[" + offset + "-" + maxPos + "] Invalid HEX sequence at position=" + (0 - seqLen - 1));
        final byte[] out = new byte[seqLen];
        int outOffset = 0;
        for (int curPos = offset; curPos < maxPos; outOffset++) {
            final char hi = cs.charAt(curPos), lo = cs.charAt(curPos + 1);
            final int b = rebuild(hi, lo) & 0x00FF;
            out[outOffset] = (byte) b;
            if (delim != '\0') curPos += 3; else curPos += 2;
        }
        if (outOffset < out.length) throw new NumberFormatException("toByteArray(" + cs + ")[" + offset + "-" + maxPos + "] Incomplete HEX sequence");
        return out;
    }

    public static final byte[] toByteArray(CharSequence cs, char delim) {
        return toByteArray(cs, 0, (null == cs) ? 0 : cs.length(), delim);
    }

    public static final char UPC_HEX_MODIFIER = 'X', LWC_HEX_MODIFIER = 'x';

    public static final boolean isHexModifier(char c) {
        return (UPC_HEX_MODIFIER == c) || (LWC_HEX_MODIFIER == c);
    }

    public static final int MIN_HEX_PATTERN = 2 + MAX_HEX_DIGITS_PER_BYTE;

    /**
	 * @param cs The {@link CharSequence} to check
	 * @param offset The offset to check from
	 * @param len Available remaining data from the given offset
	 * @return <code>true</code> if the &quot;\xHH&quot; pattern is found in
	 * the sequence starting at the given offset
	 */
    public static final boolean isHexPattern(CharSequence cs, int offset, int len) {
        if ((null == cs) || (offset < 0) || (len < MIN_HEX_PATTERN)) return false;
        if ((offset + MIN_HEX_PATTERN) > cs.length()) return false;
        final char ch1 = cs.charAt(offset), ch2 = cs.charAt(offset + 1);
        if ((ch1 != '\\') || (!isHexModifier(ch2))) return false;
        return isHexDigit(cs.charAt(offset + 2)) && isHexDigit(cs.charAt(offset + 3));
    }

    public static final boolean isHexPattern(CharSequence cs) {
        return isHexPattern(cs, 0, (null == cs) ? 0 : cs.length());
    }

    public static final String translateControlCharacters(final String s) {
        final int sLen = (null == s) ? 0 : s.length();
        int lastPos = 0, curPos = (sLen <= 0) ? (-1) : s.indexOf('\\');
        StringBuilder sb = null;
        for (; (curPos >= lastPos) && (curPos < sLen); curPos = s.indexOf('\\', curPos)) {
            final int remLen = sLen - curPos;
            if (!isHexPattern(s, curPos, remLen)) {
                curPos++;
                continue;
            }
            final char ch = (char) (fromString(s, curPos + (MIN_HEX_PATTERN - MAX_HEX_DIGITS_PER_BYTE), MAX_HEX_DIGITS_PER_BYTE) & 0x00FF);
            if (null == sb) sb = new StringBuilder(sLen - MIN_HEX_PATTERN);
            if (curPos > lastPos) {
                final String ss = s.substring(lastPos, curPos);
                sb.append(ss);
            }
            sb.append(ch);
            curPos += MIN_HEX_PATTERN;
            if ((lastPos = curPos) >= sLen) break;
        }
        if ((null == sb) || (sb.length() <= 0)) return s;
        if (lastPos < sLen) {
            final String rem = s.substring(lastPos);
            sb.append(rem);
        }
        return sb.toString();
    }

    public static final String toString(final short v, final boolean useUppercase) {
        final byte[] data = { (byte) ((v >> 8) & 0x00FF), (byte) (v & 0x00FF) };
        return toString(data, '\0', useUppercase);
    }

    public static final String toString(final int v, final boolean useUppercase) {
        final byte[] data = { (byte) ((v >> 24) & 0x00FF), (byte) ((v >> 16) & 0x00FF), (byte) ((v >> 8) & 0x00FF), (byte) (v & 0x00FF) };
        return toString(data, '\0', useUppercase);
    }

    public static final String toString(final long v, final boolean useUppercase) {
        final byte[] data = { (byte) ((v >> 56) & 0x00FF), (byte) ((v >> 48) & 0x00FF), (byte) ((v >> 40) & 0x00FF), (byte) ((v >> 32) & 0x00FF), (byte) ((v >> 24) & 0x00FF), (byte) ((v >> 16) & 0x00FF), (byte) ((v >> 8) & 0x00FF), (byte) (v & 0x00FF) };
        return toString(data, '\0', useUppercase);
    }
}
