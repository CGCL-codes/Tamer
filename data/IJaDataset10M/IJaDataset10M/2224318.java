package sun.io;

public class CharToByteASCII extends CharToByteConverter {

    public String getCharacterEncoding() {
        return "ASCII";
    }

    private char highHalfZoneCode;

    public int flush(byte[] output, int outStart, int outEnd) throws MalformedInputException {
        if (highHalfZoneCode != 0) {
            highHalfZoneCode = 0;
            throw new MalformedInputException("String ends with <High Half Zone code> of UTF16");
        }
        byteOff = charOff = 0;
        return 0;
    }

    public int convert(char[] input, int inOff, int inEnd, byte[] output, int outOff, int outEnd) throws MalformedInputException, UnknownCharacterException, ConversionBufferFullException {
        char inputChar;
        int outputSize;
        charOff = inOff;
        byteOff = outOff;
        if (highHalfZoneCode != 0) {
            inputChar = highHalfZoneCode;
            highHalfZoneCode = 0;
            if (input[inOff] >= 0xdc00 && input[inOff] <= 0xdfff) {
                badInputLength = 1;
                throw new UnknownCharacterException();
            } else {
                badInputLength = 0;
                throw new MalformedInputException("Previous converted string ends with " + "<High Half Zone Code> of UTF16 " + ", but this string is not begin with <Low Half Zone>");
            }
        }
        while (charOff < inEnd) {
            inputChar = input[charOff];
            if (inputChar <= '') {
                if (byteOff + 1 > outEnd) throw new ConversionBufferFullException();
                output[byteOff++] = (byte) inputChar;
                charOff += 1;
            } else if (inputChar >= '?' && inputChar <= '?') {
                if (charOff + 1 == inEnd) {
                    highHalfZoneCode = inputChar;
                    break;
                }
                inputChar = input[charOff + 1];
                if (inputChar >= '?' && inputChar <= '?') {
                    if (subMode) {
                        outputSize = subBytes.length;
                        if (byteOff + outputSize > outEnd) throw new ConversionBufferFullException();
                        System.arraycopy(subBytes, 0, output, byteOff, outputSize);
                        byteOff += outputSize;
                        charOff += 2;
                    } else {
                        badInputLength = 2;
                        throw new UnknownCharacterException();
                    }
                } else {
                    badInputLength = 1;
                    throw new MalformedInputException();
                }
            } else if (inputChar >= '?' && inputChar <= '?') {
                badInputLength = 1;
                throw new MalformedInputException();
            } else {
                if (subMode) {
                    outputSize = subBytes.length;
                    if (byteOff + outputSize > outEnd) throw new ConversionBufferFullException();
                    System.arraycopy(subBytes, 0, output, byteOff, outputSize);
                    byteOff += outputSize;
                    charOff += 1;
                } else {
                    badInputLength = 1;
                    throw new UnknownCharacterException();
                }
            }
        }
        return byteOff - outOff;
    }

    public boolean canConvert(char ch) {
        return (ch <= '');
    }

    public void reset() {
        byteOff = charOff = 0;
        highHalfZoneCode = 0;
    }

    /**
     * returns the maximum number of bytes needed to convert a char
     */
    public int getMaxBytesPerChar() {
        return 1;
    }
}
