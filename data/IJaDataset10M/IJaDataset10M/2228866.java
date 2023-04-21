package com.google.test.jinjector;

import j2meunit.framework.Test;
import j2meunit.framework.TestCase;
import j2meunit.framework.TestMethod;
import j2meunit.framework.TestResult;
import j2meunit.framework.TestSuite;

/**
 * Unit tests for {@link RegressionTestRunnerTest}
 * 
 * @author Michele Sama
 *
 */
public class RegressionTestRunnerTest extends TestCase {

    private static final int NUM_TESTS_IN_SUITE = 10;

    /**
   * Creates an instance of this class.
   */
    public RegressionTestRunnerTest() {
    }

    /**
   * Creates an instance of this class which will execute the 
   * specified {@link TestMethod}.
   * 
   * @param name The name to assign to this test.
   * @param method The {@link TestMethod} to execute
   */
    public RegressionTestRunnerTest(String name, TestMethod method) {
        super(name, method);
    }

    /**
   * Creates an instance of this class.
   * 
   * @param name The name to assign to this test.
   */
    public RegressionTestRunnerTest(String name) {
        super(name);
    }

    /**
   * Tests {@link RegressionTestRunner#doRun(Test)}.
   * 
   * <p> This test creates a test suite and verifies that all the tests have 
   * executed and that all have performed both {@link TestCase#setUp()} and 
   * {@link TestCase#tearDown()}.
   */
    public void testDoRun_allTestRunned() {
        final TestSuite suite = createPassingSuite();
        AssistedTestCase test = new AssistedTestCase() {

            public Test suite() {
                return suite;
            }
        };
        try {
            runAndWaitUntilTermination(test);
        } catch (InterruptedException e) {
            fail("The runner has been interrupted before termination: " + e.getMessage());
        }
        for (int i = 0; i < NUM_TESTS_IN_SUITE; i++) {
            FlaggableTestCase executedTest = (FlaggableTestCase) suite.testAt(i);
            assertTrue("Test has not been settedUp.", executedTest.wasSetUp());
            assertTrue("Test has not been executed.", executedTest.wasRun());
            assertTrue("Test has not been tearedDown.", executedTest.wasTornDown());
        }
    }

    /**
   * Tests {@link RegressionTestRunner#doRun(Test)}.
   * 
   * <p> This test creates a test suite and verifies that all the tests have 
   * executed and that all of them have passed.
   */
    public void testDoRun_passed() {
        final TestSuite suite = createPassingSuite();
        AssistedTestCase test = new AssistedTestCase() {

            public Test suite() {
                return suite;
            }
        };
        try {
            runAndWaitUntilTermination(test);
        } catch (InterruptedException e) {
            fail("The runner has been interrupted before termination: " + e.getMessage());
        }
        for (int i = 0; i < NUM_TESTS_IN_SUITE; i++) {
            FlaggableTestCase executedTest = (FlaggableTestCase) suite.testAt(i);
            assertTrue("Test has not been executed.", executedTest.wasRun());
            assertTrue("Test has not passed.", !executedTest.hasFailed());
        }
    }

    /**
   * Tests {@link RegressionTestRunner#doRun(Test)}.
   * 
   * <p> This test creates a test suite and verifies that all the tests have 
   * executed and that all of them have failed.
   */
    public void testDoRun_failure() {
        final TestSuite suite = createFailingSuite();
        AssistedTestCase test = new AssistedTestCase() {

            public Test suite() {
                return suite;
            }
        };
        try {
            runAndWaitUntilTermination(test);
        } catch (InterruptedException e) {
            fail("The runner has been interrupted before termination: " + e.getMessage());
        }
        for (int i = 0; i < NUM_TESTS_IN_SUITE; i++) {
            FlaggableTestCase executedTest = (FlaggableTestCase) suite.testAt(i);
            assertTrue("Test has not been executed.", executedTest.wasRun());
            assertTrue("Test has not failed.", executedTest.hasFailed());
            assertTrue("Test has not been tearedDown.", executedTest.wasTornDown());
        }
    }

    /**
   * Tests {@link RegressionTestRunner#displayResult()} by checking that it 
   * will be invoked internally.
   */
    public void testDisplayResult_isInvoked() {
        AssistedTestCase test = new AssistedTestCase() {

            public Test suite() {
                TestSuite suite = new TestSuite();
                suite.addTest(FlaggableTestCase.createPassingTest());
                return suite;
            }
        };
        FlaggableStrategy strategy = new FlaggableStrategy();
        RegressionTestRunner runner = new RegressionTestRunner(test, strategy);
        try {
            runAndWaitUntilTermination(runner);
        } catch (InterruptedException e) {
            fail("The runner has been interrupted before termination: " + e.getMessage());
        }
        assertTrue("displayResult() has not been invoked.", strategy.hasDisplayedResults);
    }

    /**
   * Creates a {@link TestSuite} containing {@link #NUM_TESTS_IN_SUITE} tests 
   * which will automatically pass.
   * 
   * <p> All the tests in the suite will be instances of 
   * {@link FlaggableTestCase}.
   * 
   * @return a {@link TestSuite}.
   */
    public static TestSuite createPassingSuite() {
        TestSuite suite = new TestSuite();
        for (int i = 0; i < NUM_TESTS_IN_SUITE; i++) {
            suite.addTest(FlaggableTestCase.createPassingTest());
        }
        return suite;
    }

    /**
   * Creates a {@link TestSuite} containing {@link #NUM_TESTS_IN_SUITE} tests 
   * which will automatically fail.
   * 
   * <p> All the tests in the suite will be instances of 
   * {@link FlaggableTestCase}.
   * 
   * @return a {@link TestSuite}.
   */
    public static TestSuite createFailingSuite() {
        TestSuite suite = new TestSuite();
        for (int i = 0; i < NUM_TESTS_IN_SUITE; i++) {
            suite.addTest(FlaggableTestCase.createFailingTest());
        }
        return suite;
    }

    /**
   * Runs a {@link RegressionTestRunner} with the given test and waits for its 
   * termination.
   * 
   * @param test The {@link TestCase} to be executed.
   * @throws InterruptedException if the test is interrupted while waiting.
   */
    public static void runAndWaitUntilTermination(AssistedTestCase test) throws InterruptedException {
        runAndWaitUntilTermination(new RegressionTestRunner(test));
    }

    /**
   * Runs a {@link RegressionTestRunner} and waits for its termination.
   * 
   * <p> The delay is applied at least once to be sure that the runner has 
   * been started.
   * 
   * @param runner The {@link RegressionTestRunner} to be executed.
   * @throws InterruptedException if the test is interrupted while waiting.
   */
    public static void runAndWaitUntilTermination(RegressionTestRunner runner) throws InterruptedException {
        runner.start();
        do {
            Thread.sleep(200);
        } while (runner.isExecuting());
    }

    /**
   * Flags the {@link ResultDisplayerStrategy} when 
   * {@link #displayResult(TestResult)} is invoked.
   * 
   * <p> This is required by 
   * {@link RegressionTestRunnerTest#testDisplayResult_isInvoked()}.
   * 
   * @author Michele Sama
   */
    private class FlaggableStrategy implements ResultDisplayerStrategy {

        /**
     * Flags any invocation of the {@link #displayResult(TestResult)} method. 
     * <code>false</code> by default, <code>true</code> if the method has 
     * been invoked.  
     */
        public boolean hasDisplayedResults = false;

        /**
     * Overrides the superclass' method by flagging the first time it is 
     * invoked.
     * 
     * @see ResultDisplayerStrategy#displayResult(TestResult)
     */
        public void displayResult(TestResult result) {
            hasDisplayedResults = true;
        }
    }

    /**
   * @see j2meunit.framework.TestCase#suite()
   */
    public Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new RegressionTestRunnerTest("testDisplayResult_isInvoked") {

            public void runTest() {
                testDisplayResult_isInvoked();
            }
        });
        suite.addTest(new RegressionTestRunnerTest("testDoRun_allTestRunned") {

            public void runTest() {
                testDoRun_allTestRunned();
            }
        });
        suite.addTest(new RegressionTestRunnerTest("testDoRun_failure") {

            public void runTest() {
                testDoRun_failure();
            }
        });
        suite.addTest(new RegressionTestRunnerTest("testDoRun_passed") {

            public void runTest() {
                testDoRun_passed();
            }
        });
        return suite;
    }
}
