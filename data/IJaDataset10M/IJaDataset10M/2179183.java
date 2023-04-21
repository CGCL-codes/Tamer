package org.apache.harmony.nio.tests.java.nio;

import java.nio.IntBuffer;
import java.nio.ReadOnlyBufferException;

public class ReadOnlyIntBufferTest extends IntBufferTest {

    protected void setUp() throws Exception {
        super.setUp();
        buf = buf.asReadOnlyBuffer();
        baseBuf = buf;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testIsReadOnly() {
        assertTrue(buf.isReadOnly());
    }

    public void testHasArray() {
        assertFalse(buf.hasArray());
    }

    public void testArray() {
        try {
            buf.array();
            fail("Should throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        }
    }

    public void testHashCode() {
        IntBuffer duplicate = buf.duplicate();
        assertEquals(buf.hashCode(), duplicate.hashCode());
    }

    public void testArrayOffset() {
        try {
            buf.arrayOffset();
            fail("Should throw Exception");
        } catch (UnsupportedOperationException e) {
        }
    }

    public void testCompact() {
        try {
            buf.compact();
            fail("Should throw Exception");
        } catch (ReadOnlyBufferException e) {
        }
    }

    public void testPutint() {
        try {
            buf.put(0);
            fail("Should throw Exception");
        } catch (ReadOnlyBufferException e) {
        }
    }

    public void testPutintArray() {
        int array[] = new int[1];
        try {
            buf.put(array);
            fail("Should throw Exception");
        } catch (ReadOnlyBufferException e) {
        }
        try {
            buf.put((int[]) null);
            fail("Should throw Exception");
        } catch (NullPointerException e) {
        }
    }

    public void testPutintArrayintint() {
        int array[] = new int[1];
        try {
            buf.put(array, 0, array.length);
            fail("Should throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        }
        try {
            buf.put((int[]) null, -1, 1);
            fail("Should throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        }
        try {
            buf.put(new int[buf.capacity() + 1], 0, buf.capacity() + 1);
            fail("Should throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        }
        try {
            buf.put(array, -1, array.length);
            fail("Should throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        }
    }

    public void testPutIntBuffer() {
        IntBuffer other = IntBuffer.allocate(1);
        try {
            buf.put(other);
            fail("Should throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        }
        try {
            buf.put((IntBuffer) null);
            fail("Should throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        }
        try {
            buf.put(buf);
            fail("Should throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        }
    }

    public void testPutintint() {
        try {
            buf.put(0, (int) 0);
            fail("Should throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        }
        try {
            buf.put(-1, (int) 0);
            fail("Should throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        }
    }
}
