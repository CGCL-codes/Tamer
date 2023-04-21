package org.jazzteam.jpatterns.core.configuration.exceptions;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.jazzteam.jpatterns.core.JPException;

/**
 * $Author:: zmicer $<br/>
 * $Rev:: 67 $<br/>
 * * $Date:: 2007-08-28 21:37:07 #$<br/>
 * $Date:: 2007-08-28 21:37:07 #$<br/>
 */
public class JPInitializationExceptionTest extends TestCase {

    /**
	 * Logger instance.
	 */
    public static final Logger LOG = Logger.getLogger(JPInitializationExceptionTest.class);

    /**
	 * Instance of the covered class to be used below in the tests
	 */
    private JPInitializationException m_jpInitializationException;

    /**
	 * Contructor with name of test attribute.
	 * 
	 * @param name
	 *            name of the test
	 */
    public JPInitializationExceptionTest(final String name) {
        super(name);
    }

    /**
	 * Perform the set up functionality for the test.
	 * 
	 * @throws Exception
	 *             may occur in the case of some problems
	 */
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * Perform the tear down functionality for the test
	 * 
	 * @throws Exception
	 *             may occur in the case of some problems
	 */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test suite method
	 * 
	 * @return the built test suite
	 */
    public static Test suite() {
        return new TestSuite(JPInitializationExceptionTest.class);
    }

    /**
	 * Tests {@link JPInitializationException#JPInitializationException}
	 * 
	 * @throws Exception
	 *             in the case smth. wrong occuried.
	 * @forversion 1.0
	 */
    public void testJPInitializationException() throws Exception {
        JPException jpException = new JPInitializationException();
        jpException = new JPInitializationException("Checking the exception");
        jpException = new JPInitializationException(new Exception("Checking the exception"));
        jpException = new JPInitializationException("Checking the exception", new Exception("Checking the exception"));
    }
}
