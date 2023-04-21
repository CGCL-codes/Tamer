package com.tomgibara.crinch.bits;

import java.math.BigInteger;

/**
 * A convenient base class for creating {@link BitWriter} implementations.
 * Implementations MUST implement either {@link #writeBit(int)} or
 * {@link #write(int, int)}, SHOULD implement {@link #getPosition()} where
 * practical and MAY override any other methods as necessary, say to improve
 * performance.
 * 
 * @author Tom Gibara
 * 
 */
public abstract class AbstractBitWriter implements BitWriter {

    @Override
    public int writeBit(int bit) {
        return write(bit, 1);
    }

    @Override
    public int writeBoolean(boolean bit) {
        return writeBit(bit ? 1 : 0);
    }

    @Override
    public long writeBooleans(boolean value, long count) {
        if (count == 0) return 0;
        final int bits = value ? -1 : 0;
        if (count <= 32) return write(bits, (int) count);
        int c = 0;
        while (count > 32) {
            c += write(bits, 32);
            count -= 32;
        }
        return c;
    }

    @Override
    public int write(int bits, int count) {
        if (count < 0) throw new IllegalArgumentException("negative count");
        if (count > 32) throw new IllegalArgumentException("count too great");
        if (count == 0) return 0;
        int c = 0;
        for (count--; count >= 0; count--) {
            c += writeBit(bits >>> count);
        }
        return c;
    }

    @Override
    public int write(long bits, int count) {
        if (count < 0) throw new IllegalArgumentException("negative count");
        if (count > 64) throw new IllegalArgumentException("count too great");
        if (count <= 32) {
            return write((int) bits, count);
        } else {
            return write((int) (bits >> 32), count - 32) + write((int) bits, 32);
        }
    }

    @Override
    public int write(BigInteger bits, int count) {
        if (count < 0) throw new IllegalArgumentException("negative count");
        if (count <= 32) return write(bits.intValue(), count);
        if (count <= 64) return write(bits.longValue(), count);
        int c = 0;
        for (count--; count >= 0; count--) {
            c += writeBoolean(bits.testBit(count));
        }
        return c;
    }

    public int flush() {
        return 0;
    }

    @Override
    public int padToBoundary(BitBoundary boundary) throws BitStreamException {
        if (boundary == null) throw new IllegalArgumentException("null boundary");
        int bits = bitsToBoundary(boundary);
        if (bits == 0) return 0;
        return (int) writeBooleans(false, bits);
    }

    @Override
    public long getPosition() {
        return -1L;
    }

    int bitsToBoundary(BitBoundary boundary) {
        long position = getPosition();
        if (position < 0) throw new UnsupportedOperationException("writer does not support position");
        return -(int) position & boundary.mask;
    }
}
