package org.eclipse.core.internal.preferences;

public class Base64 {

    private static final byte equalSign = (byte) '=';

    static char digits[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

    /**
	 * This method decodes the byte array in base 64 encoding into a char array
	 * Base 64 encoding has to be according to the specification given by the
	 * RFC 1521 (5.2).
	 * 
	 * @param data the encoded byte array
	 * @return the decoded byte array
	 */
    public static byte[] decode(byte[] data) {
        if (data.length == 0) return data;
        int lastRealDataIndex = data.length - 1;
        while (data[lastRealDataIndex] == equalSign) lastRealDataIndex--;
        int padBytes = data.length - 1 - lastRealDataIndex;
        int byteLength = data.length * 6 / 8 - padBytes;
        byte[] result = new byte[byteLength];
        int dataIndex = 0;
        int resultIndex = 0;
        int allBits = 0;
        int resultChunks = (lastRealDataIndex + 1) / 4;
        for (int i = 0; i < resultChunks; i++) {
            allBits = 0;
            for (int j = 0; j < 4; j++) allBits = (allBits << 6) | decodeDigit(data[dataIndex++]);
            for (int j = resultIndex + 2; j >= resultIndex; j--) {
                result[j] = (byte) (allBits & 0xff);
                allBits = allBits >>> 8;
            }
            resultIndex += 3;
        }
        switch(padBytes) {
            case 1:
                allBits = 0;
                for (int j = 0; j < 3; j++) allBits = (allBits << 6) | decodeDigit(data[dataIndex++]);
                allBits = allBits << 6;
                allBits = allBits >>> 8;
                for (int j = resultIndex + 1; j >= resultIndex; j--) {
                    result[j] = (byte) (allBits & 0xff);
                    allBits = allBits >>> 8;
                }
                break;
            case 2:
                allBits = 0;
                for (int j = 0; j < 2; j++) allBits = (allBits << 6) | decodeDigit(data[dataIndex++]);
                allBits = allBits << 6;
                allBits = allBits << 6;
                allBits = allBits >>> 8;
                allBits = allBits >>> 8;
                result[resultIndex] = (byte) (allBits & 0xff);
                break;
        }
        return result;
    }

    /**
	 * This method converts a Base 64 digit to its numeric value.
	 * 
	 * @param data digit (character) to convert
	 * @return value for the digit
	 */
    static int decodeDigit(byte data) {
        char charData = (char) data;
        if (charData <= 'Z' && charData >= 'A') return charData - 'A';
        if (charData <= 'z' && charData >= 'a') return charData - 'a' + 26;
        if (charData <= '9' && charData >= '0') return charData - '0' + 52;
        switch(charData) {
            case '+':
                return 62;
            case '/':
                return 63;
            default:
                throw new IllegalArgumentException("Invalid char to decode: " + data);
        }
    }

    /**
	 * This method encodes the byte array into a char array in base 64 according
	 * to the specification given by the RFC 1521 (5.2).
	 * 
	 * @param data the encoded char array
	 * @return the byte array that needs to be encoded
	 */
    public static byte[] encode(byte[] data) {
        int sourceChunks = data.length / 3;
        int len = ((data.length + 2) / 3) * 4;
        byte[] result = new byte[len];
        int extraBytes = data.length - (sourceChunks * 3);
        int dataIndex = 0;
        int resultIndex = 0;
        int allBits = 0;
        for (int i = 0; i < sourceChunks; i++) {
            allBits = 0;
            for (int j = 0; j < 3; j++) allBits = (allBits << 8) | (data[dataIndex++] & 0xff);
            for (int j = resultIndex + 3; j >= resultIndex; j--) {
                result[j] = (byte) digits[(allBits & 0x3f)];
                allBits = allBits >>> 6;
            }
            resultIndex += 4;
        }
        switch(extraBytes) {
            case 1:
                allBits = data[dataIndex++];
                allBits = allBits << 8;
                allBits = allBits << 8;
                for (int j = resultIndex + 3; j >= resultIndex; j--) {
                    result[j] = (byte) digits[(allBits & 0x3f)];
                    allBits = allBits >>> 6;
                }
                result[result.length - 1] = (byte) '=';
                result[result.length - 2] = (byte) '=';
                break;
            case 2:
                allBits = data[dataIndex++];
                allBits = (allBits << 8) | (data[dataIndex++] & 0xff);
                allBits = allBits << 8;
                for (int j = resultIndex + 3; j >= resultIndex; j--) {
                    result[j] = (byte) digits[(allBits & 0x3f)];
                    allBits = allBits >>> 6;
                }
                result[result.length - 1] = (byte) '=';
                break;
        }
        return result;
    }
}
