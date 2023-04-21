package org.xmlcml.cml.base.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.cml.base.CMLAttribute;
import org.xmlcml.cml.base.IntArrayAttribute;
import org.xmlcml.euclid.test.IntTest;

/**
 * tests for intAttribute.
 * 
 * @author pmr
 * 
 */
public class IntArrayAttributeTest extends AttributeBaseTest {

    IntArrayAttribute daa1;

    IntArrayAttribute daa2;

    /**
     * setup.
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        daa1 = new IntArrayAttribute(new CMLAttribute("foo"), " 1   3  ");
    }

    /**
     * Test method for 'org.xmlcml.cml.base.IntArrayAttribute.getCMLValue()'
     */
    @Test
    public void testGetCMLValue() {
        Assert.assertNull("get CMLValue", daa1.getCMLValue());
    }

    /**
     * Test method for
     * 'org.xmlcml.cml.base.IntArrayAttribute.setCMLValue(String)'
     */
    @Test
    public void testSetCMLValueString() {
        daa1.setCMLValue("3   5");
        int[] dd = (int[]) daa1.getCMLValue();
        IntTest.assertEquals("get CMLValue", new int[] { 3, 5 }, dd);
    }

    /**
     * Test method for
     * 'org.xmlcml.cml.base.IntArrayAttribute.IntArrayAttribute(IntArrayAttribute)'
     */
    @Test
    public void testIntArrayAttributeIntArrayAttribute() {
        daa1.setCMLValue("3  5");
        daa2 = new IntArrayAttribute(daa1);
        int[] dd = (int[]) daa2.getCMLValue();
        IntTest.assertEquals("get CMLValue", new int[] { 3, 5 }, dd);
    }

    /**
     * Test method for
     * 'org.xmlcml.cml.base.IntArrayAttribute.setCMLValue(int[])'
     */
    @Test
    public void testSetCMLValueIntArray() {
        daa1.setCMLValue(new int[] { 5, 7 });
        Assert.assertEquals("get Value", "5 7", daa1.getValue());
    }

    /**
     * Test method for 'org.xmlcml.cml.base.IntArrayAttribute.checkValue(int[])'
     */
    @Test
    public void testCheckValue() {
        daa1.checkValue(new int[] { 5, 7 });
        Assert.assertEquals("get Value", "1 3", daa1.getValue());
    }

    /**
     * Test method for 'org.xmlcml.cml.base.IntArrayAttribute.split(String,
     * String)'
     */
    @Test
    public void testSplit() {
        int[] dd = IntArrayAttribute.split("1 3 5", " ");
        Assert.assertEquals("split", 3, dd.length);
        IntTest.assertEquals("split", new int[] { 1, 3, 5 }, dd);
        dd = IntArrayAttribute.split("7 3 5", null);
        Assert.assertEquals("split", 3, dd.length);
        IntTest.assertEquals("split", new int[] { 7, 3, 5 }, dd);
    }

    /**
     * Test method for 'org.xmlcml.cml.base.IntArrayAttribute.getIntArray()'
     */
    @Test
    public void testGetIntArray() {
        daa1.setCMLValue(new int[] { 5, 7 });
        IntTest.assertEquals("get Value", new int[] { 5, 7 }, daa1.getIntegerArray());
    }
}
