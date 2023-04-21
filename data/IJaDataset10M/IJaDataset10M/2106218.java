package org.openfast.codec;

import java.nio.ByteBuffer;

public interface IntegerCodec extends TypeCodec {

    int decode(ByteBuffer buffer);

    /**
     * Encodes the value into the FAST encoded buffer
     * 
     * @param buffer the destination for fast encoded bytes
     * @param offset the index in the buffer to continue encoding
     * @param value the value to encode
     * @return the new offset in the buffer
     */
    int encode(byte[] buffer, int offset, int value);
}
