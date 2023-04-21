package com.aptana.ide.core.tests;

import java.io.InputStream;
import com.aptana.ide.core.StreamUtils;

/**
 * StreamUtilsTest is a unit test class for class StreamUtils.
 * 
 * @see com.aptana.ide.core.StreamUtils
 * @author Parasoft Jtest 7.5
 */
public class StreamUtilsTest extends PackageTestCase {

    /**
	 * Constructs a test case for the test specified by the name argument.
	 * 
	 * @param name
	 *            the name of the test case
	 * @author Parasoft Jtest 7.5
	 */
    public StreamUtilsTest(String name) {
        super(name);
    }

    /**
	 * Test for method: static readContent(java.io.InputStream,java.lang.String)
	 * 
	 * @throws Throwable
	 *             Tests may throw any Throwable
	 * @see StreamUtils#readContent(java.io.InputStream,java.lang.String)
	 * @author Parasoft Jtest 7.5
	 */
    public void testReadContent1() throws Throwable {
        StreamUtils.readContent((InputStream) null, (String) null);
    }

    /**
	 * Test for method: static readContent(java.io.InputStream,java.lang.String)
	 * 
	 * @throws Throwable
	 *             Tests may throw any Throwable
	 * @see StreamUtils#readContent(java.io.InputStream,java.lang.String)
	 * @author Parasoft Jtest 7.5
	 */
    public void testReadContent2() throws Throwable {
        StreamUtils.readContent((InputStream) null, "");
    }

    /**
	 * Used to set up the test. This method is called by JUnit before each of the tests are
	 * executed.
	 * 
	 * @see junit.framework.TestCase#setUp()
	 * @author Parasoft Jtest 7.5
	 */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * Used to clean up after the test. This method is called by JUnit after each of the tests have
	 * been completed.
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 * @author Parasoft Jtest 7.5
	 */
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Utility main method. Runs the test cases defined in this test class. Usage: java
	 * com.aptana.ide.core.tests.StreamUtilsTest
	 * 
	 * @param args
	 *            command line arguments are not needed
	 * @author Parasoft Jtest 7.5
	 */
    public static void main(String[] args) {
    }

    /**
	 * Get the class object of the class which will be tested.
	 * 
	 * @return the class which will be tested
	 * @author Parasoft Jtest 7.5
	 */
    public Class getTestedClass() {
        return StreamUtils.class;
    }
}
