package org.openscience.cdk.test.math;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.math.Primes;
import org.openscience.cdk.test.CDKTestCase;

/**
 * @cdk.module test-standard
 */
public class PrimesTest extends CDKTestCase {

    public PrimesTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(PrimesTest.class);
    }

    public void testGetPrimeAt_int() {
        assertEquals(2, Primes.getPrimeAt(0));
        try {
            Primes.getPrimeAt(2229);
            fail("Should fail her, because it contains only X primes.");
        } catch (ArrayIndexOutOfBoundsException exception) {
        }
    }

    public void testArrayIndexOutOfBounds() {
        try {
            Primes.getPrimeAt(-1);
            fail("Should fail her, because only positive integers are accepted");
        } catch (ArrayIndexOutOfBoundsException exception) {
        }
    }
}
