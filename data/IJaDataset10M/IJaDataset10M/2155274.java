package com.triplyx.volume;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;

public class CiphertextStripeInputStreamTest extends TestCase {

    public void testReadsInput() throws IOException {
        InputStream stripedInputStream = new ByteArrayInputStream(stripedData);
        InputStream csis = new CiphertextStripeInputStream(8, stripedInputStream);
        byte[] buffer = new byte[512];
        int bytesRead = csis.read(buffer);
        assertEquals(cipherText.length, bytesRead);
        for (int i = 0; i < cipherText.length; i++) {
            assertEquals(cipherText[i], buffer[i]);
        }
        assertEquals(-1, csis.read());
        assertEquals(-1, csis.read());
    }

    /**
	 * This test checks that a BufferedInputStream is safe to use as an input.
	 * BufferedInputStream will skip fewer bytes than asked for if the buffer
	 * does not have sufficient bytes remaining. (So-called incomplete skips.)
	 * 
	 * This behaviour could be mistaken for end-of-file, but isn't.
	 */
    public void testReadsInputWithIncompleteSkips() throws IOException {
        InputStream stripedInputStream = new ByteArrayInputStream(stripedData);
        InputStream bsis = new BrokenSkipInputStream(stripedInputStream);
        InputStream csis = new CiphertextStripeInputStream(8, bsis);
        byte[] buffer = new byte[512];
        int bytesRead = csis.read(buffer);
        assertEquals(cipherText.length, bytesRead);
        for (int i = 0; i < cipherText.length; i++) {
            assertEquals(cipherText[i], buffer[i]);
        }
        assertEquals(-1, csis.read());
        assertEquals(-1, csis.read());
    }

    /**
	 * Skips only 5 bytes when called, which shouldn't match any stripe size.
	 * Used to test that incomplete skips will not break the downstream caller.
	 */
    private class BrokenSkipInputStream extends InputStream {

        private InputStream source;

        public BrokenSkipInputStream(InputStream source) {
            this.source = source;
        }

        @Override
        public int read() throws IOException {
            return source.read();
        }

        @Override
        public long skip(long n) throws IOException {
            return source.skip(Math.min(n, 5L));
        }
    }

    ;

    public void testRejectsNullInputStream() {
        try {
            new CiphertextStripeInputStream(8, null);
            fail("Did not throw NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    public void testRejectsZeroStripeSize() {
        try {
            new CiphertextStripeInputStream(0, new ByteArrayInputStream(stripedData));
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testRejectsNegativeStripeSize() {
        try {
            new CiphertextStripeInputStream(-1, new ByteArrayInputStream(stripedData));
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    private final byte[] cipherText = { (byte) 0x37, (byte) 0x0b, (byte) 0x12, (byte) 0xa6, (byte) 0xe2, (byte) 0xb8, (byte) 0xf8, (byte) 0x30, (byte) 0x91, (byte) 0x0e, (byte) 0xa7, (byte) 0x79, (byte) 0xea, (byte) 0x8d, (byte) 0xa4, (byte) 0x8e };

    private final byte[] stripedData = { (byte) 0x37, (byte) 0x0b, (byte) 0x12, (byte) 0xa6, (byte) 0xe2, (byte) 0xb8, (byte) 0xf8, (byte) 0x30, (byte) 0xf2, (byte) 0x3f, (byte) 0xfe, (byte) 0xd6, (byte) 0xaf, (byte) 0xc4, (byte) 0x82, (byte) 0x52, (byte) 0x91, (byte) 0x0e, (byte) 0xa7, (byte) 0x79, (byte) 0xea, (byte) 0x8d, (byte) 0xa4, (byte) 0x8e, (byte) 0x6b, (byte) 0x4d, (byte) 0x52, (byte) 0xe0, (byte) 0x4a, (byte) 0xa9, (byte) 0x01, (byte) 0xd8 };
}
