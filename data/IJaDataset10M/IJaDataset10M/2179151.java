package org.metaphile.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.metaphile.test.camera.*;
import org.metaphile.test.misc.TestJFIF01;
import org.metaphile.test.misc.TestJFIF02;
import org.metaphile.test.misc.TestJFIF03;
import org.metaphile.test.misc.TestJFIF04;

/**
 * The Metaphile test suite
 * @author stuart
 */
public class AllTests {

    private AllTests() {
    }

    /**
     * The Metaphile test suite
     * @return the junit test
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.metaphile.test");
        suite.addTestSuite(TestJFIF01.class);
        suite.addTestSuite(TestJFIF02.class);
        suite.addTestSuite(TestJFIF03.class);
        suite.addTestSuite(TestJFIF04.class);
        suite.addTestSuite(KodakDC210.class);
        suite.addTestSuite(KodakDC240.class);
        suite.addTestSuite(NikonE950.class);
        suite.addTestSuite(OlympusC960.class);
        suite.addTestSuite(OlympusD320L.class);
        suite.addTestSuite(RicohRDC5300.class);
        suite.addTestSuite(SanyoVPCG250.class);
        suite.addTestSuite(SanyoVPCSX550.class);
        suite.addTestSuite(SonyCyberShot.class);
        suite.addTestSuite(SonyD700.class);
        return suite;
    }
}
