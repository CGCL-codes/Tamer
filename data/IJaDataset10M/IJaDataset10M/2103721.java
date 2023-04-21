package rbsla.regressiontest;

import junit.framework.TestSuite;
import rbsla.wrapper.ProvaWrapper;
import java.util.List;

/**
 * Test suite for a automatic regression tests of the RBSLA/ConractLog framework.
 * The system is able to automaticly execute the tests and create a test report using JUnit.
 * 
 * @author <A HREF="mailto:paschke@in.tum.de">Adrian Paschke</A>
 * @version 0.1 <1 Sept 2005>
 * @since 0.1
 */
public class RegressionTestSuite {

    /**
     * Get a test suite. This suite is the result of merging
     * all test cases defined in the ./rules/test/tests.prova script.
     * @return a test suite
     */
    public static TestSuite suite() {
        TestSuite suite = new TestSuite("All RBSLA test cases");
        ProvaWrapper wrapper = new ProvaWrapper("./examples/test_cases/tests.prova");
        List testCases = wrapper.solveVar("test(TestCase)", "TestCase");
        for (int i = 0; i < testCases.size(); i++) {
            try {
                String testCase = testCases.get(i).toString();
                RegressionTest test = new RegressionTest(testCase);
                suite.addTest(test);
            } catch (Exception x) {
                System.err.println("Cannot create merged test suite!");
                x.printStackTrace(System.err);
            }
        }
        return suite;
    }
}
