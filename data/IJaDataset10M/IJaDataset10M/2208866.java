package Util.Random;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

public class MersenneTwisterFast implements Serializable, Cloneable, RandomNumberGenerator {

    private static final int N = 624;

    private static final int M = 397;

    private static final int MATRIX_A = 0x9908b0df;

    private static final int UPPER_MASK = 0x80000000;

    private static final int LOWER_MASK = 0x7fffffff;

    private static final int TEMPERING_MASK_B = 0x9d2c5680;

    private static final int TEMPERING_MASK_C = 0xefc60000;

    private int mt[];

    private int mti;

    private int mag01[];

    private double __nextNextGaussian;

    private boolean __haveNextNextGaussian;

    public Object clone() throws CloneNotSupportedException {
        MersenneTwisterFast f = (MersenneTwisterFast) (super.clone());
        f.mt = mt.clone();
        f.mag01 = mag01.clone();
        return f;
    }

    public boolean stateEquals(Object o) {
        if (o == this) return true;
        if (o == null || !(o instanceof MersenneTwisterFast)) return false;
        MersenneTwisterFast other = (MersenneTwisterFast) o;
        if (mti != other.mti) return false;
        for (int x = 0; x < mag01.length; x++) if (mag01[x] != other.mag01[x]) return false;
        for (int x = 0; x < mt.length; x++) if (mt[x] != other.mt[x]) return false;
        return true;
    }

    /** Reads the entire state of the MersenneTwister RNG from the stream
     *  @param stream input stream
     *  @throws IOException exception from stream reading
     */
    public void readState(DataInputStream stream) throws IOException {
        int len = mt.length;
        for (int x = 0; x < len; x++) mt[x] = stream.readInt();
        len = mag01.length;
        for (int x = 0; x < len; x++) mag01[x] = stream.readInt();
        mti = stream.readInt();
        __nextNextGaussian = stream.readDouble();
        __haveNextNextGaussian = stream.readBoolean();
    }

    /** Writes the entire state of the MersenneTwister RNG to the stream
     *  @param stream output stream
     *  @throws IOException exception from stream reading
     */
    public void writeState(DataOutputStream stream) throws IOException {
        int len = mt.length;
        for (int x = 0; x < len; x++) stream.writeInt(mt[x]);
        len = mag01.length;
        for (int x = 0; x < len; x++) stream.writeInt(mag01[x]);
        stream.writeInt(mti);
        stream.writeDouble(__nextNextGaussian);
        stream.writeBoolean(__haveNextNextGaussian);
    }

    /**
     * Constructor using the default seed.
     */
    public MersenneTwisterFast() {
        this(System.currentTimeMillis());
    }

    /**
     * Constructor using a given seed.  Though you pass this seed in
     * as a long, it's best to make sure it's actually an integer.
     * @param seed seed
     */
    public MersenneTwisterFast(final long seed) {
        setSeed(seed);
    }

    /**
     * Constructor using an array of integers as seed.
     * Your array must have a non-zero length.  Only the first 624 integers
     * in the array are used; if the array is shorter than this then
     * integers are repeatedly used in a wrap-around fashion.
     * @param array seed array
     */
    public MersenneTwisterFast(final int[] array) {
        setSeed(array);
    }

    /**
     * Initalize the pseudo random number generator.  Don't
     * pass in a long that's bigger than an int (Mersenne Twister
     * only uses the first 32 bits for its seed).
     */
    public synchronized void setSeed(final long seed) {
        __haveNextNextGaussian = false;
        mt = new int[N];
        mag01 = new int[2];
        mag01[0] = 0x0;
        mag01[1] = MATRIX_A;
        mt[0] = (int) (seed);
        for (mti = 1; mti < N; mti++) {
            mt[mti] = (1812433253 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti);
            mt[mti] &= 0xffffffff;
        }
    }

    /**
     * Sets the seed of the MersenneTwister using an array of integers.
     * Your array must have a non-zero length.  Only the first 624 integers
     * in the array are used; if the array is shorter than this then
     * integers are repeatedly used in a wrap-around fashion.
     */
    public synchronized void setSeed(final int[] array) {
        if (array.length == 0) throw new IllegalArgumentException("Array length must be greater than zero");
        int i, j, k;
        setSeed(19650218);
        i = 1;
        j = 0;
        k = (N > array.length ? N : array.length);
        for (; k != 0; k--) {
            mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * 1664525)) + array[j] + j;
            mt[i] &= 0xffffffff;
            i++;
            j++;
            if (i >= N) {
                mt[0] = mt[N - 1];
                i = 1;
            }
            if (j >= array.length) j = 0;
        }
        for (k = N - 1; k != 0; k--) {
            mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * 1566083941)) - i;
            mt[i] &= 0xffffffff;
            i++;
            if (i >= N) {
                mt[0] = mt[N - 1];
                i = 1;
            }
        }
        mt[0] = 0x80000000;
    }

    public final int nextInt() {
        int y;
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
            mti = 0;
        }
        y = mt[mti++];
        y ^= y >>> 11;
        y ^= (y << 7) & TEMPERING_MASK_B;
        y ^= (y << 15) & TEMPERING_MASK_C;
        y ^= (y >>> 18);
        return y;
    }

    public final short nextShort() {
        int y;
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
            mti = 0;
        }
        y = mt[mti++];
        y ^= y >>> 11;
        y ^= (y << 7) & TEMPERING_MASK_B;
        y ^= (y << 15) & TEMPERING_MASK_C;
        y ^= (y >>> 18);
        return (short) (y >>> 16);
    }

    public final char nextChar() {
        int y;
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
            mti = 0;
        }
        y = mt[mti++];
        y ^= y >>> 11;
        y ^= (y << 7) & TEMPERING_MASK_B;
        y ^= (y << 15) & TEMPERING_MASK_C;
        y ^= (y >>> 18);
        return (char) (y >>> 16);
    }

    public final boolean nextBoolean() {
        int y;
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
            mti = 0;
        }
        y = mt[mti++];
        y ^= y >>> 11;
        y ^= (y << 7) & TEMPERING_MASK_B;
        y ^= (y << 15) & TEMPERING_MASK_C;
        y ^= (y >>> 18);
        return (y >>> 31) != 0;
    }

    /** This generates a coin flip with a probability <tt>probability</tt>
     * of returning true, else returning false.  <tt>probability</tt> must
     * be between 0.0 and 1.0, inclusive.   Not as precise a random real
     * event as nextBoolean(double), but twice as fast. To explicitly
     * use this, remember you may need to cast to float first.
     * @param probability flip probability
     * @return next boolean
     */
    public final boolean nextBoolean(final float probability) {
        int y;
        if (probability < 0.0f || probability > 1.0f) throw new IllegalArgumentException("probability must be between 0.0 and 1.0 inclusive.");
        if (probability == 0.0f) return false; else if (probability == 1.0f) return true;
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
            mti = 0;
        }
        y = mt[mti++];
        y ^= y >>> 11;
        y ^= (y << 7) & TEMPERING_MASK_B;
        y ^= (y << 15) & TEMPERING_MASK_C;
        y ^= (y >>> 18);
        return (y >>> 8) / ((float) (1 << 24)) < probability;
    }

    /** This generates a coin flip with a probability <tt>probability</tt>
     * of returning true, else returning false.  <tt>probability</tt> must
     * be between 0.0 and 1.0, inclusive. */
    @SuppressWarnings({ "ConstantConditions" })
    public final boolean nextBoolean(final double probability) {
        int y;
        int z;
        if (probability < 0.0 || probability > 1.0) throw new IllegalArgumentException("probability must be between 0.0 and 1.0 inclusive.");
        if (probability == 0.0) return false; else if (probability == 1.0) return true;
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
            mti = 0;
        }
        y = mt[mti++];
        y ^= y >>> 11;
        y ^= (y << 7) & TEMPERING_MASK_B;
        y ^= (y << 15) & TEMPERING_MASK_C;
        y ^= (y >>> 18);
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (z >>> 1) ^ mag01[z & 0x1];
            }
            for (; kk < N - 1; kk++) {
                z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (z >>> 1) ^ mag01[z & 0x1];
            }
            z = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (z >>> 1) ^ mag01[z & 0x1];
            mti = 0;
        }
        z = mt[mti++];
        z ^= z >>> 11;
        z ^= (z << 7) & TEMPERING_MASK_B;
        z ^= (z << 15) & TEMPERING_MASK_C;
        z ^= (z >>> 18);
        return ((((long) (y >>> 6)) << 27) + (z >>> 5)) / (double) (1L << 53) < probability;
    }

    public final byte nextByte() {
        int y;
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
            mti = 0;
        }
        y = mt[mti++];
        y ^= y >>> 11;
        y ^= (y << 7) & TEMPERING_MASK_B;
        y ^= (y << 15) & TEMPERING_MASK_C;
        y ^= (y >>> 18);
        return (byte) (y >>> 24);
    }

    public final void nextBytes(byte[] bytes) {
        int y;
        for (int x = 0; x < bytes.length; x++) {
            if (mti >= N) {
                int kk;
                final int[] mt = this.mt;
                final int[] mag01 = this.mag01;
                for (kk = 0; kk < N - M; kk++) {
                    y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                    mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
                }
                for (; kk < N - 1; kk++) {
                    y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                    mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
                }
                y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
                mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
                mti = 0;
            }
            y = mt[mti++];
            y ^= y >>> 11;
            y ^= (y << 7) & TEMPERING_MASK_B;
            y ^= (y << 15) & TEMPERING_MASK_C;
            y ^= (y >>> 18);
            bytes[x] = (byte) (y >>> 24);
        }
    }

    @SuppressWarnings({ "ConstantConditions" })
    public final long nextLong() {
        int y;
        int z;
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
            mti = 0;
        }
        y = mt[mti++];
        y ^= y >>> 11;
        y ^= (y << 7) & TEMPERING_MASK_B;
        y ^= (y << 15) & TEMPERING_MASK_C;
        y ^= (y >>> 18);
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (z >>> 1) ^ mag01[z & 0x1];
            }
            for (; kk < N - 1; kk++) {
                z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (z >>> 1) ^ mag01[z & 0x1];
            }
            z = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (z >>> 1) ^ mag01[z & 0x1];
            mti = 0;
        }
        z = mt[mti++];
        z ^= z >>> 11;
        z ^= (z << 7) & TEMPERING_MASK_B;
        z ^= (z << 15) & TEMPERING_MASK_C;
        z ^= (z >>> 18);
        return (((long) y) << 32) + (long) z;
    }

    /** Returns a long drawn uniformly from 0 to n-1.  Suffice it to say,
     * n must be > 0, or an IllegalArgumentException is raised.
     * @param n max long
     * @return next long
     */
    @SuppressWarnings({ "ConstantConditions" })
    public final long nextLong(final long n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        long bits, val;
        do {
            int y;
            int z;
            if (mti >= N) {
                int kk;
                final int[] mt = this.mt;
                final int[] mag01 = this.mag01;
                for (kk = 0; kk < N - M; kk++) {
                    y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                    mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
                }
                for (; kk < N - 1; kk++) {
                    y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                    mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
                }
                y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
                mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
                mti = 0;
            }
            y = mt[mti++];
            y ^= y >>> 11;
            y ^= (y << 7) & TEMPERING_MASK_B;
            y ^= (y << 15) & TEMPERING_MASK_C;
            y ^= (y >>> 18);
            if (mti >= N) {
                int kk;
                final int[] mt = this.mt;
                final int[] mag01 = this.mag01;
                for (kk = 0; kk < N - M; kk++) {
                    z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                    mt[kk] = mt[kk + M] ^ (z >>> 1) ^ mag01[z & 0x1];
                }
                for (; kk < N - 1; kk++) {
                    z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                    mt[kk] = mt[kk + (M - N)] ^ (z >>> 1) ^ mag01[z & 0x1];
                }
                z = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
                mt[N - 1] = mt[M - 1] ^ (z >>> 1) ^ mag01[z & 0x1];
                mti = 0;
            }
            z = mt[mti++];
            z ^= z >>> 11;
            z ^= (z << 7) & TEMPERING_MASK_B;
            z ^= (z << 15) & TEMPERING_MASK_C;
            z ^= (z >>> 18);
            bits = (((((long) y) << 32) + (long) z) >>> 1);
            val = bits % n;
        } while (bits - val + (n - 1) < 0);
        return val;
    }

    /** Returns a random double in the half-open range from [0.0,1.0).  Thus 0.0 is a valid
     * result but 1.0 is not. */
    @SuppressWarnings({ "ConstantConditions" })
    public final double nextDouble() {
        int y;
        int z;
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
            mti = 0;
        }
        y = mt[mti++];
        y ^= y >>> 11;
        y ^= (y << 7) & TEMPERING_MASK_B;
        y ^= (y << 15) & TEMPERING_MASK_C;
        y ^= (y >>> 18);
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (z >>> 1) ^ mag01[z & 0x1];
            }
            for (; kk < N - 1; kk++) {
                z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (z >>> 1) ^ mag01[z & 0x1];
            }
            z = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (z >>> 1) ^ mag01[z & 0x1];
            mti = 0;
        }
        z = mt[mti++];
        z ^= z >>> 11;
        z ^= (z << 7) & TEMPERING_MASK_B;
        z ^= (z << 15) & TEMPERING_MASK_C;
        z ^= (z >>> 18);
        return ((((long) (y >>> 6)) << 27) + (z >>> 5)) / (double) (1L << 53);
    }

    @SuppressWarnings({ "ConstantConditions" })
    public final double nextGaussian() {
        if (__haveNextNextGaussian) {
            __haveNextNextGaussian = false;
            return __nextNextGaussian;
        } else {
            double v1, v2, s;
            do {
                int y;
                int z;
                int a;
                int b;
                if (mti >= N) {
                    int kk;
                    final int[] mt = this.mt;
                    final int[] mag01 = this.mag01;
                    for (kk = 0; kk < N - M; kk++) {
                        y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                        mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
                    }
                    for (; kk < N - 1; kk++) {
                        y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                        mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
                    }
                    y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
                    mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
                    mti = 0;
                }
                y = mt[mti++];
                y ^= y >>> 11;
                y ^= (y << 7) & TEMPERING_MASK_B;
                y ^= (y << 15) & TEMPERING_MASK_C;
                y ^= (y >>> 18);
                if (mti >= N) {
                    int kk;
                    final int[] mt = this.mt;
                    final int[] mag01 = this.mag01;
                    for (kk = 0; kk < N - M; kk++) {
                        z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                        mt[kk] = mt[kk + M] ^ (z >>> 1) ^ mag01[z & 0x1];
                    }
                    for (; kk < N - 1; kk++) {
                        z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                        mt[kk] = mt[kk + (M - N)] ^ (z >>> 1) ^ mag01[z & 0x1];
                    }
                    z = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
                    mt[N - 1] = mt[M - 1] ^ (z >>> 1) ^ mag01[z & 0x1];
                    mti = 0;
                }
                z = mt[mti++];
                z ^= z >>> 11;
                z ^= (z << 7) & TEMPERING_MASK_B;
                z ^= (z << 15) & TEMPERING_MASK_C;
                z ^= (z >>> 18);
                if (mti >= N) {
                    int kk;
                    final int[] mt = this.mt;
                    final int[] mag01 = this.mag01;
                    for (kk = 0; kk < N - M; kk++) {
                        a = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                        mt[kk] = mt[kk + M] ^ (a >>> 1) ^ mag01[a & 0x1];
                    }
                    for (; kk < N - 1; kk++) {
                        a = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                        mt[kk] = mt[kk + (M - N)] ^ (a >>> 1) ^ mag01[a & 0x1];
                    }
                    a = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
                    mt[N - 1] = mt[M - 1] ^ (a >>> 1) ^ mag01[a & 0x1];
                    mti = 0;
                }
                a = mt[mti++];
                a ^= a >>> 11;
                a ^= (a << 7) & TEMPERING_MASK_B;
                a ^= (a << 15) & TEMPERING_MASK_C;
                a ^= (a >>> 18);
                if (mti >= N) {
                    int kk;
                    final int[] mt = this.mt;
                    final int[] mag01 = this.mag01;
                    for (kk = 0; kk < N - M; kk++) {
                        b = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                        mt[kk] = mt[kk + M] ^ (b >>> 1) ^ mag01[b & 0x1];
                    }
                    for (; kk < N - 1; kk++) {
                        b = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                        mt[kk] = mt[kk + (M - N)] ^ (b >>> 1) ^ mag01[b & 0x1];
                    }
                    b = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
                    mt[N - 1] = mt[M - 1] ^ (b >>> 1) ^ mag01[b & 0x1];
                    mti = 0;
                }
                b = mt[mti++];
                b ^= b >>> 11;
                b ^= (b << 7) & TEMPERING_MASK_B;
                b ^= (b << 15) & TEMPERING_MASK_C;
                b ^= (b >>> 18);
                v1 = 2 * (((((long) (y >>> 6)) << 27) + (z >>> 5)) / (double) (1L << 53)) - 1;
                v2 = 2 * (((((long) (a >>> 6)) << 27) + (b >>> 5)) / (double) (1L << 53)) - 1;
                s = v1 * v1 + v2 * v2;
            } while (s >= 1 || s == 0);
            double multiplier = Math.sqrt(-2 * Math.log(s) / s);
            __nextNextGaussian = v2 * multiplier;
            __haveNextNextGaussian = true;
            return v1 * multiplier;
        }
    }

    /** Returns a random float in the half-open range from [0.0f,1.0f).  Thus 0.0f is a valid
     * result but 1.0f is not.
     * @return next float
     */
    public final float nextFloat() {
        int y;
        if (mti >= N) {
            int kk;
            final int[] mt = this.mt;
            final int[] mag01 = this.mag01;
            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
            mti = 0;
        }
        y = mt[mti++];
        y ^= y >>> 11;
        y ^= (y << 7) & TEMPERING_MASK_B;
        y ^= (y << 15) & TEMPERING_MASK_C;
        y ^= (y >>> 18);
        return (y >>> 8) / ((float) (1 << 24));
    }

    /** Returns an integer drawn uniformly from 0 to n-1.  Suffice it to say,
     * n must be > 0, or an IllegalArgumentException is raised. */
    @SuppressWarnings({ "ConstantConditions" })
    public final int nextInt(final int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        if ((n & -n) == n) {
            int y;
            if (mti >= N) {
                int kk;
                final int[] mt = this.mt;
                final int[] mag01 = this.mag01;
                for (kk = 0; kk < N - M; kk++) {
                    y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                    mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
                }
                for (; kk < N - 1; kk++) {
                    y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                    mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
                }
                y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
                mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
                mti = 0;
            }
            y = mt[mti++];
            y ^= y >>> 11;
            y ^= (y << 7) & TEMPERING_MASK_B;
            y ^= (y << 15) & TEMPERING_MASK_C;
            y ^= (y >>> 18);
            return (int) ((n * (long) (y >>> 1)) >> 31);
        }
        int bits, val;
        do {
            int y;
            if (mti >= N) {
                int kk;
                final int[] mt = this.mt;
                final int[] mag01 = this.mag01;
                for (kk = 0; kk < N - M; kk++) {
                    y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                    mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
                }
                for (; kk < N - 1; kk++) {
                    y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                    mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
                }
                y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
                mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
                mti = 0;
            }
            y = mt[mti++];
            y ^= y >>> 11;
            y ^= (y << 7) & TEMPERING_MASK_B;
            y ^= (y << 15) & TEMPERING_MASK_C;
            y ^= (y >>> 18);
            bits = (y >>> 1);
            val = bits % n;
        } while (bits - val + (n - 1) < 0);
        return val;
    }

    /**
     * Tests the code.
     * @param args arguments
     */
    @SuppressWarnings({ "ConstantConditions" })
    public static void main(String args[]) {
        int j;
        MersenneTwisterFast r;
        r = new MersenneTwisterFast(new int[] { 0x123, 0x234, 0x345, 0x456 });
        System.out.println("Output of MersenneTwisterFast with new (2002/1/26) seeding mechanism");
        for (j = 0; j < 1000; j++) {
            long l = (long) r.nextInt();
            if (l < 0) l += 4294967296L;
            String s = String.valueOf(l);
            while (s.length() < 10) s = " " + s;
            System.out.print(s + " ");
            if (j % 5 == 4) System.out.println();
        }
        final long SEED = 4357;
        int xx;
        long ms;
        System.out.println("\nTime to test grabbing 100000000 ints");
        Random rr = new Random(SEED);
        xx = 0;
        ms = System.currentTimeMillis();
        for (j = 0; j < 100000000; j++) xx += rr.nextInt();
        System.out.println("java.util.Random: " + (System.currentTimeMillis() - ms) + "          Ignore this: " + xx);
        r = new MersenneTwisterFast(SEED);
        ms = System.currentTimeMillis();
        xx = 0;
        for (j = 0; j < 100000000; j++) xx += r.nextInt();
        System.out.println("Mersenne Twister Fast: " + (System.currentTimeMillis() - ms) + "          Ignore this: " + xx);
        System.out.println("\nGrab the first 1000 booleans");
        r = new MersenneTwisterFast(SEED);
        for (j = 0; j < 1000; j++) {
            System.out.print(r.nextBoolean() + " ");
            if (j % 8 == 7) System.out.println();
        }
        if (!(j % 8 == 7)) System.out.println();
        System.out.println("\nGrab 1000 booleans of increasing probability using nextBoolean(double)");
        r = new MersenneTwisterFast(SEED);
        for (j = 0; j < 1000; j++) {
            System.out.print(r.nextBoolean(j / 999.0) + " ");
            if (j % 8 == 7) System.out.println();
        }
        if (!(j % 8 == 7)) System.out.println();
        System.out.println("\nGrab 1000 booleans of increasing probability using nextBoolean(float)");
        r = new MersenneTwisterFast(SEED);
        for (j = 0; j < 1000; j++) {
            System.out.print(r.nextBoolean(j / 999.0f) + " ");
            if (j % 8 == 7) System.out.println();
        }
        if (!(j % 8 == 7)) System.out.println();
        byte[] bytes = new byte[1000];
        System.out.println("\nGrab the first 1000 bytes using nextBytes");
        r = new MersenneTwisterFast(SEED);
        r.nextBytes(bytes);
        for (j = 0; j < 1000; j++) {
            System.out.print(bytes[j] + " ");
            if (j % 16 == 15) System.out.println();
        }
        if (!(j % 16 == 15)) System.out.println();
        byte b;
        System.out.println("\nGrab the first 1000 bytes -- must be same as nextBytes");
        r = new MersenneTwisterFast(SEED);
        for (j = 0; j < 1000; j++) {
            System.out.print((b = r.nextByte()) + " ");
            if (b != bytes[j]) System.out.print("BAD ");
            if (j % 16 == 15) System.out.println();
        }
        if (!(j % 16 == 15)) System.out.println();
        System.out.println("\nGrab the first 1000 shorts");
        r = new MersenneTwisterFast(SEED);
        for (j = 0; j < 1000; j++) {
            System.out.print(r.nextShort() + " ");
            if (j % 8 == 7) System.out.println();
        }
        if (!(j % 8 == 7)) System.out.println();
        System.out.println("\nGrab the first 1000 ints");
        r = new MersenneTwisterFast(SEED);
        for (j = 0; j < 1000; j++) {
            System.out.print(r.nextInt() + " ");
            if (j % 4 == 3) System.out.println();
        }
        if (!(j % 4 == 3)) System.out.println();
        System.out.println("\nGrab the first 1000 ints of different sizes");
        r = new MersenneTwisterFast(SEED);
        int max = 1;
        for (j = 0; j < 1000; j++) {
            System.out.print(r.nextInt(max) + " ");
            max *= 2;
            if (max <= 0) max = 1;
            if (j % 4 == 3) System.out.println();
        }
        if (!(j % 4 == 3)) System.out.println();
        System.out.println("\nGrab the first 1000 longs");
        r = new MersenneTwisterFast(SEED);
        for (j = 0; j < 1000; j++) {
            System.out.print(r.nextLong() + " ");
            if (j % 3 == 2) System.out.println();
        }
        if (!(j % 3 == 2)) System.out.println();
        System.out.println("\nGrab the first 1000 longs of different sizes");
        r = new MersenneTwisterFast(SEED);
        long max2 = 1;
        for (j = 0; j < 1000; j++) {
            System.out.print(r.nextLong(max2) + " ");
            max2 *= 2;
            if (max2 <= 0) max2 = 1;
            if (j % 4 == 3) System.out.println();
        }
        if (!(j % 4 == 3)) System.out.println();
        System.out.println("\nGrab the first 1000 floats");
        r = new MersenneTwisterFast(SEED);
        for (j = 0; j < 1000; j++) {
            System.out.print(r.nextFloat() + " ");
            if (j % 4 == 3) System.out.println();
        }
        if (!(j % 4 == 3)) System.out.println();
        System.out.println("\nGrab the first 1000 doubles");
        r = new MersenneTwisterFast(SEED);
        for (j = 0; j < 1000; j++) {
            System.out.print(r.nextDouble() + " ");
            if (j % 3 == 2) System.out.println();
        }
        if (!(j % 3 == 2)) System.out.println();
        System.out.println("\nGrab the first 1000 gaussian doubles");
        r = new MersenneTwisterFast(SEED);
        for (j = 0; j < 1000; j++) {
            System.out.print(r.nextGaussian() + " ");
            if (j % 3 == 2) System.out.println();
        }
        if (!(j % 3 == 2)) System.out.println();
    }
}
