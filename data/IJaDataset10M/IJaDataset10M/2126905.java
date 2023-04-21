package tests.com.scholardesk.rss;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Utilities Tests");
        suite.addTestSuite(RSSFeedTestCase.class);
        suite.addTestSuite(AtomFeedTestCase.class);
        return suite;
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}
