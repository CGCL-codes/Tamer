package mars4stars.unused;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for default package");
        suite.addTestSuite(FirstTest.class);
        return suite;
    }
}
