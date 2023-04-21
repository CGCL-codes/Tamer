package test;

import test.newtags.FormsHQLTest;
import test.newtags.ListOQLTest;
import test.tags.FormsOQLTest;
import test.tags.ListHQLTest;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * TestSuite that runs all the Makumba tests
 * @author Stefan Baebler
 * @author Manuel Gay
 */
public class all {

    public static void main(String[] args) {
        System.out.println("Makumba test suite: Running all tests...");
        TestResult tr = junit.textui.TestRunner.run(suite());
        if (!tr.wasSuccessful()) System.exit(1);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("All JUnit Tests for Makumba");
        suite.addTest(config.suite());
        suite.addTest(mdd.suite());
        suite.addTest(table.suite());
        suite.addTest(tableHibernate.suite());
        suite.addTest(FormsHQLTest.suite());
        suite.addTest(ListHQLTest.suite());
        suite.addTest(FormsOQLTest.suite());
        suite.addTest(ListOQLTest.suite());
        return suite;
    }
}
