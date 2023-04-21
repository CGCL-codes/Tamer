package com.tomgibara.crinch.math;

import java.math.BigInteger;

class LongCombinator extends AbstractCombinator implements PackedCombinator {

    private final int n;

    private final int k;

    private final BigInteger size;

    private final int nBits;

    private final long nMask;

    private final long[][] cs;

    LongCombinator(int n, int k, boolean packed) {
        this.n = n;
        this.k = k;
        cs = new long[k + 1][n + 1];
        for (int i = 0; i <= k; i++) {
            for (int j = 0; j <= n; j++) {
                cs[i][j] = Combinators.chooseAsLong(j, i);
            }
        }
        size = BigInteger.valueOf(cs[k][n]);
        if (packed) {
            nMask = (n == 1 ? 1 : (Long.highestOneBit(n - 1) << 1)) - 1;
            nBits = Long.bitCount(nMask);
            if (nBits * k > 64) throw new IllegalArgumentException("Too many bits for packing");
        } else {
            nBits = -1;
            nMask = -1L;
        }
    }

    LongCombinator(int n, int k) {
        this(n, k, false);
    }

    @Override
    public int getElementCount() {
        return n;
    }

    @Override
    public int getTupleLength() {
        return k;
    }

    @Override
    public BigInteger size() {
        return size;
    }

    @Override
    public int[] getCombination(BigInteger m) throws IndexOutOfBoundsException, IllegalArgumentException {
        return getCombination(m, new int[k]);
    }

    @Override
    public int[] getCombination(BigInteger m, int[] as) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (m.signum() < 0) throw new IndexOutOfBoundsException();
        if (m.compareTo(BigInteger.valueOf(cs[k][n])) >= 0) throw new IndexOutOfBoundsException();
        if (as.length < k) throw new IllegalArgumentException();
        return getCombinationImpl(m.longValue(), as);
    }

    @Override
    public int[] getCombination(long m) {
        return getCombination(m, new int[k]);
    }

    @Override
    public int[] getCombination(long m, int[] as) {
        final long[][] cs = this.cs;
        if (m < 0) throw new IndexOutOfBoundsException();
        if (m >= cs[k][n]) throw new IndexOutOfBoundsException();
        if (as.length < k) throw new IllegalArgumentException();
        return getCombinationImpl(m, as);
    }

    @Override
    public int getBitsPerElement() {
        return nBits;
    }

    @Override
    public long getPackedCombination(long m) throws IllegalStateException, IndexOutOfBoundsException {
        final long[][] cs = this.cs;
        if (m < 0) throw new IndexOutOfBoundsException();
        if (m >= cs[k][n]) throw new IndexOutOfBoundsException();
        int a = n;
        int b = k;
        long x = cs[b][a] - 1 - m;
        long as = 0L;
        for (int i = 0; i < k; i++) {
            a = largest(a, b, x);
            x -= cs[b][a];
            as = (as << nBits) | (n - 1 - a);
            b--;
        }
        return as;
    }

    @Override
    public int getPackedElement(long packed, int i) throws IllegalArgumentException {
        if (i < 0 || i >= k) throw new IllegalArgumentException("invalid tuple index");
        return (int) ((packed >> ((k - i - 1) * nBits)) & nMask);
    }

    private int[] getCombinationImpl(long m, int[] as) {
        int a = n;
        int b = k;
        long x = cs[b][a] - 1 - m;
        for (int i = 0; i < as.length; i++) {
            a = largest(a, b, x);
            x -= cs[b][a];
            as[i] = a;
            b--;
        }
        for (int i = 0; i < as.length; i++) {
            as[i] = n - 1 - as[i];
        }
        return as;
    }

    private int largest(int a, int b, long x) {
        int v;
        for (v = a - 1; cs[b][v] > x; v--) ;
        return v;
    }
}
