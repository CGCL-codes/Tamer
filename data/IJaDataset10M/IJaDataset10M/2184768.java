package org.gaelocaltest.ext;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Suite of all tests.
 *
 * @author 403rus@google.com (Dmitriy Trubenkov)
 */
public class AllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ServletRunnerTest.class);
        return suite;
    }
}
