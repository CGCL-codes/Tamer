package com.ingenico.tools.nio;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class ShortBuffer extends SampleBuffer {

    private static final Logger _logger = Logger.getLogger(ShortBuffer.class.getCanonicalName());

    public ShortBuffer(ByteBuffer buffer) {
        super(buffer, 2);
    }

    @Override
    public Number get() {
        Number pipo = (buffer.getShort() & 0xFFFF);
        _logger.fine("Short Buffer reads = " + pipo);
        return pipo;
    }
}
