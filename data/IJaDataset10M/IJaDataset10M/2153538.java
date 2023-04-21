package jbreport.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The class that consolidates all the tests in the package.
 *
 * @author Grant Finnemore
 * @version $Revision: 1.1.1.1 $
 */
public class SuiteOfTests {

    static {
        try {
            Class.forName("jbreport.util.MemoryTrack");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(DatasourceTest.class));
        suite.addTest(new TestSuite(ParseTest.class));
        suite.addTest(new TestSuite(ExpandTest.class));
        return suite;
    }
}
