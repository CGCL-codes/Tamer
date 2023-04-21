package org.asyncj.buffers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class BoundedBuffer implements Buffer {

    public static final int BUFFER_SIZE = 8096;

    final ByteBuffer buffer;

    public BoundedBuffer() {
        this(BUFFER_SIZE);
    }

    public BoundedBuffer(int size) {
        this(ByteBuffer.allocateDirect(size));
    }

    public BoundedBuffer(ByteBuffer aBuffer) {
        super();
        buffer = aBuffer;
    }

    public boolean isEmpty() {
        return false == buffer.hasRemaining();
    }

    public long writeTo(WritableByteChannel channel) throws IOException {
        buffer.flip();
        return channel.write(buffer);
    }

    public long readFrom(ReadableByteChannel channel) throws IOException {
        buffer.clear();
        return channel.read(buffer);
    }

    public byte[] getBytes() {
        byte[] data = new byte[buffer.position()];
        buffer.flip();
        buffer.get(data);
        return data;
    }

    public void write(byte[] bytes) {
        buffer.put(bytes);
    }

    public void clear() {
        buffer.clear();
    }
}
