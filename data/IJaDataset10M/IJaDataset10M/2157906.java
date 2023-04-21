package com.siemens.ct.exi.values;

public class BinaryHexValue extends AbstractBinaryValue {

    private static final long serialVersionUID = 4914135099644891193L;

    private int lengthData;

    public BinaryHexValue(byte[] bytes) {
        super(ValueType.BINARY_HEX, bytes);
    }

    public static BinaryHexValue parse(String val) {
        byte[] bytes = decode(val);
        if (bytes == null) {
            return null;
        } else {
            return new BinaryHexValue(bytes);
        }
    }

    public int getCharactersLength() {
        if (slen == -1) {
            lengthData = bytes.length;
            slen = lengthData * 2;
        }
        return slen;
    }

    public char[] toCharacters(char[] cbuffer, int offset) {
        getCharactersLength();
        int temp;
        for (int i = 0; i < lengthData; i++) {
            temp = bytes[i];
            if (temp < 0) temp += 256;
            cbuffer[offset + i * 2] = lookUpHexAlphabet[temp >> 4];
            cbuffer[offset + i * 2 + 1] = lookUpHexAlphabet[temp & 0xf];
        }
        return cbuffer;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof BinaryHexValue) {
            return _equals(((BinaryHexValue) o).bytes);
        } else {
            BinaryHexValue bv = BinaryHexValue.parse(o.toString());
            return bv == null ? false : _equals(bv.bytes);
        }
    }

    @Override
    public int hashCode() {
        int hc = 0;
        for (byte b : bytes) {
            hc = (hc * 31) ^ b;
        }
        return hc;
    }

    private static final int BASELENGTH = 128;

    private static final int LOOKUPLENGTH = 16;

    private static final byte[] hexNumberTable = new byte[BASELENGTH];

    private static final char[] lookUpHexAlphabet = new char[LOOKUPLENGTH];

    static {
        for (int i = 0; i < BASELENGTH; i++) {
            hexNumberTable[i] = -1;
        }
        for (int i = '9'; i >= '0'; i--) {
            hexNumberTable[i] = (byte) (i - '0');
        }
        for (int i = 'F'; i >= 'A'; i--) {
            hexNumberTable[i] = (byte) (i - 'A' + 10);
        }
        for (int i = 'f'; i >= 'a'; i--) {
            hexNumberTable[i] = (byte) (i - 'a' + 10);
        }
        for (int i = 0; i < 10; i++) {
            lookUpHexAlphabet[i] = (char) ('0' + i);
        }
        for (int i = 10; i <= 15; i++) {
            lookUpHexAlphabet[i] = (char) ('A' + i - 10);
        }
    }

    /**
	 * Decode hex string to a byte array
	 * 
	 * @param encoded
	 *            encoded string
	 * @return return array of byte to encode
	 */
    public static byte[] decode(String encoded) {
        if (encoded == null) return null;
        int lengthData = encoded.length();
        if (lengthData % 2 != 0) return null;
        char[] binaryData = encoded.toCharArray();
        int lengthDecode = lengthData / 2;
        byte[] decodedData = new byte[lengthDecode];
        byte temp1, temp2;
        char tempChar;
        for (int i = 0; i < lengthDecode; i++) {
            tempChar = binaryData[i * 2];
            temp1 = (tempChar < BASELENGTH) ? hexNumberTable[tempChar] : -1;
            if (temp1 == -1) return null;
            tempChar = binaryData[i * 2 + 1];
            temp2 = (tempChar < BASELENGTH) ? hexNumberTable[tempChar] : -1;
            if (temp2 == -1) return null;
            decodedData[i] = (byte) ((temp1 << 4) | temp2);
        }
        return decodedData;
    }
}
