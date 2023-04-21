package org.apache.harmony.nio.tests.java.nio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DirectShortBufferTest extends ShortBufferTest {

    public void setUp() {
        buf = ByteBuffer.allocateDirect(BUFFER_LENGTH * 2).asShortBuffer();
        loadTestData1(buf);
        baseBuf = buf;
    }

    public void tearDown() {
        buf = null;
        baseBuf = null;
    }

    public void testHasArray() {
        assertFalse(buf.hasArray());
    }

    public void testArray() {
        try {
            buf.array();
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
    }

    public void testArrayOffset() {
        try {
            buf.arrayOffset();
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
    }

    public void testIsDirect() {
        assertTrue(buf.isDirect());
    }

    public void testOrder() {
        assertEquals(ByteOrder.BIG_ENDIAN, buf.order());
    }
}
