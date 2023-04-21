package org.apache.harmony.nio.tests.java.nio;

import java.nio.CharBuffer;

public class ReadOnlyHeapCharBufferTest extends ReadOnlyCharBufferTest {

    protected void setUp() throws Exception {
        super.setUp();
        buf = CharBuffer.allocate(BUFFER_LENGTH);
        super.loadTestData1(buf);
        buf = buf.asReadOnlyBuffer();
        baseBuf = buf;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
