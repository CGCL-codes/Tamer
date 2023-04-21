package org.jmathematics.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.jmathematics.Term;
import org.jmathematics.number.IntegerNumber;
import org.jmathematics.number.Natural0Number;
import org.jmathematics.number.NaturalNumber;
import org.jmathematics.number.Numbers;
import org.jmathematics.number.Prime;
import org.jmathematics.sets.NumberSet;
import org.jmathematics.utils.BigIntegerUtils;
import org.jmathematics.utils.IntegerUtils;
import org.jmathematics.vector.Vector;
import org.jmathematics.vector.Vectors;

public final class NaturalNumberImpl extends AbstractIntegerNumber implements NaturalNumber {

    public static final NaturalNumberImpl ONE = new NaturalNumberImpl(BigIntegerUtils.ONE, new int[] {});

    public static final NaturalNumberImpl TWO = new NaturalNumberImpl(BigIntegerUtils.TWO, new int[] { 1 });

    public static final NaturalNumberImpl THREE = new NaturalNumberImpl(BigIntegerUtils.THREE, new int[] { 0, 1 });

    public static final NaturalNumberImpl FIVE = new NaturalNumberImpl(BigIntegerUtils.FIVE, new int[] { 0, 0, 1 });

    public static final NaturalNumberImpl SEVEN = new NaturalNumberImpl(BigIntegerUtils.SEVEN, new int[] { 0, 0, 0, 1 });

    public static final NaturalNumberImpl TEN = new NaturalNumberImpl(BigIntegerUtils.TEN, new int[] { 1, 0, 1 });

    public static final NaturalNumberImpl ELEVEN = new NaturalNumberImpl(BigIntegerUtils.ELEVEN, new int[] { 0, 0, 0, 0, 1 });

    private static final Map<BigInteger, NaturalNumberImpl> cache = new HashMap<BigInteger, NaturalNumberImpl>();

    static {
        BigInteger bi;
        cache.put(BigIntegerUtils.ONE, ONE);
        cache.put(BigIntegerUtils.TWO, TWO);
        cache.put(BigIntegerUtils.THREE, THREE);
        bi = BigInteger.valueOf(4L);
        cache.put(bi, new NaturalNumberImpl(bi, new int[] { 2 }));
        cache.put(BigIntegerUtils.FIVE, FIVE);
        cache.put(BigIntegerUtils.SIX, new NaturalNumberImpl(BigIntegerUtils.SIX, new int[] { 1, 1 }));
        cache.put(BigIntegerUtils.SEVEN, SEVEN);
        bi = BigInteger.valueOf(8L);
        cache.put(bi, new NaturalNumberImpl(bi, new int[] { 3 }));
        bi = BigInteger.valueOf(9L);
        cache.put(bi, new NaturalNumberImpl(bi, new int[] { 0, 2 }));
        cache.put(BigIntegerUtils.TEN, TEN);
        cache.put(BigIntegerUtils.ELEVEN, ELEVEN);
    }

    private static NaturalNumberImpl cached(BigInteger integer, Map<Integer, NaturalNumber> primeFactors) {
        NaturalNumberImpl cached = cache.get(integer);
        if (cached == null) {
            cached = new NaturalNumberImpl(integer, primeFactors);
            if (cache.size() < 100000) cache.put(integer, cached);
        }
        return cached.setPrimefactors(primeFactors);
    }

    public static NaturalNumberImpl valueOf(BigInteger integer) {
        int cmp = BigInteger.ONE.compareTo(integer);
        if (cmp > 0) throw new RuntimeException(integer + " is not natural");
        if (cmp == 0) return ONE;
        return cached(integer, null);
    }

    public static NaturalNumberImpl valueOf(Map<Integer, NaturalNumber> primeFactors) {
        BigInteger val = BigInteger.ONE;
        for (Map.Entry<Integer, NaturalNumber> pF : primeFactors.entrySet()) val = val.multiply(Prime.get(pF.getKey()).toBigInteger().pow(pF.getValue().toBigInteger().intValue()));
        return cached(val, primeFactors);
    }

    public static NaturalNumberImpl valueOf(BigInteger integer, Map<Integer, NaturalNumber> primeFactors) {
        return cached(integer, primeFactors);
    }

    private final BigInteger integer;

    private Vector<NaturalNumber> primeFactors;

    private NaturalNumberImpl(BigInteger integer, int[] primefactors) {
        this.integer = integer;
        if (primefactors != null) {
            Map<Integer, NaturalNumber> map = new HashMap<Integer, NaturalNumber>();
            for (int i = 0; i < primefactors.length; i++) if (primefactors[i] != 0) map.put(IntegerUtils.valueOf(i), NaturalNumberImpl.valueOf(BigInteger.valueOf(primefactors[i])));
            this.primeFactors = Vectors.valueOf(map);
        }
    }

    private NaturalNumberImpl(BigInteger integer, Map<Integer, NaturalNumber> primefactors) {
        this.integer = integer;
        if (primefactors != null) this.primeFactors = Vectors.valueOf(primefactors);
    }

    private NaturalNumberImpl setPrimefactors(Map<Integer, NaturalNumber> primefactors) {
        if (primefactors != null && this.primeFactors == null) this.primeFactors = Vectors.valueOf(primefactors);
        return this;
    }

    public BigInteger toBigInteger() {
        return integer;
    }

    public NumberSet getNumberSet() {
        return NumberSet.NATURAL;
    }

    public NaturalNumberImpl toCanonical() {
        return this;
    }

    public IntegerNumber getAdditiveInverse() {
        return Numbers.valueOf(integer.negate());
    }

    public Vector<NaturalNumber> getPrimeFactors() {
        if (this.primeFactors == null) {
            System.out.println("searching primefactors of " + integer);
            this.primeFactors = Prime.getPrimeFactors(this);
        }
        return this.primeFactors;
    }

    public boolean isPrime() {
        boolean found = false;
        Vector<NaturalNumber> pf = getPrimeFactors();
        for (Integer n : pf.nonZeroIndices()) {
            Natural0Number x = pf.get(n.intValue());
            if (found) return false;
            found = true;
            if (!Numbers.ONE.equals(x)) return false;
        }
        return found;
    }

    public boolean equalsCanonical(Term to) {
        return to == null ? false : equals(to.toCanonical());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj instanceof NaturalNumberImpl) return integer.equals(((NaturalNumberImpl) obj).integer);
        return false;
    }

    @Override
    public int hashCode() {
        return integer.hashCode();
    }

    public NaturalNumberImpl getSuccessor() {
        return (NaturalNumberImpl) Numbers.valueOf(integer.add(BigInteger.ONE));
    }
}
