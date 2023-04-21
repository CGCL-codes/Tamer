package com.wowd.wobly.unknown;

import java.nio.ByteBuffer;
import com.wowd.wobly.WoblyUtils.Buffers;

public class BytesSizeCompressedUnknownField extends UnknownField {

    private final int size;

    private final byte[] bytes;

    public BytesSizeCompressedUnknownField(ByteBuffer buf, int tag) {
        super(tag);
        size = Buffers.getVInt(buf);
        bytes = new byte[size];
        buf.get(bytes);
    }

    @Override
    public int getSize() {
        return Buffers.sizeVInt(tag) + Buffers.sizeVInt(bytes.length) + bytes.length;
    }

    @Override
    public void write(ByteBuffer buf) {
        Buffers.putVInt(buf, tag);
        Buffers.putVInt(buf, size);
        buf.put(bytes);
    }
}
