package com.ail.commercialtest;

import com.ail.util.Rate;
import junit.framework.Test;
import junit.framework.TestCase;
import java.math.BigDecimal;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/16 21:08:54 $
 * @source $Source: /home/bob/CVSRepository/projects/common/test.jar/com/ail/commercialtest/TestRate.java,v $
 */
public class TestRate extends TestCase {

    /**
     * Constructs a test case with the given name.
     * @param name The tests name
     */
    public TestRate(String name) {
        super(name);
    }

    /**
     * Create an instance of this test case as a TestSuite.
     * @return Test an instance of this test case.
     */
    public static Test suite() {
        return new junit.framework.TestSuite(TestRate.class);
    }

    /**
     * Run this testcase from the command line.
     * @param args No command line args are required.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Test valid arguments for the Rate constructor
     * @throws Exception
     */
    public void testGoodFormats() throws Exception {
        new Rate("3/20");
        new Rate("10%");
        new Rate("10percent");
        new Rate("10PeRcEnT");
        new Rate("100PERMIL");
        new Rate("100Permil");
    }

    /**
     * Test how badly formatted arguments are handled by the constructor.
     */
    public void testBadFormats() {
        try {
            new Rate("101%");
            fail("101% accepted");
        } catch (IllegalArgumentException e) {
        }
        try {
            new Rate("-1%");
            fail("-1% accepted");
        } catch (IllegalArgumentException e) {
        }
        try {
            new Rate("10plop");
            fail("10plop accepted");
        } catch (NumberFormatException e) {
        }
        try {
            new Rate("1001permil");
            fail("1001permil accepted");
        } catch (IllegalArgumentException e) {
        }
        try {
            new Rate("-1permil");
            fail("1permil accepted");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testRateCalculations() {
        Rate rate;
        rate = new Rate("10%");
        assertEquals("10%", rate.getRate());
        assertEquals("100", rate.applyTo("1000", 0, BigDecimal.ROUND_HALF_UP));
        assertTrue(100.0 == rate.applyTo(1000, 0, BigDecimal.ROUND_HALF_UP));
        assertEquals(new BigDecimal("100"), rate.applyTo(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP));
        rate = new Rate("10Percent");
        assertEquals("10Percent", rate.getRate());
        assertEquals("100", rate.applyTo("1000", 0, BigDecimal.ROUND_HALF_UP));
        assertTrue(100.0 == rate.applyTo(1000, 0, BigDecimal.ROUND_HALF_UP));
        assertEquals(new BigDecimal("100"), rate.applyTo(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP));
        rate = new Rate("10Permil");
        assertEquals("10Permil", rate.getRate());
        assertEquals("10", rate.applyTo("1000", 0, BigDecimal.ROUND_HALF_UP));
        assertTrue(10.0 == rate.applyTo(1000, 0, BigDecimal.ROUND_HALF_UP));
        assertEquals(new BigDecimal("10"), rate.applyTo(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP));
        rate = new Rate("3/20");
        assertEquals("3/20", rate.getRate());
        assertEquals("150", rate.applyTo("1000", 0, BigDecimal.ROUND_HALF_UP));
        assertTrue(150.0 == rate.applyTo(1000, 0, BigDecimal.ROUND_HALF_UP));
        assertEquals(new BigDecimal("150"), rate.applyTo(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP));
        rate = new Rate("10.5%");
        assertEquals("10.5%", rate.getRate());
        assertEquals("1.1", rate.applyTo("10", 1, BigDecimal.ROUND_HALF_UP));
    }
}
