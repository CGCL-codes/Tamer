package com.google.api.adwords.lib;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Enumerates all {@code com.google.api.adwords.lib} tests.
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class LibTestSuite extends TestSuite {

    /**
   * Returns a test suite for all lib tests.
   *
   * @return a test suite for all lib tests.
   */
    public static Test suite() {
        TestSuite suite = new TestSuite("Lib tests");
        suite.addTestSuite(AdWordsV200909SmokeTest.class);
        suite.addTestSuite(AdWordsV13SmokeTest.class);
        suite.addTestSuite(AdWordsServiceFactoryTest.class);
        suite.addTestSuite(ServiceAccountantTest.class);
        suite.addTestSuite(ServiceAccountantManagerTest.class);
        return suite;
    }
}
