package org.enerj.apache.commons.beanutils;

import org.enerj.apache.commons.beanutils.DynaProperty;
import org.enerj.apache.commons.beanutils.LazyDynaClass;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Test Case for the <code>LazyDynaClass</code> implementation class.</p>
 *
 * @author Niall Pemberton
 */
public class LazyDynaClassTestCase extends TestCase {

    protected LazyDynaClass dynaClass = null;

    protected String testProperty = "myProperty";

    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public LazyDynaClassTestCase(String name) {
        super(name);
    }

    /**
     * Run this Test
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        dynaClass = new LazyDynaClass();
    }

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(LazyDynaClassTestCase.class));
    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        dynaClass = null;
    }

    /**
     * Test add(name) method
     */
    public void testAddProperty1() {
        dynaClass.add(testProperty);
        DynaProperty dynaProperty = dynaClass.getDynaProperty(testProperty);
        assertEquals("name is correct", testProperty, dynaProperty.getName());
        assertEquals("type is correct", Object.class, dynaProperty.getType());
    }

    /**
     * Test add(name, type) method
     */
    public void testAddProperty2() {
        dynaClass.add(testProperty, String.class);
        DynaProperty dynaProperty = dynaClass.getDynaProperty(testProperty);
        assertEquals("name is correct", testProperty, dynaProperty.getName());
        assertEquals("type is correct", String.class, dynaProperty.getType());
    }

    /**
     * Test add(name, type, readable, writable) method
     */
    public void testAddProperty3() {
        try {
            dynaClass.add(testProperty, String.class, true, true);
            fail("add(name, type, readable, writable) did not throw UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
        }
    }

    /**
     * Test add(name) method with 'null' name
     */
    public void testAddPropertyNullName1() {
        try {
            dynaClass.add((String) null);
            fail("null property name not prevented");
        } catch (IllegalArgumentException expected) {
        }
    }

    /**
     * Test add(name, type) method with 'null' name
     */
    public void testAddPropertyNullName2() {
        try {
            dynaClass.add(null, String.class);
            fail("null property name not prevented");
        } catch (IllegalArgumentException expected) {
        }
    }

    /**
     * Test add(name, type, readable, writable) method with 'null' name
     */
    public void testAddPropertyNullName3() {
        try {
            dynaClass.add(null, String.class, true, true);
            fail("add(name, type, readable, writable) did not throw UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
        }
    }

    /**
     * Test add(name) method when restricted is set to 'true'
     */
    public void testAddPropertyRestricted1() {
        dynaClass.setRestricted(true);
        assertTrue("MutableDynaClass is restricted", dynaClass.isRestricted());
        try {
            dynaClass.add(testProperty);
            fail("add(name) did not throw IllegalStateException");
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * Test add(name, type) method when restricted is set to 'true'
     */
    public void testAddPropertyRestricted2() {
        dynaClass.setRestricted(true);
        assertTrue("MutableDynaClass is restricted", dynaClass.isRestricted());
        try {
            dynaClass.add(testProperty, String.class);
            fail("add(name, type) did not throw IllegalStateException");
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * Test add(name, type, readable, writable) method when restricted is set to 'true'
     */
    public void testAddPropertyRestricted3() {
        dynaClass.setRestricted(true);
        assertTrue("MutableDynaClass is restricted", dynaClass.isRestricted());
        try {
            dynaClass.add(testProperty, String.class, true, true);
            fail("add(name, type, readable, writable) did not throw UnsupportedOperationException");
        } catch (UnsupportedOperationException t) {
        }
    }

    /**
     * Test retrieving a property which doesn't exist (returnNull is 'false')
     */
    public void testGetPropertyDoesntExist1() {
        dynaClass.setReturnNull(false);
        assertFalse("returnNull is 'false'", dynaClass.isReturnNull());
        DynaProperty dynaProperty = dynaClass.getDynaProperty(testProperty);
        assertEquals("name is correct", testProperty, dynaProperty.getName());
        assertEquals("type is correct", Object.class, dynaProperty.getType());
        assertFalse("property doesnt exist", dynaClass.isDynaProperty(testProperty));
    }

    /**
     * Test retrieving a property which doesn't exist (returnNull is 'true')
     */
    public void testGetPropertyDoesntExist2() {
        dynaClass.setReturnNull(true);
        assertTrue("returnNull is 'true'", dynaClass.isReturnNull());
        assertNull("property is null", dynaClass.getDynaProperty(testProperty));
    }

    /**
     * Test removing a property
     */
    public void testRemoveProperty() {
        dynaClass.setReturnNull(true);
        dynaClass.add(testProperty);
        assertTrue("Property exists", dynaClass.isDynaProperty(testProperty));
        assertNotNull("property is Not null", dynaClass.getDynaProperty(testProperty));
        dynaClass.remove(testProperty);
        assertFalse("Property doesn't exist", dynaClass.isDynaProperty(testProperty));
        assertNull("property is null", dynaClass.getDynaProperty(testProperty));
    }

    /**
     * Test removing a property, name is null
     */
    public void testRemovePropertyNullName() {
        try {
            dynaClass.remove(null);
            fail("remove(null) did not throw IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    /**
     * Test removing a property, DynaClass is restricted
     */
    public void testRemovePropertyRestricted() {
        dynaClass.add(testProperty);
        assertTrue("Property exists", dynaClass.isDynaProperty(testProperty));
        dynaClass.setRestricted(true);
        assertTrue("MutableDynaClass is restricted", dynaClass.isRestricted());
        try {
            dynaClass.remove(testProperty);
            fail("remove property when MutableDynaClassis restricted did not throw IllegalStateException");
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * Test removing a property which doesn't exist
     */
    public void testRemovePropertyDoesntExist() {
        assertFalse("property doesn't exist", dynaClass.isDynaProperty(testProperty));
        dynaClass.remove(testProperty);
        assertFalse("property still doesn't exist", dynaClass.isDynaProperty(testProperty));
    }
}
