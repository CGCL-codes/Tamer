package org.firebirdsql.encodings;

public class Encoding_Cp1251 extends Encoding_OneByte {

    private static char[] defaultByteToChar = new char[256];

    private static byte[] defaultCharToByte = new byte[256 * 256];

    ;

    static {
        Initialize("Cp1251", defaultByteToChar, defaultCharToByte);
    }

    private char[] byteToChar;

    private byte[] charToByte;

    public Encoding_Cp1251() {
        byteToChar = defaultByteToChar;
        charToByte = defaultCharToByte;
    }

    public Encoding_Cp1251(char[] charMapping) {
        byteToChar = new char[256];
        charToByte = new byte[256 * 256];
        Initialize("Cp1251", byteToChar, charToByte, charMapping);
    }

    public int encodeToCharset(char[] in, int off, int len, byte[] out) {
        return super.encodeToCharset(charToByte, in, off, len, out);
    }

    public int decodeFromCharset(byte[] in, int off, int len, char[] out) {
        return super.decodeFromCharset(byteToChar, in, off, len, out);
    }
}
