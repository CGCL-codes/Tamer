package math.integer;

import junit.framework.TestCase;
import math.abstractalgebra.AbstractMath;

public class PrimeFactorizationStrategyImplTest extends TestCase {

    public void testPrime(long p) {
        for (long i = 2; i < p; i++) {
            assertFalse(p % i == 0);
        }
    }

    public void testPF(PrimeFactorizationStrategy strategy, long x) {
        PrimeFactor[] pfs = strategy.primeFactorization(x);
        long result = 1;
        long prev = 1;
        for (PrimeFactor pf : pfs) {
            assertTrue(pf.getPrime() > prev);
            testPrime(pf.getPrime());
            result = result * AbstractMath.pow(IntegerMulGroup.instance, pf.getPrime(), pf.getExponent());
            prev = pf.getPrime();
        }
        assertEquals(x, result);
    }

    public void testLC() {
        PrimeFactorizationStrategy strategy = new PrimeFactorizationStrategyImpl();
        for (int i = 1; i < 1000; i++) {
            testPF(strategy, i);
        }
    }
}
