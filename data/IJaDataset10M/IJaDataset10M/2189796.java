package org.apache.harmony.luni.tests.java.io;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import junit.framework.TestCase;

public class ReaderTest extends TestCase {

    public void test_Reader_CharBuffer_null() throws IOException {
        String s = "MY TEST STRING";
        MockReader mockReader = new MockReader(s.toCharArray());
        CharBuffer charBuffer = null;
        try {
            mockReader.read(charBuffer);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    public void test_Reader_CharBuffer_ZeroChar() throws IOException {
        String s = "MY TEST STRING";
        char[] srcBuffer = s.toCharArray();
        MockReader mockReader = new MockReader(srcBuffer);
        CharBuffer charBuffer = CharBuffer.allocate(0);
        int result = mockReader.read(charBuffer);
        assertEquals(0, result);
        char[] destBuffer = new char[srcBuffer.length];
        mockReader.read(destBuffer);
        assertEquals(s, String.valueOf(destBuffer));
    }

    public void test_Reader_CharBufferChar() throws IOException {
        String s = "MY TEST STRING";
        char[] srcBuffer = s.toCharArray();
        final int CHARBUFFER_SIZE = 10;
        MockReader mockReader = new MockReader(srcBuffer);
        CharBuffer charBuffer = CharBuffer.allocate(CHARBUFFER_SIZE);
        charBuffer.append('A');
        final int CHARBUFFER_REMAINING = charBuffer.remaining();
        int result = mockReader.read(charBuffer);
        assertEquals(CHARBUFFER_REMAINING, result);
        charBuffer.rewind();
        assertEquals(s.substring(0, CHARBUFFER_REMAINING), charBuffer.subSequence(CHARBUFFER_SIZE - CHARBUFFER_REMAINING, CHARBUFFER_SIZE).toString());
        char[] destBuffer = new char[srcBuffer.length - CHARBUFFER_REMAINING];
        mockReader.read(destBuffer);
        assertEquals(s.substring(CHARBUFFER_REMAINING), String.valueOf(destBuffer));
    }

    /**
     * @tests {@link java.io.Reader#mark(int)}
     */
    public void test_mark() {
        MockReader mockReader = new MockReader();
        try {
            mockReader.mark(0);
            fail("Should throw IOException for Reader do not support mark");
        } catch (IOException e) {
        }
    }

    /**
     * @tests {@link java.io.Reader#read()}
     */
    public void test_read() throws IOException {
        MockReader reader = new MockReader();
        assertEquals("Should be equal to -1", -1, reader.read());
        String string = "MY TEST STRING";
        char[] srcBuffer = string.toCharArray();
        MockReader mockReader = new MockReader(srcBuffer);
        for (char c : srcBuffer) {
            assertEquals("Should be equal to \'" + c + "\'", c, mockReader.read());
        }
        mockReader.read();
        assertEquals("Should be equal to -1", -1, reader.read());
    }

    /**
     * @tests {@link java.io.Reader#ready()}
     */
    public void test_ready() throws IOException {
        MockReader mockReader = new MockReader();
        assertFalse("Should always return false", mockReader.ready());
    }

    /**
     * @tests {@link java.io.Reader#reset()}
     */
    public void test_reset() {
        MockReader mockReader = new MockReader();
        try {
            mockReader.reset();
            fail("Should throw IOException");
        } catch (IOException e) {
        }
    }

    /**
     * @tests {@link java.io.Reader#skip(long)}
     */
    public void test_skip() throws IOException {
        String string = "MY TEST STRING";
        char[] srcBuffer = string.toCharArray();
        int length = srcBuffer.length;
        MockReader mockReader = new MockReader(srcBuffer);
        assertEquals("Should be equal to \'M\'", 'M', mockReader.read());
        mockReader.skip(length / 2);
        assertEquals("Should be equal to \'S\'", 'S', mockReader.read());
        mockReader.skip(length);
        try {
            mockReader.skip(-1);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    class MockReader extends Reader {

        private char[] contents;

        private int current_offset = 0;

        private int length = 0;

        public MockReader() {
            super();
        }

        public MockReader(char[] data) {
            contents = data;
            length = contents.length;
        }

        @Override
        public void close() throws IOException {
            contents = null;
        }

        @Override
        public int read(char[] buf, int offset, int count) throws IOException {
            if (null == contents) {
                return -1;
            }
            if (length <= current_offset) {
                return -1;
            }
            if (buf.length < offset + count) {
                throw new IndexOutOfBoundsException();
            }
            count = Math.min(count, length - current_offset);
            for (int i = 0; i < count; i++) {
                buf[offset + i] = contents[current_offset + i];
            }
            current_offset += count;
            return count;
        }
    }
}
