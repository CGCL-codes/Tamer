package org.yajul.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import junit.framework.TestCase;
import org.yajul.io.Base64Decoder;
import org.yajul.io.Base64Encoder;
import org.yajul.io.Base64FormatException;
import org.yajul.io.Base64InputStream;
import org.yajul.io.StreamCopier;

public class Base64Test extends TestCase {

    public Base64Test(String name) {
        super(name);
    }

    public void testString() throws Base64FormatException {
        String plain = "hello there!";
        String encoded = Base64Encoder.encode(plain);
        String decoded = Base64Decoder.decode(encoded);
        assertEquals(plain, decoded);
    }

    public void testOneByte() throws Base64FormatException, IOException {
        byte[] inputBytes = new byte[] { 42 };
        byte[] encoded = encode(inputBytes);
        assertEquals(4, encoded.length);
        byte[] decoded = decode(encoded);
        assertTrue(Arrays.equals(inputBytes, decoded));
    }

    public void testTwoBytes() throws Base64FormatException, IOException {
        byte[] inputBytes = new byte[] { 42, 43 };
        byte[] encoded = encode(inputBytes);
        assertEquals(4, encoded.length);
        byte[] decoded = decode(encoded);
        assertTrue(Arrays.equals(inputBytes, decoded));
    }

    public void testThreeBytes() throws Base64FormatException, IOException {
        byte[] inputBytes = new byte[] { 42, 43, 44 };
        byte[] encoded = encode(inputBytes);
        assertEquals(4, encoded.length);
        byte[] decoded = decode(encoded);
        assertTrue(Arrays.equals(inputBytes, decoded));
    }

    public void test128() throws Base64FormatException, IOException {
        for (int i = 1; i <= 128; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int j = 0; j < i; j++) {
                baos.write(j);
            }
            byte[] inputBytes = baos.toByteArray();
            byte[] encodedBytes = encode(inputBytes);
            assertTrue(!Arrays.equals(inputBytes, encodedBytes));
            assertTrue(inputBytes.length <= encodedBytes.length);
            byte[] decodedBytes = decode(encodedBytes);
            assertTrue(Arrays.equals(inputBytes, decodedBytes));
        }
    }

    private byte[] decode(byte[] encodedBytes) throws IOException, Base64FormatException {
        ByteArrayInputStream in;
        ByteArrayOutputStream out;
        in = new ByteArrayInputStream(encodedBytes);
        out = new ByteArrayOutputStream();
        Base64Decoder dec = new Base64Decoder(in, out);
        dec.process();
        byte[] decodedBytes = out.toByteArray();
        return decodedBytes;
    }

    private byte[] encode(byte[] inputBytes) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Base64Encoder enc = new Base64Encoder(in, out);
        enc.process();
        byte[] encodedBytes = out.toByteArray();
        return encodedBytes;
    }

    public void testInputStream() throws Base64FormatException, IOException {
        String plain = "hello there!";
        String encoded = Base64Encoder.encode(plain);
        Base64InputStream in = new Base64InputStream(new ByteArrayInputStream(encoded.getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamCopier.copy(in, out);
        String decoded = new String(out.toByteArray());
        System.out.println(decoded);
    }
}
