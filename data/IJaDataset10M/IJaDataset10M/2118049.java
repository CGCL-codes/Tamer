package com.gargoylesoftware.base.testing;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import junit.framework.TestCase;

/**
 * An extension of junit.framework.TestCase that adds those methods that we really
 * wish were part of JUnit.
 *
 * @version  $Revision: 1.4 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class BaseTestCase extends TestCase {

    /**
     * Create an instance
     * @param name The name of the test
     */
    public BaseTestCase(final String name) {
        super(name);
    }

    /**
     * Convenience method to signal that this test hasn't been finished yet.  This
     * will print the name of the test to System.out.
     */
    public void notImplemented() {
        System.out.println("Test not implemented yet [" + getClass().getName() + " : " + getName() + "]");
    }

    /**
     * Assert that the two collections are the same irrespective of order.
     *
     * @param  a The first collection
     * @param  b The second collection
     */
    public void assertCollectionsEqual(final Collection a, final Collection b) {
        final Collection copyOfB = new LinkedList(b);
        final Iterator iterator = a.iterator();
        while (iterator.hasNext()) {
            final Object object = iterator.next();
            if (copyOfB.contains(object) == false) {
                fail("Expected: " + a + " but got: " + b);
            }
            copyOfB.remove(object);
        }
        if (copyOfB.isEmpty() == false) {
            fail("Second collection has elements that aren't in the first collection: " + copyOfB);
        }
    }

    /**
     * Assert that the two objects are the same.  Junit has a method like this
     * however it does not display what the two objects are.  This method will
     * display the toString() representations of the two objects in the case that
     * the assertion fails.
     *
     * @param description The failure message to use if the two objects are not the same.
     * @param a The first object to compare.
     * @param b The second object to compare.
     */
    public static void assertSame(final String description, final Object a, final Object b) {
        if (a != b) {
            fail(description + ": Objects not the same <" + a + "> and <" + b + ">");
        }
    }

    /**
     * Assert that the two objects are the same.  Junit has a method like this
     * however it does not display what the two objects are.  This method will
     * display the toString() representations of the two objects in the case that
     * the assertion fails.
     *
     * @param a The first object to compare.
     * @param b The second object to compare.
     */
    public static void assertSame(final Object a, final Object b) {
        if (a != b) {
            fail("Objects not the same <" + a + "> and <" + b + ">");
        }
    }

    /**
     * Assert that the specified condition is false.  Older versions of junit have assertTrue()
     * but not assertFalse so we add it here to be sure that it is present.
     *
     * @param description The failure message to be used if the condition is not false.
     * @param condition The value to check.
     */
    public static void assertFalse(final String description, final boolean condition) {
        if (condition == true) {
            fail(description + ": Expected false");
        }
    }

    /**
     * Assert that the specified condition is false.  Older versions of junit have assertTrue()
     * but not assertFalse so we add it here to be sure that it is present.
     *
     * @param condition The value to check.
     */
    public static void assertFalse(final boolean condition) {
        if (condition == true) {
            fail("Expected false");
        }
    }

    /**
     * Assert that the specified object is an instance of this class
     *
     * @param label A description of the test
     * @param object The object to test
     * @param clazz The class
     */
    public void assertInstanceOf(final String label, final Object object, final Class clazz) {
        if (clazz.isAssignableFrom(object.getClass()) == false) {
            fail(label + ": object [" + object + "] is not an instance of class [" + clazz.getName() + "]");
        }
    }

    /**
     * Assert that the specified object is an instance of this class
     *
     * @param object The object to test
     * @param clazz The class
     */
    public void assertInstanceOf(final Object object, final Class clazz) {
        if (clazz.isAssignableFrom(object.getClass()) == false) {
            fail("object [" + object + "] is not an instance of class [" + clazz.getName() + "]");
        }
    }
}
