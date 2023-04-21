package abbot.swt.junit.extensions.test;

import junit.extensions.TestSetup;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import org.eclipse.swt.widgets.Display;
import abbot.swt.junit.extensions.SWTTestSetup;

public class SWTTestSetupTest extends TestCase {

    public SWTTestSetupTest() {
        super();
    }

    public SWTTestSetupTest(String name) {
        super(name);
    }

    private void check(Throwable expected, TestResult result) {
        if (expected == null) {
            AbstractTestCaseTest.check(null, result.failureCount(), result.failures());
            AbstractTestCaseTest.check(null, result.errorCount(), result.errors());
        } else if (expected instanceof AssertionFailedError) {
            AbstractTestCaseTest.check(expected, result.failureCount(), result.failures());
            AbstractTestCaseTest.check(null, result.errorCount(), result.errors());
        } else {
            AbstractTestCaseTest.check(null, result.failureCount(), result.failures());
            AbstractTestCaseTest.check(expected, result.errorCount(), result.errors());
        }
    }

    public void testThreading() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
                assertNull(Display.getCurrent());
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void setUp() throws Exception {
                assertNull(Display.getCurrent());
            }

            protected void tearDown() throws Exception {
                assertNull(Display.getCurrent());
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(null, result);
    }

    public void testCallOrder() {
        final int[] phase = new int[1];
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
                Assert.assertEquals(1, phase[0]);
                phase[0]++;
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void setUp() throws Exception {
                Assert.assertEquals(0, phase[0]);
                phase[0]++;
            }

            protected void tearDown() throws Exception {
                Assert.assertEquals(2, phase[0]);
                phase[0]++;
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(null, result);
    }

    public void testFailureInTestThread() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
                FailuresAndErrors.testFailureInTestThread();
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_FAILURE_IN_TEST_THREAD, result);
    }

    public void testFailureInTestThreadSetUp() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void setUp() throws Exception {
                FailuresAndErrors.testFailureInTestThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_FAILURE_IN_TEST_THREAD, result);
    }

    public void testFailureInTestThreadTearDown() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void tearDown() throws Exception {
                FailuresAndErrors.testFailureInTestThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_FAILURE_IN_TEST_THREAD, result);
    }

    public void testSWTExceptionInTestThread() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
                FailuresAndErrors.testSWTExceptionInTestThread();
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_SWTEXCEPTION_IN_TEST_THREAD, result);
    }

    public void testSWTExceptionInTestThreadSetUp() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void setUp() throws Exception {
                FailuresAndErrors.testSWTExceptionInTestThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_SWTEXCEPTION_IN_TEST_THREAD, result);
    }

    public void testSWTExceptionInTestThreadTearDown() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void tearDown() throws Exception {
                FailuresAndErrors.testSWTExceptionInTestThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_SWTEXCEPTION_IN_TEST_THREAD, result);
    }

    public void testExceptionInTestThread() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
                FailuresAndErrors.testExceptionInTestThread();
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_EXCEPTION_IN_TEST_THREAD, result);
    }

    public void testExceptionInTestThreadSetUp() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void setUp() throws Exception {
                FailuresAndErrors.testExceptionInTestThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_EXCEPTION_IN_TEST_THREAD, result);
    }

    public void testExceptionInTestThreadTearDown() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void tearDown() throws Exception {
                FailuresAndErrors.testExceptionInTestThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_EXCEPTION_IN_TEST_THREAD, result);
    }

    public void testErrorInTestThread() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
                FailuresAndErrors.testErrorInTestThread();
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_ERROR_IN_TEST_THREAD, result);
    }

    public void testErrorInTestThreadSetUp() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void setUp() throws Exception {
                FailuresAndErrors.testErrorInTestThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_ERROR_IN_TEST_THREAD, result);
    }

    public void testErrorInTestThreadTearDown() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void tearDown() throws Exception {
                FailuresAndErrors.testErrorInTestThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_ERROR_IN_TEST_THREAD, result);
    }

    public void testTimeoutInTestThread() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
                FailuresAndErrors.testTimeoutInTestThread();
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_TIMEOUT_IN_TEST_THREAD, result);
    }

    public void testTimeoutInTestThreadSetUp() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void setUp() throws Exception {
                FailuresAndErrors.testTimeoutInTestThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_TIMEOUT_IN_TEST_THREAD, result);
    }

    public void testTimeoutInTestThreadTearDown() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void tearDown() throws Exception {
                FailuresAndErrors.testTimeoutInTestThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_TIMEOUT_IN_TEST_THREAD, result);
    }

    public void testFailureInUIThread() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
                FailuresAndErrors.testFailureInUIThread();
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_FAILURE_IN_UI_THREAD, result);
    }

    public void testFailureInUIThreadSetUp() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void setUp() throws Exception {
                FailuresAndErrors.testFailureInUIThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_FAILURE_IN_UI_THREAD, result);
    }

    public void testFailureInUIThreadTearDown() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void tearDown() throws Exception {
                FailuresAndErrors.testFailureInUIThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_FAILURE_IN_UI_THREAD, result);
    }

    public void testSWTExceptionInUIThread() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
                FailuresAndErrors.testSWTExceptionInUIThread();
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_SWTEXCEPTION_IN_UI_THREAD, result);
    }

    public void testSWTExceptionInUIThreadSetUp() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void setUp() throws Exception {
                FailuresAndErrors.testSWTExceptionInUIThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_SWTEXCEPTION_IN_UI_THREAD, result);
    }

    public void testSWTExceptionInUIThreadTearDown() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void tearDown() throws Exception {
                FailuresAndErrors.testSWTExceptionInUIThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_SWTEXCEPTION_IN_UI_THREAD, result);
    }

    public void testExceptionInUIThread() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
                FailuresAndErrors.testExceptionInUIThread();
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_EXCEPTION_IN_UI_THREAD, result);
    }

    public void testErrorInUIThread() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
                FailuresAndErrors.testErrorInUIThread();
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_ERROR_IN_UI_THREAD, result);
    }

    public void testErrorInUIThreadSetUp() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void setUp() throws Exception {
                FailuresAndErrors.testErrorInUIThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_ERROR_IN_UI_THREAD, result);
    }

    public void testErrorInUIThreadTearDown() {
        Test test = new TestCase() {

            protected void runTest() throws Throwable {
            }
        };
        TestSetup testSetup = new SWTTestSetup(test) {

            protected void tearDown() throws Exception {
                FailuresAndErrors.testErrorInUIThread();
            }
        };
        TestResult result = new TestResult();
        testSetup.run(result);
        check(FailuresAndErrors.EXCEPTION_ERROR_IN_UI_THREAD, result);
    }
}
