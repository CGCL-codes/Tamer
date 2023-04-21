package org.jcvi.common.core.seq.read.trace.sanger.chromat.scf.header.pos;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * <code>ShortPositionStrategy</code> is the implementation
 * of {@link PositionStrategy} that encodes positions
 * as a short (2 bytes).
 * @author dkatzel
 *
 *
 */
public class ShortPositionStrategy implements PositionStrategy {

    @Override
    public short getPosition(DataInputStream in) throws IOException {
        return (short) in.readUnsignedShort();
    }

    /**
     * Max allowed value is the maximum value of a <code>short</code>.
     * @return {@link Short#MAX_VALUE}
     */
    @Override
    public int getMaxAllowedValue() {
        return Short.MAX_VALUE;
    }

    @Override
    public void setPosition(short position, ByteBuffer buffer) {
        buffer.putShort(position);
    }

    @Override
    public byte getSampleSize() {
        return 2;
    }
}
