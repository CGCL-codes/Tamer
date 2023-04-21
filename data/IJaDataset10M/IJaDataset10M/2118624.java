package org.dicom4j.core.utils;

import org.dolmen.core.lang.HexaDecimalUtils;

/**
 * <p>
 * Various static methods helpful for manipulating arrays of bytes and
 * extracting values from them.
 * </p>
 * 
 * @author dclunie
 */
public class ByteArray {

    private static final String identString = "@(#) $Header: /userland/cvs/pixelmed/imgbook/com/pixelmed/utils/ByteArray.java,v 1.13 2003/10/19 17:39:59 dclunie Exp $";

    /**
	 * <p>
	 * Extract an unsigned big endian integer from an entire byte array.
	 * </p>
	 * 
	 * @param b
	 *          the byte array which is the big endian encoded integer
	 * @return the unsigned integer value
	 */
    public static final int bigEndianToUnsignedInt(byte[] b) {
        return bigEndianToUnsignedInt(b, 0, b.length);
    }

    /**
	 * <p>
	 * Extract an unsigned big endian integer from a byte array.
	 * </p>
	 * 
	 * @param b
	 *          the byte array containing the big endian encoded integer
	 * @param offset
	 *          the offset of the start of the integer in the byte array
	 * @param length
	 *          the number of bytes that the integer occupies in the byte array
	 * @return the unsigned integer value
	 */
    public static final int bigEndianToUnsignedInt(byte[] b, int offset, int length) {
        int v = 0;
        while (length-- > 0) {
            v = v << 8 | b[offset++] & 0xff;
        }
        return v;
    }

    /**
	 * <p>
	 * Concatenate the entire contents of two byte arrays into a new byte array.
	 * </p>
	 * 
	 * @param src1
	 *          the first byte array
	 * @param src2
	 *          the second byte array
	 * @return a new byte array containing the bytes extracted from the first
	 *         array followed by the bytes extracted from the second array
	 */
    public static final byte[] concatenate(byte[] src1, byte[] src2) {
        return src1 == null ? src2 : src2 == null ? src1 : concatenate(src1, 0, src1.length, src2, 0, src2.length);
    }

    /**
	 * <p>
	 * Concatenate portions of two byte arrays into a new byte array.
	 * </p>
	 * 
	 * @param src1
	 *          the first byte array
	 * @param offset1
	 *          the offset of the bytes to be extracted from the first byte array
	 * @param length1
	 *          the number of bytes to be extracted from the first byte array
	 * @param src2
	 *          the second byte array
	 * @param offset2
	 *          the offset of the bytes to be extracted from the second byte array
	 * @param length2
	 *          the number of bytes to be extracted from the second byte array
	 * @return a new byte array containing the bytes extracted from the first
	 *         array followed by the bytes extracted from the second array
	 */
    public static final byte[] concatenate(byte[] src1, int offset1, int length1, byte[] src2, int offset2, int length2) {
        byte[] dst = new byte[length1 + length2];
        System.arraycopy(src1, offset1, dst, 0, length1);
        System.arraycopy(src2, offset2, dst, length1, length2);
        return dst;
    }

    private static final String dump(byte[] src) {
        return HexaDecimalUtils.dump(src);
    }

    /**
	 * <p>
	 * Extract a portion of a byte array into a new byte array.
	 * </p>
	 * 
	 * @param src
	 *          the source byte array
	 * @param offset
	 *          the offset of the bytes to be extracted
	 * @param length
	 *          the number of bytes to be extracted
	 * @return a new byte array containing the extracted bytes
	 */
    public static final byte[] extractBytes(byte[] src, int offset, int length) {
        byte[] dst = new byte[length];
        System.arraycopy(src, offset, dst, 0, length);
        return dst;
    }

    /**
	 * <p>
	 * Testing.
	 * </p>
	 * 
	 * @param arg
	 *          ignored
	 */
    public static void main(String arg[]) {
        int[] wordLengths = { 1, 2, 4, 8 };
        byte[] bytes = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        for (int i = 0; i < wordLengths.length; ++i) {
            int wordLength = wordLengths[i];
            System.err.println("swapEndianness: wordLength = " + wordLength);
            System.err.print("Before: " + dump(bytes));
            swapEndianness(bytes, bytes.length, wordLength);
            System.err.print("After:  " + dump(bytes));
            swapEndianness(bytes, bytes.length, wordLength);
            System.err.print("Back:   " + dump(bytes));
            System.err.println();
        }
    }

    /**
	 * <p>
	 * Swap the byte order (endianness) of fixed length words within a byte array.
	 * </p>
	 * 
	 * @param src
	 *          the byte array (swapped in place)
	 * @param byteCount
	 *          the number of bytes in the array to swap
	 * @param wordLength
	 *          the length in bytes of each word
	 */
    public static final void swapEndianness(byte[] src, int byteCount, int wordLength) {
        int startOfWord = 0;
        int endOfWord = wordLength - 1;
        int iterationsWithinWord = wordLength / 2;
        int countWithinWord = iterationsWithinWord;
        while (startOfWord < byteCount) {
            byte tmp = src[startOfWord];
            src[startOfWord] = src[endOfWord];
            src[endOfWord] = tmp;
            --endOfWord;
            if (--countWithinWord <= 0) {
                countWithinWord = iterationsWithinWord;
                startOfWord += iterationsWithinWord;
                endOfWord = startOfWord + wordLength;
            }
            ++startOfWord;
        }
    }

    private ByteArray() {
    }
}
