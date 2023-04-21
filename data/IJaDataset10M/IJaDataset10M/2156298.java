package math.integer;

import math.abstractalgebra.AbstractMath;

public class LeastCommonMultipleStrategyImpl implements LeastCommonMultipleStrategy {

    public long leastCommonMultiple(long... args) {
        PrimeFactor[][] pfs = new PrimeFactor[args.length][];
        for (int i = 0; i < args.length; i++) {
            pfs[i] = IntMath.primeFactorization(args[i]);
        }
        int[] pos = new int[args.length];
        long result = 1;
        for (; ; ) {
            long thePrime = Long.MAX_VALUE;
            for (int i = 0; i < args.length; i++) {
                if (pos[i] < pfs[i].length && pfs[i][pos[i]].getPrime() < thePrime) {
                    thePrime = pfs[i][pos[i]].getPrime();
                }
            }
            if (thePrime == Long.MAX_VALUE) {
                break;
            }
            long maxmult = 0;
            for (int i = 0; i < args.length; i++) {
                if (pos[i] < pfs[i].length && pfs[i][pos[i]].getPrime() == thePrime) {
                    maxmult = Math.max(pfs[i][pos[i]].getExponent(), maxmult);
                    pos[i]++;
                }
            }
            result *= AbstractMath.pow(IntegerMulGroup.instance, thePrime, maxmult);
        }
        return result;
    }
}
