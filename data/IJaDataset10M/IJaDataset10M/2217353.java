package org.hydracache.io;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;

/**
 * @author nzhu
 * 
 */
public class BufferTest {

    private static final int DATA_LENGTH = 250;

    @Test
    public void testWrap() {
        Buffer buffer = Buffer.wrap(new byte[DATA_LENGTH]);
        assertEquals("Wrap result is incorrect", DATA_LENGTH, buffer.size());
    }

    @Test
    public void testAsDataOutputAndInputStream() throws IOException {
        Buffer buffer = Buffer.allocate();
        buffer.asDataOutpuStream().write(new byte[DATA_LENGTH]);
        assertEquals("Output length is incorrect", DATA_LENGTH, buffer.size());
        assertEquals("Input length is incorrect", DATA_LENGTH, buffer.size());
    }
}
