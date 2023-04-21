package net.sourceforge.toscanaj.controller.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

    public AllTests(String s) {
        super(s);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(net.sourceforge.toscanaj.controller.cernato.tests.AllTests.suite());
        suite.addTest(net.sourceforge.toscanaj.controller.fca.tests.AllTests.suite());
        return suite;
    }
}
