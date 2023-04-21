package com.wowd.wobly.inheritance;

import java.nio.ByteBuffer;
import com.wowd.wobly.Wobly;
import com.wowd.wobly.WoblyUtils.Buffers;
import com.wowd.wobly.exceptions.WoblyReadException;

public class WoblyCompressedReaderHandler<T extends Wobly> extends AbstractWoblyHandler<T> {

    @Override
    protected byte getTypeID(ByteBuffer buf) {
        if (buf.remaining() <= 0) throw new WoblyReadException();
        int startPos = buf.position();
        Buffers.getVInt(buf);
        byte b = buf.get();
        buf.position(startPos);
        return b;
    }
}
