package com.bm.testsuite;

import java.util.Collection;
import org.jmock.MockObjectTestCase;
import com.bm.utils.BeanEqualsTester;

/**
 * Superclass of all EJB3unit methods. An extension of junit.framework.TestCase
 * that adds those methods that we really wish were part of JUnit.
 * 
 * @author Daniel Wiese
 * @since 09.02.2006
 */
public class BaseTest extends MockObjectTestCase {

    /**
	 * Create an instance.
	 * 
	 * @param name
	 *            The name of the test
	 */
    public BaseTest(final String name) {
        super(name);
    }

    /**
	 * Default costructor.
	 */
    public BaseTest() {
        super();
    }

    /**
	 * Assert that the two collections are the same irrespective of order.
	 * 
	 * @param a
	 *            The first collection
	 * @param b
	 *            The second collection
	 * @param <T> -
	 *            the type of the collection
	 */
    public <T> void assertCollectionsEqual(final Collection<? extends T> a, final Collection<? extends T> b) {
        BeanEqualsTester.testColletionsForEqual(a, b);
    }

    /**
	 * Assert that the specified condition is false. Older versions of junit
	 * have assertTrue() but not assertFalse so we add it here to be sure that
	 * it is present.
	 * 
	 * @param description
	 *            The failure message to be used if the condition is not false.
	 * @param condition
	 *            The value to check.
	 */
    public static void assertFalse(final String description, final boolean condition) {
        if (condition) {
            fail(description + ": Expected false");
        }
    }

    /**
	 * Assert that the specified condition is false. Older versions of junit
	 * have assertTrue() but not assertFalse so we add it here to be sure that
	 * it is present.
	 * 
	 * @param condition
	 *            The value to check.
	 */
    public static void assertFalse(final boolean condition) {
        if (condition) {
            fail("Expected false");
        }
    }

    /**
	 * Assert that the specified object is an instance of this class.
	 * 
	 * @param label
	 *            A description of the test
	 * @param object
	 *            The object to test
	 * @param clazz
	 *            The class
	 * @param <T> -
	 *            the type of the object
	 */
    public <T> void assertInstanceOf(final String label, final T object, final Class<? extends T> clazz) {
        if (!clazz.isAssignableFrom(object.getClass())) {
            fail(label + ": object [" + object + "] is not an instance of class [" + clazz.getName() + "]");
        }
    }

    /**
	 * Assert that the specified object is an instance of this class.
	 * 
	 * @param object
	 *            The object to test
	 * @param clazz
	 *            The class
	 * @param <T> -
	 *            the type of the object
	 */
    public <T> void assertInstanceOf(final T object, final Class<? extends T> clazz) {
        this.assertInstanceOf("", object, clazz);
    }

    /**
	 * We don�t want that test fails because htey have no test method.
	 * 
	 * @author Daniel Wiese
	 * @since 23.04.2006
	 */
    public void testNothing() {
    }
}
