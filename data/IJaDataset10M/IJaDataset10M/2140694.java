package edu.rabbit.kernel.memory;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author Yuanyan<yanyan.cao@gmail.com>
 * 
 * 
 */
public class DirectByteBuffer extends DbByteBuffer {

    public DirectByteBuffer() {
    }

    public DirectByteBuffer(ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    public void allocate(int size) {
        assert (size > 0);
        buffer = ByteBuffer.allocateDirect(size);
    }

    @Override
    public byte[] asArray() {
        final byte[] b = new byte[buffer.remaining()];
        getBytes(0, b, 0, b.length);
        return b;
    }

    @Override
    public void fill(int from, int count, byte value) {
        final byte[] b = new byte[count];
        Arrays.fill(b, value);
        putBytes(from, b, 0, count);
    }

    @Override
    public void getBytes(int pointer, byte[] bytes, int to, int count) {
        final int position = buffer.position();
        try {
            buffer.position(pointer);
            buffer.get(bytes, to, count);
        } finally {
            buffer.position(position);
        }
    }

    @Override
    public void putBytes(int pointer, byte[] bytes, int from, int count) {
        final int position = buffer.position();
        try {
            buffer.position(pointer);
            buffer.put(bytes, from, count);
        } finally {
            buffer.position(position);
        }
    }
}
