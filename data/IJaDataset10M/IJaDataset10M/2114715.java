package org.vizzini.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import java.util.logging.Logger;

/**
 * Provides unit tests for the <code>Sample</code> class.
 *
 * <p>By default, all test methods (methods with a <code>@Test</code>
 * annotation) are run.  Specify individual test methods to ignore using the
 * <code>@Ignore</code> annotation. See the references below to run individual
 * tests from the command line.</p>
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @see      <a href="http://junit.sourceforge.net/doc/faq/faq.htm">JUnit
 *           FAQ</a>
 * @see      <a href="http://junit.sourceforge.net/doc/cookbook/cookbook.htm">JUnit
 *           Cookbook</a>
 * @since    v0.4
 */
public class SampleTest {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(SampleTest.class.getName());

    /**
     * Test the <code>doSomething()</code> method.
     *
     * @since  v0.4
     */
    @Test
    public void doSomething() {
        LOGGER.finest("doSomething");
        Object object = new Long(123L);
        assertThat(object, is(not(nullValue())));
    }

    /**
     * Sets up the test fixture. (Called before every test case method.)
     *
     * @since  v0.4
     */
    @Before
    public void setUp() {
    }

    /**
     * Tears down the test fixture. (Called after every test case method.)
     *
     * @since  v0.4
     */
    @After
    public void tearDown() {
    }
}
