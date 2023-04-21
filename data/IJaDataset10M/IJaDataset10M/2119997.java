package openvend.io;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Thomas Weckert
 * @version $Revision: 1.2 $
 */
public class AllTests {

    /**
     * Returns the JUnit test suite for this package.<p>
     * 
     * @return the JUnit test suite for this package
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for package " + AllTests.class.getPackage().getName());
        suite.addTest(new TestSuite(OvFileCacheTestCase.class));
        return suite;
    }
}
