package com.mindbright.util;

public final class PrimeSieve {

    public static final byte[] bitCounts = { 0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8 };

    int[] table;

    public PrimeSieve(int x) {
        if (x < 4) x = 4;
        int len = (x - 3) / (32 * 2);
        table = new int[len];
        int max = len * 32;
        int stop = (int) java.lang.Math.sqrt((double) max) + 1;
        for (int i = 0; i < stop; i++) {
            if ((table[i / 32] & (1 << (i & (32 - 1)))) == 0) {
                int k = 3 + i * 2;
                for (int j = i + k; j < max; j += k) {
                    table[j / 32] |= (1 << (j & (32 - 1)));
                }
            }
        }
    }

    public int availablePrimes() {
        int i, bits, w, primes;
        for (i = 0, primes = 2; i < table.length; i++) {
            w = table[i];
            for (bits = 0; w != 0; w >>>= 8) {
                bits += (int) bitCounts[w & 0xff];
            }
            primes += (32 - bits);
        }
        return primes;
    }

    public int getNextPrime(int x) {
        int p = ((x - 3) / 2) + 1;
        switch(x) {
            case 0:
                return 2;
            case 1:
                return 2;
            case 2:
                return 3;
            default:
                while (true) {
                    if ((p / 32) >= table.length) return 0;
                    if ((table[p / 32] & (1 << (p & (32 - 1)))) == 0) return p * 2 + 3;
                    p++;
                }
        }
    }
}
