package de.abg.jreichert.serviceqos.calc.subtractor.serviceapi;

import org.fornax.cartridges.sculptor.framework.test.AbstractOpenEJBDbUnitTest;
import org.junit.Assert;
import org.junit.Test;

/**
 *  JUnit test with OpenEJB support.
 */
public class SubtractorTest extends AbstractOpenEJBDbUnitTest implements SubtractorTestBase {

    @javax.ejb.EJB
    private Subtractor subtractor;

    @Test
    public void testSubtract() throws Exception {
        Assert.assertEquals(-1, subtractor.subtract(4, 5));
    }
}
