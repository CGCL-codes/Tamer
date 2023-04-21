package com.googlecode.compress_j2me.gzip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.Assert;

public class AssertiveOutputStream extends OutputStream {

    private ByteArrayOutputStream baos;

    private byte[] expected;

    private int offset;

    public AssertiveOutputStream(byte[] expected) {
        this.baos = new ByteArrayOutputStream(expected.length);
        this.expected = expected;
    }

    @Override
    public void write(int b) throws IOException {
        int actualByte = 0xFF & b;
        int expectedByte = 0xFF & this.expected[this.offset];
        if (expectedByte != actualByte) {
            Assert.assertEquals("offset=" + this.offset, expectedByte, actualByte);
        }
        this.offset++;
        this.baos.write(b);
    }

    public byte[] toByteArray() {
        return this.baos.toByteArray();
    }

    public int expectedSize() {
        return this.expected.length;
    }

    public int size() {
        return this.baos.size();
    }
}
