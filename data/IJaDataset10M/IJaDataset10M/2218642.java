package org.jcvi.trace.sanger.chromatogram.scf.position;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * <code>BytePositionStrategy</code> is the implementation
 * of {@link PositionStrategy} that encodes positions
 * as a single byte.
 * @author dkatzel
 *
 *
 */
public class BytePositionStrategy implements PositionStrategy {

    @Override
    public short getPosition(DataInputStream in) throws IOException {
        return in.readByte();
    }

    /**
     * Max allowed value is the maximum value of a <code>byte</code>.
     * @return {@link Byte#MAX_VALUE}
     */
    @Override
    public int getMaxAllowedValue() {
        return Byte.MAX_VALUE;
    }

    @Override
    public void setPosition(short position, ByteBuffer buffer) {
        if (position > Byte.MAX_VALUE) {
            throw new IllegalArgumentException("position to put is too big :" + position);
        }
        buffer.put((byte) position);
    }

    @Override
    public byte getSampleSize() {
        return 1;
    }
}
