package net.sf.commonstringutil;

/**
 * 
 * @author Natalino Nugeraha
 * @version 1.0.0
 * @category internal utility
 */
final class FBS {

    private final transient long[] bits;

    private final transient int sz;

    private final transient long numBits;

    /**
	 * 
	 * @param numBit
	 */
    FBS(final long numBit) {
        this.numBits = numBit;
        this.bits = new long[(int) (((numBits - 1) >>> 6) + 1)];
        this.sz = bits.length;
    }

    /**
	 * 
	 * @param idx
	 * @return
	 */
    final boolean get(int idx) {
        assert idx >= 0 && idx < numBits;
        int i = idx >> 6;
        int bit = idx & 0x3f;
        long bitmask = 1L << bit;
        return (bits[i] & bitmask) != 0;
    }

    /**
	 * 
	 * @param idx
	 * @return
	 */
    final boolean get(long idx) {
        assert idx >= 0 && idx < numBits;
        int i = (int) (idx >> 6);
        int bit = (int) idx & 0x3f;
        long bitmask = 1L << bit;
        return (bits[i] & bitmask) != 0;
    }

    /**
	 * 
	 * @param idx
	 */
    final void set(final int idx) {
        assert idx >= 0 && idx < numBits;
        int wordNum = idx >> 6;
        int bit = idx & 0x3f;
        long bitmask = 1L << bit;
        bits[wordNum] |= bitmask;
    }

    /**
	 * 
	 * @param idx
	 */
    final void set(final long idx) {
        assert idx >= 0 && idx < numBits;
        int wordNum = (int) (idx >> 6);
        int bit = (int) idx & 0x3f;
        long bitmask = 1L << bit;
        bits[wordNum] |= bitmask;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FBS)) {
            return false;
        }
        FBS a;
        FBS b = (FBS) o;
        if (b.sz > this.sz) {
            a = b;
            b = this;
        } else {
            a = this;
        }
        for (int i = a.sz - 1; i >= b.sz; i--) {
            if (a.bits[i] != 0) {
                return false;
            }
        }
        for (int i = b.sz - 1; i >= 0; i--) {
            if (a.bits[i] != b.bits[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final int hashCode() {
        long h = 0;
        for (int i = bits.length; --i >= 0; ) {
            h ^= bits[i];
            h = (h << 1) | (h >>> 63);
        }
        return (int) ((h >> 32) ^ h) + 0x98761234;
    }
}
