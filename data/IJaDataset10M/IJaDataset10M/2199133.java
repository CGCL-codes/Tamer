package sun.io;

import sun.nio.cs.ext.ISCII91;

/**
 * Converter class. Converts between Unicode encoding and ISCII91 encoding.
 * ISCII91 is the character encoding as defined in Indian Standard document
 * IS 13194:1991 ( Indian Script Code for Information Interchange ).
 *
 * @see sun.io.ByteToCharConverter
 */
public class ByteToCharISCII91 extends ByteToCharConverter {

    private static final char[] directMapTable = ISCII91.getDirectMapTable();

    private static final char NUKTA_CHAR = '़';

    private static final char HALANT_CHAR = '्';

    private static final char ZWNJ_CHAR = '‌';

    private static final char ZWJ_CHAR = '‍';

    private static final char INVALID_CHAR = '￿';

    private char contextChar = INVALID_CHAR;

    private boolean needFlushing = false;

    /**
 * Converts ISCII91 characters to Unicode.
 * @see sun.io.ByteToCharConverter#convert
 */
    public int convert(byte input[], int inStart, int inEnd, char output[], int outStart, int outEnd) throws ConversionBufferFullException, UnknownCharacterException {
        charOff = outStart;
        byteOff = inStart;
        while (byteOff < inEnd) {
            if (charOff >= outEnd) {
                throw new ConversionBufferFullException();
            }
            int index = input[byteOff++];
            index = (index < 0) ? (index + 255) : index;
            char currentChar = directMapTable[index];
            if (contextChar == '�') {
                output[charOff++] = '�';
                contextChar = INVALID_CHAR;
                needFlushing = false;
                continue;
            }
            switch(currentChar) {
                case 'ँ':
                case 'इ':
                case 'ई':
                case 'ऋ':
                case 'ि':
                case 'ी':
                case 'ृ':
                case '।':
                    if (needFlushing) {
                        output[charOff++] = contextChar;
                        contextChar = currentChar;
                        continue;
                    }
                    contextChar = currentChar;
                    needFlushing = true;
                    continue;
                case NUKTA_CHAR:
                    switch(contextChar) {
                        case 'ँ':
                            output[charOff] = 'ॐ';
                            break;
                        case 'इ':
                            output[charOff] = 'ऌ';
                            break;
                        case 'ई':
                            output[charOff] = 'ॡ';
                            break;
                        case 'ऋ':
                            output[charOff] = 'ॠ';
                            break;
                        case 'ि':
                            output[charOff] = 'ॢ';
                            break;
                        case 'ी':
                            output[charOff] = 'ॣ';
                            break;
                        case 'ृ':
                            output[charOff] = 'ॄ';
                            break;
                        case '।':
                            output[charOff] = 'ऽ';
                            break;
                        case HALANT_CHAR:
                            if (needFlushing) {
                                output[charOff++] = contextChar;
                                contextChar = currentChar;
                                continue;
                            }
                            output[charOff] = ZWJ_CHAR;
                            break;
                        default:
                            if (needFlushing) {
                                output[charOff++] = contextChar;
                                contextChar = currentChar;
                                continue;
                            }
                            output[charOff] = NUKTA_CHAR;
                    }
                    break;
                case HALANT_CHAR:
                    if (needFlushing) {
                        output[charOff++] = contextChar;
                        contextChar = currentChar;
                        continue;
                    }
                    if (contextChar == HALANT_CHAR) {
                        output[charOff] = ZWNJ_CHAR;
                        break;
                    }
                    output[charOff] = HALANT_CHAR;
                    break;
                case INVALID_CHAR:
                    if (needFlushing) {
                        output[charOff++] = contextChar;
                        contextChar = currentChar;
                        continue;
                    }
                    if (subMode) {
                        output[charOff] = subChars[0];
                        break;
                    } else {
                        contextChar = INVALID_CHAR;
                        throw new UnknownCharacterException();
                    }
                default:
                    if (needFlushing) {
                        output[charOff++] = contextChar;
                        contextChar = currentChar;
                        continue;
                    }
                    output[charOff] = currentChar;
                    break;
            }
            contextChar = currentChar;
            needFlushing = false;
            charOff++;
        }
        return charOff - outStart;
    }

    /**
 * @see sun.io.ByteToCharConverter#flush
 */
    public int flush(char[] output, int outStart, int outEnd) throws MalformedInputException, ConversionBufferFullException {
        int charsWritten = 0;
        if (needFlushing) {
            output[outStart] = contextChar;
            charsWritten = 1;
        }
        contextChar = INVALID_CHAR;
        needFlushing = false;
        byteOff = charOff = 0;
        return charsWritten;
    }

    /**
 * Returns the character set id for the conversion.
 */
    public String getCharacterEncoding() {
        return "ISCII91";
    }

    /**
 * @see sun.io.ByteToCharConverter#reset
 */
    public void reset() {
        byteOff = charOff = 0;
    }
}
