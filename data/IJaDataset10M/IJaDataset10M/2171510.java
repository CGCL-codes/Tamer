package it.unisa.dia.gas.plaf.jpbc.util.math;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BigIntegerUtils {

    public static final BigInteger TWO = BigInteger.valueOf(2);

    public static final BigInteger THREE = BigInteger.valueOf(3);

    public static final BigInteger FOUR = BigInteger.valueOf(4);

    public static final BigInteger FIVE = BigInteger.valueOf(5);

    public static final BigInteger SIX = BigInteger.valueOf(6);

    public static final BigInteger SEVEN = BigInteger.valueOf(7);

    public static final BigInteger EIGHT = BigInteger.valueOf(8);

    public static final BigInteger TWELVE = BigInteger.valueOf(12);

    public static final BigInteger MAXINT = BigInteger.valueOf(Integer.MAX_VALUE);

    public static final BigInteger ITERBETTER = ONE.shiftLeft(1024);

    public static boolean isOdd(BigInteger bigInteger) {
        return bigInteger.testBit(0);
    }

    public static byte[] naf(BigInteger n, byte k) {
        byte[] wnaf = new byte[n.bitLength() + 1];
        short pow2wB = (short) (1 << k);
        BigInteger pow2wBI = BigInteger.valueOf(pow2wB);
        int i = 0;
        int length = 0;
        while (n.signum() > 0) {
            if (n.testBit(0)) {
                BigInteger remainder = n.mod(pow2wBI);
                if (remainder.testBit(k - 1)) {
                    wnaf[i] = (byte) (remainder.intValue() - pow2wB);
                } else {
                    wnaf[i] = (byte) remainder.intValue();
                }
                n = n.subtract(BigInteger.valueOf(wnaf[i]));
                length = i;
            } else {
                wnaf[i] = 0;
            }
            n = n.shiftRight(1);
            i++;
        }
        length++;
        byte[] wnafShort = new byte[length];
        System.arraycopy(wnaf, 0, wnafShort, 0, length);
        return wnafShort;
    }

    public static int hammingWeight(byte[] bytes, int length) {
        int weight = 0;
        for (int i = 0; i <= length; i++) {
            if (bytes[i] != 0) weight++;
        }
        return weight;
    }

    public static BigInteger generateSolinasPrime(int bits, Random random) {
        BigInteger r, q;
        int exp2, sign1;
        while (true) {
            r = BigInteger.ZERO;
            if (random.nextInt(Integer.MAX_VALUE) % 2 != 0) {
                exp2 = bits - 1;
                sign1 = 1;
            } else {
                exp2 = bits;
                sign1 = -1;
            }
            r = r.setBit(exp2);
            q = BigInteger.ZERO.setBit((random.nextInt(Integer.MAX_VALUE) % (exp2 - 1)) + 1);
            if (sign1 > 0) {
                r = r.add(q);
            } else {
                r = r.subtract(q);
            }
            if (random.nextInt(Integer.MAX_VALUE) % 2 != 0) {
                r = r.add(BigInteger.ONE);
            } else {
                r = r.subtract(BigInteger.ONE);
            }
            if (r.isProbablePrime(10)) return r;
        }
    }

    public static BigInteger factorial(int n) {
        return factorial(BigInteger.valueOf(n));
    }

    public static BigInteger factorial(BigInteger n) {
        if (n.equals(ZERO)) return ONE;
        BigInteger i = n.subtract(ONE);
        while (i.compareTo(ZERO) > 0) {
            n = n.multiply(i);
            i = i.subtract(ONE);
        }
        return n;
    }

    /**
     * Compute trace of Frobenius at q^n given trace at q
     * see p.105 of Blake, Seroussi and Smart
     *
     * @param q
     * @param trace
     * @param n
     * @return
     */
    public static BigInteger computeTrace(BigInteger q, BigInteger trace, int n) {
        int i;
        BigInteger c0, c1, c2;
        BigInteger t0;
        c2 = TWO;
        c1 = trace;
        for (i = 2; i <= n; i++) {
            c0 = trace.multiply(c1);
            t0 = q.multiply(c2);
            c0 = c0.subtract(t0);
            c2 = c1;
            c1 = c0;
        }
        return c1;
    }

    public static BigInteger pbc_mpz_curve_order_extn(BigInteger q, BigInteger t, int k) {
        BigInteger z = q.pow(k).add(BigInteger.ONE);
        BigInteger tk = computeTrace(q, t, k);
        z = z.subtract(tk);
        return z;
    }

    public static boolean isDivisible(BigInteger a, BigInteger b) {
        return a.remainder(b).compareTo(ZERO) == 0;
    }

    public static boolean isPerfectSquare(BigInteger n) {
        return fullSqrt(n)[1].signum() == 0;
    }

    public static BigInteger sqrt(BigInteger n) {
        return fullSqrt(n)[0];
    }

    public static BigInteger[] fullSqrt(BigInteger n) {
        if (n.compareTo(MAXINT) < 1) {
            long ln = n.longValue();
            long s = (long) java.lang.Math.sqrt(ln);
            return new BigInteger[] { BigInteger.valueOf(s), BigInteger.valueOf(ln - s * s) };
        }
        BigInteger[] sr = isqrtInternal(n, n.bitLength() - 1);
        if (sr[1].signum() < 0) {
            return new BigInteger[] { sr[0].subtract(ONE), sr[1].add(sr[0].shiftLeft(1)).subtract(ONE) };
        }
        return sr;
    }

    /**
     * Calculate the Legendre symbol (a/p). This is defined only for p an odd positive prime,
     * and for such p it's identical to the Jacobi symbol.
     *
     * @param a
     * @param n
     * @return
     */
    public static int legendre(BigInteger a, BigInteger n) {
        return jacobi(a, n);
    }

    public static int jacobi(BigInteger a, BigInteger n) {
        if (ZERO.equals(a)) return 0;
        int ans = 1;
        BigInteger temp;
        if (a.compareTo(ZERO) == -1) {
            a = a.negate();
            if (n.mod(FOUR).equals(THREE)) ans = -ans;
        }
        if (a.equals(ONE)) return ans;
        while (!ZERO.equals(a)) {
            if (a.compareTo(ZERO) == -1) {
                a = a.negate();
                if (n.mod(FOUR).equals(THREE)) ans = -ans;
            }
            while (a.mod(TWO).equals(ZERO)) {
                a = a.divide(TWO);
                BigInteger mod = n.mod(EIGHT);
                if (mod.equals(THREE) || mod.equals(FIVE)) ans = -ans;
            }
            temp = a;
            a = n;
            n = temp;
            if (a.mod(FOUR).equals(THREE) && n.mod(FOUR).equals(THREE)) ans = -ans;
            a = a.mod(n);
            if (a.compareTo(n.divide(TWO)) == 1) a = a.subtract(n);
        }
        if (n.equals(ONE)) return ans;
        return 0;
    }

    public static int scanOne(BigInteger a, int startIndex) {
        for (int i = startIndex, size = a.bitLength(); i < size; i++) {
            if (a.testBit(i)) return i;
        }
        return -1;
    }

    public static BigInteger getRandom(BigInteger limit) {
        return getRandom(limit, new SecureRandom());
    }

    public static BigInteger getRandom(BigInteger limit, SecureRandom secureRandom) {
        BigInteger result;
        do {
            result = new BigInteger(limit.bitLength(), secureRandom);
        } while (limit.compareTo(result) >= 0);
        return result;
    }

    /**
     * Compute trace of Frobenius at q^n given trace at q.
     * See p.105 of Blake, Seroussi and Smart.
     *
     * @param q
     * @param trace
     * @param n
     * @return
     */
    public static BigInteger traceN(BigInteger q, BigInteger trace, int n) {
        BigInteger c2 = TWO;
        BigInteger c1 = trace;
        for (int i = 2; i <= n; i++) {
            BigInteger c0 = trace.multiply(c1);
            BigInteger t0 = q.multiply(c2);
            c0 = c0.subtract(t0);
            c2 = c1;
            c1 = c0;
        }
        return c1;
    }

    private static BigInteger[] isqrtInternal(BigInteger n, int log2n) {
        if (n.compareTo(MAXINT) < 1) {
            int ln = n.intValue(), s = (int) java.lang.Math.sqrt(ln);
            return new BigInteger[] { BigInteger.valueOf(s), BigInteger.valueOf(ln - s * s) };
        }
        if (n.compareTo(ITERBETTER) < 1) {
            int d = 7 * (log2n / 14 - 1), q = 7;
            BigInteger s = BigInteger.valueOf((long) java.lang.Math.sqrt(n.shiftRight(d << 1).intValue()));
            while (d > 0) {
                if (q > d) q = d;
                s = s.shiftLeft(q);
                d -= q;
                q <<= 1;
                s = s.add(n.shiftRight(d << 1).divide(s)).shiftRight(1);
            }
            return new BigInteger[] { s, n.subtract(s.multiply(s)) };
        }
        int log2b = log2n >> 2;
        BigInteger mask = ONE.shiftLeft(log2b).subtract(ONE);
        BigInteger[] sr = isqrtInternal(n.shiftRight(log2b << 1), log2n - (log2b << 1));
        BigInteger s = sr[0];
        BigInteger[] qu = sr[1].shiftLeft(log2b).add(n.shiftRight(log2b).and(mask)).divideAndRemainder(s.shiftLeft(1));
        BigInteger q = qu[0];
        return new BigInteger[] { s.shiftLeft(log2b).add(q), qu[1].shiftLeft(log2b).add(n.and(mask)).subtract(q.multiply(q)) };
    }

    public static int hammingWeight(BigInteger value) {
        int weight = 0;
        for (int i = 0; i <= value.bitLength(); i++) {
            if (value.testBit(i)) weight++;
        }
        return weight;
    }

    /**
     * Divides `n` with primes up to `limit`. For each factor found,
     * call `fun`. If the callback returns nonzero, then aborts and returns 1.
     * Otherwise returns 0.
     */
    public abstract static class TrialDivide {

        protected BigInteger limit;

        public TrialDivide(BigInteger limit) {
            this.limit = limit;
        }

        public int trialDivide(BigInteger n) {
            BigInteger m = n;
            BigInteger p = TWO;
            while (m.compareTo(BigInteger.ONE) != 0) {
                if (m.isProbablePrime(10)) p = m;
                if (limit != null && !limit.equals(BigInteger.ZERO) && p.compareTo(limit) > 0) p = m;
                if (isDivisible(m, p)) {
                    int mul = 0;
                    do {
                        m = m.divide(p);
                        mul++;
                    } while (isDivisible(m, p));
                    if (fun(p, mul) != 0) return 1;
                }
                p = p.nextProbablePrime();
            }
            return 0;
        }

        protected abstract int fun(BigInteger factor, int multiplicity);
    }
}
