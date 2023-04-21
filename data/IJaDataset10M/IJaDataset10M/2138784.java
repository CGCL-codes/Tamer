package com.tomgibara.crinch.coding;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import com.tomgibara.crinch.bits.BitReader;
import com.tomgibara.crinch.bits.BitStreamException;
import com.tomgibara.crinch.bits.BitVector;
import com.tomgibara.crinch.bits.BitWriter;

/**
 * Implements Fibonacci coding. All code words end with "11". Note that in
 * contrast to most presentations of Fibonacci coding, the mapping from integers
 * to code words begins at zero, so that 0 -> "11", 1 -> "011" and so on.
 * 
 * The singleton instance of this class is available from
 * {@link FibonacciCoding#instance}.
 * 
 * @author Tom Gibara
 * @see http://en.wikipedia.org/wiki/Fibonacci_coding
 */
public final class FibonacciCoding extends UniversalCoding {

    /**
	 * The sole instance of this class.
	 */
    public static final FibonacciCoding instance = new FibonacciCoding();

    /**
	 * An extended coding of this class.
	 */
    public static final ExtendedCoding extended = new ExtendedCoding(instance);

    private static final BigInteger LONG_ADJ = BigInteger.ONE.shiftLeft(64);

    private static final BigInteger BIG_INT_TWO = BigInteger.valueOf(2);

    private static final long[] fibLong = new long[91];

    static {
        fibLong[0] = 1;
        fibLong[1] = 2;
        for (int i = 2; i < fibLong.length; i++) {
            fibLong[i] = fibLong[i - 1] + fibLong[i - 2];
        }
    }

    private ThreadLocal<ArrayList<BigInteger>> fibBigInt = new ThreadLocal<ArrayList<BigInteger>>();

    private FibonacciCoding() {
    }

    @Override
    int unsafeEncodePositiveInt(BitWriter writer, int value) {
        value++;
        if (value < 0) return unsafeEncodePositiveLong(writer, (value - 1) & 0x00000000ffffffffL);
        int fi = Arrays.binarySearch(fibLong, value);
        if (fi < 0) fi = -2 - fi;
        int count = fi + 3;
        int out0 = 0;
        int out1 = 1;
        int offset = 0;
        boolean o = false;
        while (fi >= 0) {
            if (++offset == 32) {
                offset = 0;
                o = true;
            }
            long f = fibLong[fi--];
            if (value >= f) {
                value -= f;
                if (o) {
                    out0 |= 1 << offset;
                } else {
                    out1 |= 1 << offset;
                }
            }
        }
        if (++offset == 32) {
            offset = 0;
            o = true;
        }
        if (o) {
            return writer.write(out0, count - 32) + writer.write(out1, 32);
        } else {
            return writer.write(out1, count);
        }
    }

    @Override
    int unsafeEncodePositiveLong(BitWriter writer, long value) {
        value++;
        if (value < 0) return unsafeEncodePositiveBigInt(writer, LONG_ADJ.add(BigInteger.valueOf(value - 1)));
        int fi = Arrays.binarySearch(fibLong, 0, 91, value);
        if (fi < 0) fi = -2 - fi;
        int count = fi + 3;
        int out0 = 0;
        int out1 = 0;
        int out2 = 1;
        int offset = 0;
        int i = 2;
        while (fi >= 0) {
            if (++offset == 32) {
                offset = 0;
                i--;
            }
            long f = fibLong[fi--];
            if (value >= f) {
                value -= f;
                switch(i) {
                    case 0:
                        out0 |= 1 << offset;
                        break;
                    case 1:
                        out1 |= 1 << offset;
                        break;
                    case 2:
                        out2 |= 1 << offset;
                        break;
                }
            }
        }
        if (++offset == 32) {
            offset = 0;
            i--;
        }
        switch(i) {
            case 0:
                return writer.write(out0, count - 64) + writer.write(out1, 32) + writer.write(out2, 32);
            case 1:
                return writer.write(out1, count - 32) + writer.write(out2, 32);
            case 2:
                return writer.write(out2, count);
            default:
                throw new IllegalStateException("Long could not be encoded!!");
        }
    }

    @Override
    int unsafeEncodePositiveBigInt(BitWriter writer, BigInteger value) {
        value = value.add(BigInteger.ONE);
        ArrayList<BigInteger> fibs = getFibBigInt();
        int fi = Collections.binarySearch(fibs, value);
        int size = fibs.size();
        if (fi == -1 - size) {
            BigInteger fib = fibs.get(size - 1);
            while (value.compareTo(fib) > 0) {
                fib = fib.add(fibs.get(size - 2));
                fibs.add(fib);
                size++;
            }
            fi = fib.equals(value) ? size - 1 : -size;
        }
        if (fi < 0) fi = -2 - fi;
        int count = fi + 3;
        BitVector bits = new BitVector(count);
        while (fi >= 0) {
            BigInteger f = fibs.get(fi);
            if (value.compareTo(f) >= 0) {
                value = value.subtract(f);
                bits.setBit(count - fi - 2, true);
            }
            fi--;
        }
        bits.setBit(0, true);
        return bits.write(writer);
    }

    @Override
    public int decodePositiveInt(BitReader reader) {
        int last = reader.readBit();
        if (last != 0) throw new RuntimeException();
        int value = 0;
        for (int i = 0; i < 47; i++) {
            int bit = reader.readBit();
            if (bit == 1) {
                if (last == 1) return value - 1;
                value += (int) fibLong[i];
            }
            last = bit;
        }
        throw new BitStreamException("Value too large for int");
    }

    @Override
    public long decodePositiveLong(BitReader reader) {
        int last = reader.readBit();
        if (last != 0) throw new RuntimeException();
        long value = 0;
        for (int i = 0; ; i++) {
            int bit = reader.readBit();
            if (bit == 1) {
                if (last == 1) return value - 1L;
                if (i == 91) {
                    value += fibLong[i - 2] + fibLong[i - 1];
                    if (reader.readBoolean()) return value - 1L;
                }
                if (i >= 91) throw new BitStreamException("Value too large for long");
                value += fibLong[i];
            }
            last = bit;
        }
    }

    @Override
    public BigInteger decodePositiveBigInt(BitReader reader) {
        boolean last = reader.readBoolean();
        if (last) throw new BitStreamException();
        ArrayList<BigInteger> fibs = getFibBigInt();
        int size = fibs.size();
        BigInteger value = BigInteger.ZERO;
        for (int i = 0; ; i++) {
            boolean bit = reader.readBoolean();
            if (bit) {
                if (last) return value.subtract(BigInteger.ONE);
                if (i == size) {
                    fibs.add(fibs.get(size - 1).add(fibs.get(size - 2)));
                    size++;
                }
                value = value.add(fibs.get(i));
            }
            last = bit;
        }
    }

    private ArrayList<BigInteger> getFibBigInt() {
        ArrayList<BigInteger> fibs = fibBigInt.get();
        if (fibs != null) return fibs;
        fibs = new ArrayList<BigInteger>();
        fibs.add(BigInteger.ONE);
        fibs.add(BIG_INT_TWO);
        fibBigInt.set(fibs);
        return fibs;
    }
}
