package framework.transformation;

import junit.framework.Test;
import junit.framework.TestSuite;

public class BundledTransformationTests {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test suite for transformation");
        suite.addTestSuite(DebugTransformationsTest.class);
        suite.addTestSuite(AnonymousIdUtilsTest.class);
        return suite;
    }
}
