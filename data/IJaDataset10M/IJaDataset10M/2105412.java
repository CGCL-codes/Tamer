package com.google.zxing.common;

import org.junit.Assert;
import org.junit.Test;
import java.nio.charset.Charset;

public final class StringUtilsTestCase extends Assert {

    @Test
    public void testShortShiftJIS_1() {
        doTest(new byte[] { (byte) 0x8b, (byte) 0xe0, (byte) 0x8b, (byte) 0x9b }, "SJIS");
    }

    @Test
    public void testShortISO88591_1() {
        doTest(new byte[] { (byte) 0x62, (byte) 0xe5, (byte) 0x64 }, "ISO-8859-1");
    }

    @Test
    public void testMixedShiftJIS_1() {
        doTest(new byte[] { (byte) 0x48, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x20, (byte) 0x8b, (byte) 0xe0, (byte) 0x21 }, "SJIS");
    }

    private static void doTest(byte[] bytes, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        String guessedName = StringUtils.guessEncoding(bytes, null);
        Charset guessedEncoding = Charset.forName(guessedName);
        assertEquals(charset, guessedEncoding);
    }

    /**
   * Utility for printing out a string in given encoding as a Java statement, since it's better
   * to write that into the Java source file rather than risk character encoding issues in the 
   * source file itself
   */
    public static void main(String[] args) {
        String text = args[0];
        Charset charset = Charset.forName(args[1]);
        StringBuilder declaration = new StringBuilder();
        declaration.append("new byte[] { ");
        for (byte b : text.getBytes(charset)) {
            declaration.append("(byte) 0x");
            declaration.append(Integer.toHexString(b & 0xFF));
            declaration.append(", ");
        }
        declaration.append('}');
        System.out.println(declaration);
    }
}
