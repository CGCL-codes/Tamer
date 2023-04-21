package org.apache.cactus.sample;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run all the Sample Servlet API 2.3 Cactus tests.
 *
 * @author <a href="mailto:vmassol@apache.org">Vincent Massol</a>
 *
 * @version $Id: TestAll.java,v 1.4 2002/07/26 17:37:17 kaila Exp $
 */
public class TestAll extends TestCase {

    /**
     * Defines the testcase name for JUnit.
     *
     * @param theName the testcase's name.
     */
    public TestAll(String theName) {
        super(theName);
    }

    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs) {
        junit.swingui.TestRunner.main(new String[] { TestAll.class.getName() });
    }

    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Sample Servlet API 2.3 Cactus tests");
        suite.addTest(org.apache.cactus.sample.TestSampleTag.suite());
        suite.addTest(org.apache.cactus.sample.TestSampleBodyTag.suite());
        return suite;
    }
}
