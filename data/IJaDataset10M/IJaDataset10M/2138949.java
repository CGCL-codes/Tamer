package org.vizzini.util;

import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * Provides unit tests for the <code>GenericData</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.2
 */
public class GenericDataTest extends TestCase {

    /** First data. */
    private GenericData _data0;

    /** Second data. */
    private GenericData _data1;

    /** Third data. */
    private GenericData _data2;

    /** Fourth data. */
    private GenericData _data3;

    /** Fifth data. */
    private GenericData _data4;

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.2
     */
    public static void main(String[] args) {
        TestRunner.run(GenericDataTest.class);
    }

    /**
     * Test the <code>equals()</code> method.
     *
     * @since  v0.2
     */
    public void testEquals() {
        assertTrue(_data0.equals(_data0));
        assertTrue(_data0.equals(_data1));
        assertFalse(_data0.equals(_data2));
        assertFalse(_data0.equals(_data3));
        assertFalse(_data0.equals(_data4));
    }

    /**
     * Test the <code>get()</code> method.
     *
     * @since  v0.4
     */
    public void testGet0() {
        String propertyName = "someProperty";
        Double value = 1.234;
        GenericData data = new GenericData();
        data.set(propertyName, Double.class, value);
        Object result = data.get(propertyName);
        assertNotNull(result);
        assertEquals(value, result);
        assertEquals(Double.class, result.getClass());
    }

    /**
     * Test the <code>get()</code> method.
     *
     * @since  v0.4
     */
    public void testGet1() {
        String propertyName = "someProperty";
        Double value = 1.234;
        GenericData data = new GenericData();
        data.set(propertyName, value);
        Object result = data.get(propertyName);
        assertNotNull(result);
        assertEquals(value, result);
        assertEquals(Double.class, result.getClass());
    }

    /**
     * Test the <code>get()</code> method.
     *
     * @since  v0.4
     */
    public void testGet2() {
        String propertyName = "someProperty";
        String value = "1.234";
        GenericData data = new GenericData();
        data.set(propertyName, Double.class, value);
        Object result = data.get(propertyName);
        assertNotNull(result);
        assertEquals(1.234, result);
        assertEquals(Double.class, result.getClass());
    }

    /**
     * Test the <code>get()</code> method.
     *
     * @since  v0.4
     */
    public void testGet3() {
        String propertyName = "someProperty";
        String value = "1.234";
        GenericData data = new GenericData();
        data.set(propertyName, value);
        Object result = data.get(propertyName);
        assertNotNull(result);
        assertEquals(value, result);
        assertEquals(String.class, result.getClass());
    }

    /**
     * @see  junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        _data0 = new GenericData();
        _data0.set("a", 0);
        _data0.set("b", 1);
        _data0.set("c", 2);
        _data1 = new GenericData();
        _data1.set("a", 0);
        _data1.set("b", 1);
        _data1.set("c", 2);
        _data2 = new GenericData();
        _data2.set("a", 0);
        _data2.set("bb", 1);
        _data2.set("c", 2);
        _data3 = new GenericData();
        _data3.set("a", 0);
        _data3.set("b", "1");
        _data3.set("c", 2);
        _data4 = new GenericData();
        _data4.set("a", 0);
        _data4.set("b", 5);
        _data4.set("c", 2);
    }

    /**
     * @see  junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() {
        _data0 = null;
        _data1 = null;
        _data2 = null;
        _data3 = null;
        _data4 = null;
    }
}
