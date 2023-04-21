package org.hl7.hpath;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PackageTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("org.hl7.hpath");
        suite.addTest(new TestSuite(HPathTest.class));
        suite.addTest(new TestSuite(UpdateTest.class));
        suite.addTest(new TestSuite(TypeTest.class));
        return suite;
    }
}
