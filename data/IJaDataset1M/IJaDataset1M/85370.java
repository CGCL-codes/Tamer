package net.sf.performancepacks.io;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shuyang Zhou
 */
public class UnsyncFilterOutputStreamTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Unit test for " + UnsyncFilterOutputStream.class.getName());
    }

    @Test
    public void testBlockWrite() throws IOException {
        UnsyncByteArrayOutputStream unsyncByteArrayOutputStream = new UnsyncByteArrayOutputStream();
        UnsyncFilterOutputStream unsyncFilterOutputStream = new UnsyncFilterOutputStream(unsyncByteArrayOutputStream);
        unsyncFilterOutputStream.write(_BUFFER, 0, 0);
        unsyncFilterOutputStream.flush();
        assertEquals(0, unsyncByteArrayOutputStream.size());
        unsyncFilterOutputStream.write(_BUFFER, 0, -1);
        unsyncFilterOutputStream.flush();
        assertEquals(0, unsyncByteArrayOutputStream.size());
        unsyncFilterOutputStream.write(_BUFFER);
        unsyncFilterOutputStream.flush();
        assertEquals(_BUFFER_SIZE, unsyncByteArrayOutputStream.size());
        assertTrue(Arrays.equals(_BUFFER, unsyncByteArrayOutputStream.toByteArray()));
    }

    @Test
    public void testClose() throws IOException {
        final AtomicBoolean flag = new AtomicBoolean(false);
        new UnsyncFilterOutputStream(new UnsyncByteArrayOutputStream() {

            public void close() throws IOException {
                flag.set(true);
            }

            public void flush() throws IOException {
                throw new IOException();
            }
        }).close();
        assertTrue(flag.get());
        flag.set(false);
        new UnsyncFilterOutputStream(new UnsyncByteArrayOutputStream() {

            public void close() throws IOException {
                flag.set(true);
            }

            public void flush() throws IOException {
            }
        }).close();
        assertTrue(flag.get());
    }

    @Test
    public void testWrite() throws IOException {
        UnsyncByteArrayOutputStream unsyncByteArrayOutputStream = new UnsyncByteArrayOutputStream();
        UnsyncFilterOutputStream unsyncFilterOutputStream = new UnsyncFilterOutputStream(unsyncByteArrayOutputStream);
        for (int i = 0; i < _BUFFER_SIZE; i++) {
            unsyncFilterOutputStream.write(i);
            assertEquals(i + 1, unsyncByteArrayOutputStream.size());
        }
        assertTrue(Arrays.equals(_BUFFER, unsyncByteArrayOutputStream.toByteArray()));
    }

    private static final int _BUFFER_SIZE = 64;

    private static final byte[] _BUFFER = new byte[_BUFFER_SIZE];

    static {
        for (int i = 0; i < _BUFFER_SIZE; i++) {
            _BUFFER[i] = (byte) i;
        }
    }
}
