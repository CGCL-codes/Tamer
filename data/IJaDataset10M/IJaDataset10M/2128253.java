package velocidoc;

import junit.framework.*;

public class AllTests {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite("Core Library Suite");
        suite.addTest(velocidoc.Test.suite());
        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }
}
