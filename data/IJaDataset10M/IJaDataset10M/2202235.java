package com.googlecode.dni.test.type;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import com.googlecode.dni.DirectNativeInterface;
import com.googlecode.dni.internal.Testing;
import com.googlecode.dni.type.AutoFree;
import com.googlecode.dni.type.NativeString;

/**
 * Tests the charset handling.
 *
 * @author Matthew Wilson
 */
public final class TestCharsets {

    static {
        Testing.init();
    }

    private static final String HEX = "0123456789abcdef";

    private final AutoFree autoFree = new AutoFree();

    /** Tests handling of ASCII strings (accelerated). */
    @Test
    public void ascii() {
        test("US-ASCII", 1);
    }

    /** Tests handling of ISO-8859-1 strings (accelerated). */
    @Test
    public void iso_8559_1() {
        test("ISO-8859-1", 1);
    }

    /** Tests handling of Windows-1252 strings (accelerated). */
    @Test
    public void windows1252() {
        test("Windows-1252", 1);
    }

    /** Tests handling of UTF-8 strings (accelerated). */
    @Test
    public void utf8() {
        test("UTF-8", 1);
    }

    /** Tests handling of UTF-16 strings (accelerated). */
    @Test
    public void utf16() {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            test("UTF-16LE", 2);
        } else {
            test("UTF-16BE", 2);
        }
    }

    /**
     * Called after the tests.
     */
    @After
    public void after() {
        this.autoFree.free();
    }

    private void test(final String charsetName, final int unitSize) {
        Charset charset = Charset.forName(charsetName);
        testString(charsetName, charset, unitSize, "");
        testString(charsetName, charset, unitSize, "Hello, World!");
        char[] allAscii = new char[127];
        for (int i = 0; i < allAscii.length; i++) {
            allAscii[i] = (char) (i + 1);
        }
        testString(charsetName, charset, unitSize, new String(allAscii));
        char[] allIso8859_1 = new char[255];
        for (int i = 0; i < allIso8859_1.length; i++) {
            allIso8859_1[i] = (char) (i + 1);
        }
        testString(charsetName, charset, unitSize, new String(allIso8859_1));
        char[] allUnicodeBmp = new char[0x10000 - 0x800 - 3];
        for (int i = 1; i < 0xd800; i++) {
            allUnicodeBmp[i - 1] = (char) i;
        }
        for (int i = 0xe000; i < 0xfffe; i++) {
            allUnicodeBmp[i - 0x801] = (char) i;
        }
        testString(charsetName, charset, unitSize, new String(allUnicodeBmp));
    }

    private void testString(final String charsetName, final Charset charset, final int unitSize, final String string) {
        if (string.indexOf('\0') >= 0) {
            throw new AssertionError("Bad input string for test (bad test)!");
        }
        NativeString nativeString = DirectNativeInterface.wrapString(string, charsetName);
        this.autoFree.add(nativeString);
        ByteBuffer expectedEncoded = charset.encode(string);
        int encodedLength = expectedEncoded.remaining();
        ByteBuffer actualEncoded = nativeString.pointer().getBuffer(encodedLength);
        Assert.assertEquals("Encoded string: " + string, toHexString(expectedEncoded), toHexString(actualEncoded));
        ByteBuffer terminator = nativeString.pointer().add(encodedLength).getBuffer(unitSize);
        while (terminator.hasRemaining()) {
            Assert.assertEquals("Terminated string: " + string, 0, terminator.get());
        }
        String actualDecoded = nativeString.read(charsetName);
        CharBuffer expectedDecoded = charset.decode(expectedEncoded);
        Assert.assertEquals("Decoded string", toHexString(expectedDecoded.toString()), toHexString(actualDecoded));
    }

    private static String toHexString(final ByteBuffer buffer) {
        if (buffer.remaining() == 0) {
            return "";
        }
        char[] string = new char[buffer.remaining() * 3 - 1];
        for (int i = 0; i < buffer.remaining(); i++) {
            byte b = buffer.get(buffer.position() + i);
            string[i * 3] = HEX.charAt((b >> 4) & 0xf);
            string[i * 3 + 1] = HEX.charAt(b & 0xf);
        }
        return new String(string);
    }

    private static String toHexString(final String input) {
        if (input.isEmpty()) {
            return "";
        }
        char[] string = new char[input.length() * 5 - 1];
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            string[i * 5] = HEX.charAt((c >> 12) & 0xf);
            string[i * 5 + 1] = HEX.charAt((c >> 8) & 0xf);
            string[i * 5 + 2] = HEX.charAt((c >> 4) & 0xf);
            string[i * 5 + 3] = HEX.charAt(c & 0xf);
        }
        return new String(string);
    }
}
