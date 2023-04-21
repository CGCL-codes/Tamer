package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.Fast;
import org.openfast.codec.LongCodec;

public class NullableSignedLongCodec extends StopBitEncodedTypeCodec implements LongCodec {

    public long decode(ByteBuffer buffer) {
        int value = FastTypeCodecs.SIGNED_INTEGER.decode(buffer);
        if (value > 0) return value - 1;
        return value;
    }

    public int encode(byte[] buffer, int offset, long value) {
        if (value == Long.MAX_VALUE) {
            buffer[offset] = 1;
            buffer[offset + 1] = 0;
            buffer[offset + 2] = 0;
            buffer[offset + 3] = 0;
            buffer[offset + 4] = 0;
            buffer[offset + 5] = 0;
            buffer[offset + 6] = 0;
            buffer[offset + 7] = 0;
            buffer[offset + 8] = 0;
            buffer[offset + 9] = Fast.STOP_BIT;
            return offset + 10;
        }
        if (value >= 0) return FastTypeCodecs.SIGNED_LONG.encode(buffer, offset, value + 1);
        return FastTypeCodecs.SIGNED_LONG.encode(buffer, offset, value);
    }

    public boolean isNull(ByteBuffer buffer) {
        return buffer.get(0) == Fast.NULL;
    }
}
