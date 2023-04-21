package org.fao.geonet.services;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.fao.geonet.services.cgp.CGPServerTest;
import org.fao.geonet.services.harvesting.HarvestTest;
import org.fao.geonet.services.metadata.MetaDataTest;
import jeeves.test.SuiteRunner;

/**
 * Runs all JUnit tests for GN services.
 *
 * @author Just van den Broecke
 */
public class AllTests extends SuiteRunner {

    public static void main(String[] args) {
        new AllTests().run();
        System.exit(0);
    }

    /**
	 * Create the test suite.
	 */
    protected Test createSuite() {
        TestSuite suite = new TestSuite("Service Tests");
        suite.addTestSuite(CGPServerTest.class);
        suite.addTestSuite(HarvestTest.class);
        return suite;
    }
}
