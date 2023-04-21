package CH.ifa.draw.test.framework;

import junit.framework.TestSuite;

public class FrameworkSuite {

    public static TestSuite suite() {
        TestSuite suite;
        suite = new TestSuite("CH.ifa.draw.test.framework");
        suite.addTestSuite(CH.ifa.draw.test.framework.FigureAttributeConstantTest.class);
        suite.addTestSuite(CH.ifa.draw.test.framework.FigureChangeEventTest.class);
        suite.addTestSuite(CH.ifa.draw.test.framework.DrawingChangeEventTest.class);
        return suite;
    }

    /**
	* Method to execute the TestSuite from command line
	* using JUnit's textui.TestRunner .
	*/
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
