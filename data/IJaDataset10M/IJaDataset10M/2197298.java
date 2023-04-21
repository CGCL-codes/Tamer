package sun.io;

import sun.nio.cs.ext.ISCII91;

public class CharToByteISCII91 extends CharToByteConverter {

    private static final byte NO_CHAR = (byte) 255;

    private static final byte[] directMapTable = ISCII91.getEncoderMappingTable();

    private static final char NUKTA_CHAR = '़';

    private static final char HALANT_CHAR = '्';

    /**
 * @return true for Devanagari and ASCII range and for the special characters
 *              Zero Width Joiner and Zero Width Non-Joiner
 * @see sun.io.CharToByteConverter#canConvert
 *
 */
    public boolean canConvert(char ch) {
        return ((ch >= 0x0900 && ch <= 0x097f) || (ch == 0x200d || ch == 0x200c) || (ch >= 0x0000 && ch <= 0x007f));
    }

    /**
 * Converts both Devanagari and ASCII range of characters.
 * @see sun.io.CharToByteConverter#convert
 */
    public int convert(char[] input, int inStart, int inEnd, byte[] output, int outStart, int outEnd) throws MalformedInputException, UnknownCharacterException, ConversionBufferFullException {
        charOff = inStart;
        byteOff = outStart;
        for (; charOff < inEnd; charOff++) {
            char inputChar = input[charOff];
            int index = Integer.MIN_VALUE;
            boolean isSurrogatePair = false;
            if (inputChar >= 0x0000 && inputChar <= 0x007f) {
                if (byteOff >= outEnd) {
                    throw new ConversionBufferFullException();
                }
                output[byteOff++] = (byte) inputChar;
                continue;
            }
            if (inputChar == 0x200c) {
                inputChar = HALANT_CHAR;
            } else if (inputChar == 0x200d) {
                inputChar = NUKTA_CHAR;
            }
            if (inputChar >= 0x0900 && inputChar <= 0x097f) {
                index = ((int) (inputChar) - 0x0900) * 2;
            } else if (inputChar >= 0xd800 && inputChar <= 0xdbff) {
                if (charOff < inEnd - 1) {
                    char nextChar = input[charOff];
                    if (nextChar >= 0xdc00 && nextChar <= 0xdfff) {
                        charOff++;
                        isSurrogatePair = true;
                    }
                }
                if (!isSurrogatePair) {
                    badInputLength = 1;
                    throw new MalformedInputException();
                }
            } else if (inputChar >= 0xdc00 && inputChar <= 0xdfff) {
                badInputLength = 1;
                throw new MalformedInputException();
            }
            if (index == Integer.MIN_VALUE || directMapTable[index] == NO_CHAR) {
                if (subMode) {
                    if (byteOff + subBytes.length >= outEnd) {
                        throw new ConversionBufferFullException();
                    }
                    System.arraycopy(subBytes, 0, output, byteOff, subBytes.length);
                    byteOff += subBytes.length;
                } else {
                    badInputLength = isSurrogatePair ? 2 : 1;
                    throw new UnknownCharacterException();
                }
            } else {
                if (byteOff >= outEnd) {
                    throw new ConversionBufferFullException();
                }
                output[byteOff++] = directMapTable[index++];
                if (directMapTable[index] != NO_CHAR) {
                    if (byteOff >= outEnd) {
                        throw new ConversionBufferFullException();
                    }
                    output[byteOff++] = directMapTable[index];
                }
            }
        }
        return byteOff - outStart;
    }

    /**
* @see sun.io.CharToByteConverter#flush
*/
    public int flush(byte[] output, int outStart, int outEnd) throws MalformedInputException, ConversionBufferFullException {
        byteOff = charOff = 0;
        return 0;
    }

    /**
 * @return The character encoding as a String.
 */
    public String getCharacterEncoding() {
        return "ISCII91";
    }

    /**
 * @see sun.io.CharToByteConverter#getMaxBytesPerChar
 */
    public int getMaxBytesPerChar() {
        return 2;
    }

    /**
 * @see sun.io.CharToByteConverter#reset
 */
    public void reset() {
        byteOff = charOff = 0;
    }
}
