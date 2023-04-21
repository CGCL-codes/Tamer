package org.openconcerto.utils.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

public class CharsetHelper {

    private byte buffer[];

    private Charset defaultCharset;

    private boolean enforce8Bit;

    public CharsetHelper(byte buffer[]) {
        enforce8Bit = false;
        this.buffer = buffer;
        defaultCharset = getDefaultSystemCharset();
    }

    public CharsetHelper(byte buffer[], Charset defaultCharset) {
        enforce8Bit = false;
        this.buffer = buffer;
        setDefaultCharset(defaultCharset);
    }

    public void setDefaultCharset(Charset defaultCharset) {
        if (defaultCharset != null) this.defaultCharset = defaultCharset; else this.defaultCharset = getDefaultSystemCharset();
    }

    public void setEnforce8Bit(boolean enforce) {
        enforce8Bit = enforce;
    }

    public boolean getEnforce8Bit() {
        return enforce8Bit;
    }

    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    public Charset guessEncoding() {
        if (hasUTF8Bom(buffer)) return Charset.forName("UTF-8");
        if (hasUTF16LEBom(buffer)) return Charset.forName("UTF-16LE");
        if (hasUTF16BEBom(buffer)) return Charset.forName("UTF-16BE");
        boolean highOrderBit = false;
        boolean validU8Char = true;
        int length = buffer.length;
        int i = 0;
        do {
            if (i >= length - 6) break;
            byte b0 = buffer[i];
            byte b1 = buffer[i + 1];
            byte b2 = buffer[i + 2];
            byte b3 = buffer[i + 3];
            byte b4 = buffer[i + 4];
            byte b5 = buffer[i + 5];
            if (b0 < 0) {
                highOrderBit = true;
                if (isTwoBytesSequence(b0)) {
                    if (!isContinuationChar(b1)) validU8Char = false; else i++;
                } else if (isThreeBytesSequence(b0)) {
                    if (!isContinuationChar(b1) || !isContinuationChar(b2)) validU8Char = false; else i += 2;
                } else if (isFourBytesSequence(b0)) {
                    if (!isContinuationChar(b1) || !isContinuationChar(b2) || !isContinuationChar(b3)) validU8Char = false; else i += 3;
                } else if (isFiveBytesSequence(b0)) {
                    if (!isContinuationChar(b1) || !isContinuationChar(b2) || !isContinuationChar(b3) || !isContinuationChar(b4)) validU8Char = false; else i += 4;
                } else if (isSixBytesSequence(b0)) {
                    if (!isContinuationChar(b1) || !isContinuationChar(b2) || !isContinuationChar(b3) || !isContinuationChar(b4) || !isContinuationChar(b5)) validU8Char = false; else i += 5;
                } else {
                    validU8Char = false;
                }
            }
            if (!validU8Char) break;
            i++;
        } while (true);
        if (!highOrderBit) if (enforce8Bit) return defaultCharset; else return Charset.forName("US-ASCII");
        if (validU8Char) return Charset.forName("UTF-8"); else return defaultCharset;
    }

    public int getBomSize() {
        if (hasUTF8Bom(buffer)) return 3;
        return !hasUTF16LEBom(buffer) && !hasUTF16BEBom(buffer) ? 0 : 2;
    }

    public static Charset guessEncoding(File f, int bufferLength) throws FileNotFoundException, IOException {
        return guessEncoding(f, bufferLength, null);
    }

    public static Charset guessEncoding(File f, int bufferLength, Charset defaultCharset) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(f);
        byte buffer[] = new byte[bufferLength];
        fis.read(buffer);
        fis.close();
        CharsetHelper toolkit = new CharsetHelper(buffer);
        if (defaultCharset != null) {
            toolkit.setDefaultCharset(defaultCharset);
        }
        return toolkit.guessEncoding();
    }

    private static boolean isContinuationChar(byte b) {
        return -128 <= b && b <= -65;
    }

    private static boolean isTwoBytesSequence(byte b) {
        return -64 <= b && b <= -33;
    }

    private static boolean isThreeBytesSequence(byte b) {
        return -32 <= b && b <= -17;
    }

    private static boolean isFourBytesSequence(byte b) {
        return -16 <= b && b <= -9;
    }

    private static boolean isFiveBytesSequence(byte b) {
        return -8 <= b && b <= -5;
    }

    private static boolean isSixBytesSequence(byte b) {
        return -4 <= b && b <= -3;
    }

    public static Charset getDefaultSystemCharset() {
        return Charset.forName(System.getProperty("file.encoding"));
    }

    private static boolean hasUTF8Bom(byte bom[]) {
        return bom[0] == -17 && bom[1] == -69 && bom[2] == -65;
    }

    private static boolean hasUTF16LEBom(byte bom[]) {
        return bom[0] == -1 && bom[1] == -2;
    }

    private static boolean hasUTF16BEBom(byte bom[]) {
        return bom[0] == -2 && bom[1] == -1;
    }

    public static Charset[] getAvailableCharsets() {
        final Collection<Charset> collection = Charset.availableCharsets().values();
        return collection.toArray(new Charset[collection.size()]);
    }
}
