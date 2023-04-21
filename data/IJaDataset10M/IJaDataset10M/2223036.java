package org.apache.harmony.security.tests.java.security.cert;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathBuilderSpi;
import java.security.cert.CertPathParameters;
import org.apache.harmony.security.tests.support.cert.MyCertPathBuilderSpi;
import junit.framework.TestCase;

/**
 * Tests for <code>CertPathBuilderSpi</code> class constructors and methods.
 * 
 */
public class CertPathBuilderSpiTest extends TestCase {

    /**
     * Constructor for CertPathBuilderSpiTest.
     * 
     * @param arg0
     */
    public CertPathBuilderSpiTest(String arg0) {
        super(arg0);
    }

    /**
     * Test for <code>CertPathBuilderSpi</code> constructor Assertion:
     * constructs CertPathBuilderSpi
     */
    public void testCertPathBuilderSpi01() throws CertPathBuilderException, InvalidAlgorithmParameterException {
        CertPathBuilderSpi certPathBuilder = new MyCertPathBuilderSpi();
        CertPathParameters cpp = null;
        try {
            certPathBuilder.engineBuild(cpp);
            fail("CertPathBuilderException must be thrown");
        } catch (CertPathBuilderException e) {
        }
        CertPathBuilderResult cpbResult = certPathBuilder.engineBuild(cpp);
        assertNull("Not null CertPathBuilderResult", cpbResult);
    }
}
