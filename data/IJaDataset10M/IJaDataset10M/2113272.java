package j2meunit.textui;

import j2meunit.framework.AssertionFailedError;
import j2meunit.framework.Test;
import j2meunit.framework.TestCase;
import j2meunit.framework.TestFailure;
import j2meunit.framework.TestListener;
import j2meunit.framework.TestResult;
import j2meunit.util.StringUtil;
import j2meunit.util.Version;
import java.io.PrintStream;
import java.util.Enumeration;

/********************************************************************
 * A command line based tool to run tests.
 * <pre>
 * java test.textui.TestRunner TestCaseClass
 * </pre>
 * TestRunner expects the name of a TestCase class as argument. If this class
 * defines a static <code>suite</code> method it  will be invoked and the
 * returned test is run. Otherwise all  the methods starting with "test"
 * having no arguments are run.
 * 
 * <p>
 * When the wait command line argument is given TestRunner waits until the
 * users types RETURN.
 * </p>
 * 
 * <p>
 * TestRunner prints a trace as the tests are executed followed by a summary at
 * the end.
 * </p>
 */
public class TestRunner implements TestListener {

    protected PrintStream fWriter = System.out;

    /***************************************
	 * Default constructor.
	 */
    public TestRunner() {
    }

    /***************************************
	 * Constructor with a specific output stream.
	 *
	 * @param writer The PrintStream to write the results to
	 */
    public TestRunner(PrintStream writer) {
        fWriter = writer;
    }

    /***************************************
	 * DOCUMENT ME!
	 *
	 * @param test DOCUMENT ME!
	 * @param t DOCUMENT ME!
	 */
    public synchronized void addError(Test test, Throwable t) {
        fWriter.print("E");
    }

    /***************************************
	 * DOCUMENT ME!
	 *
	 * @param test DOCUMENT ME!
	 * @param e DOCUMENT ME!
	 */
    public synchronized void addFailure(Test test, AssertionFailedError e) {
        fWriter.print("F");
    }

    /***************************************
	 * DOCUMENT ME!
	 *
	 * @param test DOCUMENT ME!
	 */
    public void endTest(Test test) {
    }

    /***************************************
	 * TestListener.endTestStep()
	 *
	 * @param test The test of which a step has finished
	 */
    public void endTestStep(Test test) {
        fWriter.print(".");
    }

    /***************************************
	 * main entry point.
	 *
	 * @param args DOCUMENT ME!
	 */
    public static void main(String[] args) {
        System.out.println("TestRunner.main()");
        TestRunner aTestRunner = new TestRunner();
        aTestRunner.start(args);
    }

    /***************************************
	 * Prints failures to the standard output
	 *
	 * @param result DOCUMENT ME!
	 */
    public synchronized void print(TestResult result) {
        printHeader(result);
        printErrors(result);
        printFailures(result);
    }

    /***************************************
	 * Prints the errors to the standard output
	 *
	 * @param result DOCUMENT ME!
	 */
    public void printErrors(TestResult result) {
        if (result.errorCount() != 0) {
            if (result.errorCount() == 1) fWriter.println("There was " + result.errorCount() + " error:"); else fWriter.println("There were " + result.errorCount() + " errors:");
            int i = 1;
            for (Enumeration e = result.errors(); e.hasMoreElements(); i++) {
                TestFailure failure = (TestFailure) e.nextElement();
                fWriter.println(i + ") " + failure.failedTest());
                fWriter.println(failure.thrownException().getMessage());
            }
        }
    }

    /***************************************
	 * Prints failures to the standard output
	 *
	 * @param result DOCUMENT ME!
	 */
    public void printFailures(TestResult result) {
        if (result.failureCount() != 0) {
            if (result.failureCount() == 1) fWriter.println("There was " + result.failureCount() + " failure:"); else fWriter.println("There were " + result.failureCount() + " failures:");
            int i = 1;
            for (Enumeration e = result.failures(); e.hasMoreElements(); i++) {
                TestFailure failure = (TestFailure) e.nextElement();
                fWriter.print(i + ") " + failure.failedTest());
                Throwable t = failure.thrownException();
                if (t.getMessage() != null) fWriter.println(" \"" + StringUtil.truncate(t.getMessage(), 80) + "\""); else {
                    fWriter.println();
                    fWriter.println(failure.thrownException().getMessage());
                }
            }
        }
    }

    /***************************************
	 * Prints the header of the report
	 *
	 * @param result DOCUMENT ME!
	 */
    public void printHeader(TestResult result) {
        if (result.wasSuccessful()) {
            fWriter.println();
            fWriter.print("OK");
            fWriter.println(" (" + result.runCount() + " tests)");
        } else {
            fWriter.println();
            fWriter.println("FAILURES!!!");
            fWriter.println("Test Results:");
            fWriter.println("Run: " + result.runCount() + " Failures: " + result.failureCount() + " Errors: " + result.errorCount());
        }
    }

    /***************************************
	 * Runs a single test and collects its results. This method can be used to
	 * start a test run from your program.
	 * <pre>
	 * public static void main (String[] args) {
	 *     test.textui.TestRunner.run(suite());
	 * }
	 * </pre>
	 *
	 * @param suite DOCUMENT ME!
	 */
    public static void run(Test suite) {
        TestRunner aTestRunner = new TestRunner();
        aTestRunner.doRun(suite);
    }

    /***************************************
	 * DOCUMENT ME!
	 *
	 * @param test DOCUMENT ME!
	 */
    public synchronized void startTest(Test test) {
        System.out.print(".");
    }

    /***************************************
	 * Creates the TestResult to be used for the test run.
	 *
	 * @return DOCUMENT ME!
	 */
    protected TestResult createTestResult() {
        return new TestResult();
    }

    /***************************************
	 * DOCUMENT ME!
	 *
	 * @param suite DOCUMENT ME!
	 */
    protected void doRun(Test suite) {
        TestResult result = createTestResult();
        result.addListener(this);
        long startTime = System.currentTimeMillis();
        suite.run(result);
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        fWriter.println();
        fWriter.println("Time: " + StringUtil.elapsedTimeAsString(runTime));
        print(result);
        fWriter.println();
        if (!result.wasSuccessful()) System.exit(-1);
        System.exit(0);
    }

    /***************************************
	 * Starts a test run. Analyzes the command line arguments and runs the
	 * given test suite.
	 *
	 * @param args DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    protected String processArguments(String[] args) {
        String testCase = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-v")) System.out.println("J2ME Unit " + Version.id() + "by RoleModel Software, Inc. \nOriginal JUnit by Kent Beck and Erich Gamma"); else testCase = args[i];
        }
        if (testCase.equals("")) {
            System.out.println("Usage: TestRunner testCaseName, where name is the name of the TestCase class");
            System.exit(-1);
        }
        return testCase;
    }

    /***************************************
	 * Starts a test run. Analyzes the command line arguments and runs the
	 * given test suite.
	 *
	 * @param args DOCUMENT ME!
	 */
    protected void start(String[] args) {
        String testCaseName = processArguments(args);
        try {
            Class testClass = Class.forName(testCaseName);
            TestCase testCase = (TestCase) testClass.newInstance();
            Test suite = testCase.suite();
            doRun(suite);
        } catch (Exception e) {
            System.out.println("Could not create and run test suite");
            System.exit(-1);
        }
    }
}
