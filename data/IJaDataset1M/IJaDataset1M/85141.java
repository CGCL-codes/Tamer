package com.faunos.util.io;

import java.nio.ByteBuffer;

/**
 * Variable width integral value reader and writer.
 * <h4>Attribution</h4>
 * Contains derivative work from the Apache Lucene project, which is licensed under
 * the <a href="http://www.apache.org/licenses/LICENSE-2.0.html">Apache License</a>.
 */
public class VInt {

    public static long readLong(ByteBuffer buffer) {
        byte b = buffer.get();
        long value = b & 0x7F;
        for (int shift = 7; (b & 0x80) != 0; shift += 7) {
            b = buffer.get();
            value |= (b & 0x7FL) << shift;
        }
        return value;
    }

    public static void writeLong(ByteBuffer buffer, long value) {
        while ((value & ~0x7F) != 0) {
            buffer.put((byte) ((value & 0x7f) | 0x80));
            value >>>= 7;
        }
        buffer.put((byte) value);
    }

    public static int sizeOf(long value) {
        int size = 1;
        while ((value & ~0x7F) != 0) {
            ++size;
            value >>>= 7;
        }
        return size;
    }

    private VInt() {
    }
}
