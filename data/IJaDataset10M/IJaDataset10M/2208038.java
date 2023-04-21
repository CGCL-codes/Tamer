package com.syntelos.foam;

import java.math.BigInteger;
import java.util.Random;

/**
 * This class is for notational and control purposes.
 * 
 * @author John Pritchard
 */
public class uint64 extends BigInteger {

    public uint64(byte[] val) {
        super(val);
    }

    public uint64(int signum, byte[] magnitude) {
        super(signum, magnitude);
    }

    public uint64(String val, int radix) {
        super(val, radix);
    }

    public uint64(String val) {
        super(val);
    }

    public uint64(int numBits, Random prng) {
        super(numBits, prng);
    }

    public uint64(int bitLength, int certainty, Random prng) {
        super(bitLength, certainty, prng);
    }
}
