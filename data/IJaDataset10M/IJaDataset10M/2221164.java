package tests.security.spec;

import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;
import junit.framework.TestCase;
import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;

/**
 * Tests for <code>DSAParameterSpec</code>
 * 
 */
@TestTargetClass(DSAParameterSpec.class)
public class DSAParameterSpecTest extends TestCase {

    /**
     * Ctor test 
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "DSAParameterSpec", args = { java.math.BigInteger.class, java.math.BigInteger.class, java.math.BigInteger.class })
    public final void testDSAParameterSpec() {
        AlgorithmParameterSpec aps = new DSAParameterSpec(new BigInteger("1"), new BigInteger("2"), new BigInteger("3"));
        assertTrue(aps instanceof DSAParameterSpec);
    }

    /**
     * getG() test
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getG", args = {  })
    public final void testGetG() {
        DSAParameterSpec dps = new DSAParameterSpec(new BigInteger("1"), new BigInteger("2"), new BigInteger("3"));
        assertEquals(3, dps.getG().intValue());
    }

    /**
     * getP() test
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getP", args = {  })
    public final void testGetP() {
        DSAParameterSpec dps = new DSAParameterSpec(new BigInteger("1"), new BigInteger("2"), new BigInteger("3"));
        assertEquals(1, dps.getP().intValue());
    }

    /**
     * getQ() test 
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getQ", args = {  })
    public final void testGetQ() {
        DSAParameterSpec dps = new DSAParameterSpec(new BigInteger("1"), new BigInteger("2"), new BigInteger("3"));
        assertEquals(2, dps.getQ().intValue());
    }
}
