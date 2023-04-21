package lcd_api.dynamic_values;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This is a test suite containing automatic JUnit tests for all of the 
 * DynamicValues Executing this test will execute all of the sub tests in one 
 * shot.
 * 
 * @author Robert Derelanko
 */
public class DynamicValueTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Dynamic Value Tests");
        suite.addTestSuite(DynamicValueTest.class);
        suite.addTestSuite(NumberDynamicValueTest.class);
        suite.addTestSuite(StringDynamicValueTest.class);
        suite.addTestSuite(DynamicValueFormatterTest.class);
        return suite;
    }
}
