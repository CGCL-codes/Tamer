package abbot.swt.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllTests.class.getPackage().getName());
        suite.addTestSuite(AbbotTest.class);
        suite.addTestSuite(LogTest.class);
        return suite;
    }
}
