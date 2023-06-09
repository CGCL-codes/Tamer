package org.xmlcml.cml.base;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.euclid.EC;
import org.xmlcml.euclid.test.DoubleTestBase;

/**
 * tests for doubleAttribute.
 * 
 * @author pmr
 * 
 */
public class DoubleArrayAttributeTest {

    DoubleArraySTAttribute daa1;

    DoubleArraySTAttribute daa2;

    /**
	 * setup.
	 * 
	 * @throws Exception
	 */
    @Before
    public void setUp() throws Exception {
        daa1 = new DoubleArraySTAttribute(new CMLAttribute("foo"), " 1.2   3.4  ");
    }

    /**
	 * Test method for
	 * 'org.xmlcml.cml.base.DoubleArraySTAttribute.getCMLValue()'
	 */
    @Test
    public void testGetCMLValue() {
        Assert.assertNull("get CMLValue", daa1.getCMLValue());
    }

    /**
	 * Test method for
	 * 'org.xmlcml.cml.base.DoubleArraySTAttribute.setCMLValue(String)'
	 */
    @Test
    public void testSetCMLValueString() {
        daa1.setCMLValue("3.4  5.6");
        double[] dd = (double[]) daa1.getCMLValue();
        DoubleTestBase.assertEquals("get CMLValue", new double[] { 3.4, 5.6 }, dd, EC.EPS);
    }

    /**
	 * Test method for'org.xmlcml.cml.base.DoubleArraySTAttribute.DoubleArrayAttribute(DoubleArraySTAttribu
	 * t e ) '
	 */
    @Test
    public void testDoubleArrayAttributeDoubleArrayAttribute() {
        daa1.setCMLValue("3.4  5.6");
        daa2 = new DoubleArraySTAttribute(daa1);
        double[] dd = (double[]) daa2.getCMLValue();
        DoubleTestBase.assertEquals("get CMLValue", new double[] { 3.4, 5.6 }, dd, EC.EPS);
    }

    /**
	 * Test method for
	 * 'org.xmlcml.cml.base.DoubleArraySTAttribute.setCMLValue(double[])'
	 */
    @Test
    public void testSetCMLValueDoubleArray() {
        daa1.setCMLValue(new double[] { 5.6, 7.8 });
        Assert.assertEquals("get Value", "5.6 7.8", daa1.getValue());
    }

    /**
	 * Test method for
	 * 'org.xmlcml.cml.base.DoubleArraySTAttribute.checkValue(double[])'
	 */
    @Test
    public void testCheckValue() {
        daa1.checkValue(new double[] { 5.6, 7.8 });
        Assert.assertEquals("get Value", "1.2 3.4", daa1.getValue());
    }

    /**
	 * Test method for 'org.xmlcml.cml.base.DoubleArraySTAttribute.split(String,
	 * String)'
	 */
    @Test
    public void testSplit() {
        double[] dd = DoubleArraySTAttribute.split("1.2 3.4 5.6", CMLConstants.S_SPACE);
        Assert.assertEquals("split", 3, dd.length);
        DoubleTestBase.assertEquals("split", new double[] { 1.2, 3.4, 5.6 }, dd, EC.EPS);
        dd = DoubleArraySTAttribute.split("1.7 3.4 5.6", null);
        Assert.assertEquals("split", 3, dd.length);
        DoubleTestBase.assertEquals("split", new double[] { 1.7, 3.4, 5.6 }, dd, EC.EPS);
    }

    @Test
    public void testNaNSplit() {
        double[] dd = DoubleArraySTAttribute.split("NaN INF -INF 2.1", CMLConstants.S_SPACE);
        DoubleTestBase.assertObjectivelyEquals("NaN split", new double[] { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 2.1 }, dd, EC.EPS);
    }

    /**
	 * Test method for
	 * 'org.xmlcml.cml.base.DoubleArraySTAttribute.getDoubleArray()'
	 */
    @Test
    public void testGetDoubleArray() {
        daa1.setCMLValue(new double[] { 5.6, 7.8 });
        DoubleTestBase.assertEquals("get Value", new double[] { 5.6, 7.8 }, daa1.getDoubleArray(), EC.EPS);
    }
}
