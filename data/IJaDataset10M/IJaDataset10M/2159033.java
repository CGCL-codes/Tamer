package jaxlib.junit.ui;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.runner.BaseTestRunner;
import junit.runner.StandardTestSuiteLoader;
import junit.runner.TestSuiteLoader;
import junit.runner.Version;
import jaxlib.junit.ReportingTest;
import jaxlib.junit.TestReport;
import jaxlib.junit.TestReportNode;
import jaxlib.junit.XTestSuite;

/**
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: PrintingTestRunner.java 892 2003-08-06 18:08:32Z joerg_wassmer $.00
 */
public class PrintingTestRunner extends BaseTestRunner {

    public static void main(String args[]) {
        PrintingTestRunner runner = new PrintingTestRunner();
        try {
            TestResult r = runner.start(args);
            if (!r.wasSuccessful()) System.exit(-1); else System.exit(0);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-2);
        }
    }

    /**
	 * Runs a suite extracted from a TestCase subclass.
	 */
    public static void run(Class testClass) {
        run(new XTestSuite(testClass));
    }

    /**
	 * Runs a single test and collects its results.
	 * This method can be used to start a test run
	 * from your program.
	 * <pre>
	 * public static void main (String[] args) {
	 *     test.textui.TestRunner.run(suite());
	 * }
	 * </pre>
	 */
    public static void run(Test suite) {
        PrintingTestRunner runner = new PrintingTestRunner();
        runner.doRun(suite, false);
    }

    /**
	 * Runs a single test and waits until the user
	 * types RETURN.
	 */
    public static void runAndWait(Test suite) {
        PrintingTestRunner runner = new PrintingTestRunner();
        runner.doRun(suite, true);
    }

    private final PrintStream out;

    /**
	 * Constructs a PrintingTestRunner.
	 */
    public PrintingTestRunner() {
        this(System.out);
    }

    /**
	 * Constructs a PrintingTestRunner using the given stream for all the output.
	 */
    public PrintingTestRunner(PrintStream out) {
        if (out == null) throw new NullPointerException("Null out.");
        this.out = out;
    }

    /**
	 * Always use the StandardTestSuiteLoader. Overridden from
	 * BaseTestRunner.
	 */
    public TestSuiteLoader getLoader() {
        return new StandardTestSuiteLoader();
    }

    public synchronized void addError(Test test, Throwable t) {
        this.out.print("ERROR");
    }

    public synchronized void addFailure(Test test, AssertionFailedError t) {
        this.out.print("FAILED");
    }

    /**
	 * Creates the TestResult to be used for the test run.
	 */
    protected TestResult createTestResult() {
        return new TestResult();
    }

    public TestResult doRun(Test suite, boolean wait) {
        TestResult result = createTestResult();
        result.addListener(this);
        long startTime = System.currentTimeMillis();
        suite.run(result);
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        this.out.println();
        this.out.println("Time: " + elapsedTimeAsString(runTime));
        print(result);
        this.out.println();
        pause(wait);
        return result;
    }

    protected void pause(boolean wait) {
        if (wait) {
            this.out.println("<RETURN> to continue");
            try {
                System.in.read();
            } catch (Exception e) {
            }
        }
    }

    public synchronized void startTest(Test test) {
        String s = test.toString();
        this.out.print(s);
        for (int i = 69 - s.length(); i > 0; i--) this.out.print('.');
        this.out.println("...");
    }

    public synchronized void endTest(Test test) {
        if (test instanceof ReportingTest) {
            ReportingTest xt = (ReportingTest) test;
            TestReport report = xt.getReport();
            if (report != null) {
                Set<TestReportNode> reports = report.getNodes();
                if (!reports.isEmpty()) {
                    for (Iterator<TestReportNode> it = reports.iterator(); it.hasNext(); ) {
                        TestReportNode e = it.next();
                        this.out.print(e.getName());
                        Object value = e.getValue();
                        if (value == null) this.out.println(); else {
                            this.out.print(": ");
                            this.out.print(e.getValue());
                            String label = e.getLabel();
                            if (label == null) this.out.println(); else {
                                this.out.print(" ");
                                this.out.println(label);
                            }
                        }
                        String details = e.getDetailedReport();
                        if (details != null) {
                            StringTokenizer st = new StringTokenizer(details, "\n");
                            while (st.hasMoreTokens()) {
                                this.out.print("   ");
                                this.out.println(st.nextToken());
                            }
                        }
                    }
                }
                this.out.println();
            }
        } else this.out.println();
    }

    /**
	 * Prints failures to the standard output
	 */
    private synchronized void print(TestResult result) {
        printErrors(result);
        printFailures(result);
        printHeader(result);
    }

    /**
	 * Prints the errors to the standard output
	 */
    private synchronized void printErrors(TestResult result) {
        if (result.errorCount() != 0) {
            if (result.errorCount() == 1) this.out.println("There was " + result.errorCount() + " error:"); else this.out.println("There were " + result.errorCount() + " errors:");
            int i = 1;
            for (Enumeration e = result.errors(); e.hasMoreElements(); i++) {
                TestFailure failure = (TestFailure) e.nextElement();
                this.out.println(i + ") " + failure.failedTest());
                this.out.print(getFilteredTrace(failure.thrownException()));
            }
        }
    }

    /**
	 * Prints failures to the standard output
	 */
    private synchronized void printFailures(TestResult result) {
        if (result.failureCount() != 0) {
            if (result.failureCount() == 1) this.out.println("There was " + result.failureCount() + " failure:"); else this.out.println("There were " + result.failureCount() + " failures:");
            int i = 1;
            for (Enumeration e = result.failures(); e.hasMoreElements(); i++) {
                TestFailure failure = (TestFailure) e.nextElement();
                this.out.print(i + ") " + failure.failedTest());
                Throwable t = failure.thrownException();
                this.out.print(getFilteredTrace(failure.thrownException()));
            }
        }
    }

    /**
	 * Prints the header of the report
	 */
    private synchronized void printHeader(TestResult result) {
        if (result.wasSuccessful()) {
            this.out.println();
            this.out.print("OK");
            this.out.println(" (" + result.runCount() + " tests)");
        } else {
            this.out.println();
            this.out.println("FAILURES!!!");
            this.out.println("Tests run: " + result.runCount() + ",  Failures: " + result.failureCount() + ",  Errors: " + result.errorCount());
        }
    }

    /**
	 * Starts a test run. Analyzes the command line arguments
	 * and runs the given test suite.
	 */
    protected TestResult start(String args[]) throws Exception {
        String testCase = "";
        boolean wait = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-wait")) wait = true; else if (args[i].equals("-c")) testCase = extractClassName(args[++i]); else if (args[i].equals("-v")) System.err.println("JUnit " + Version.id() + " by Kent Beck and Erich Gamma"); else testCase = args[i];
        }
        if (testCase.equals("")) throw new Exception("Usage: PrintingTestRunner [-wait] testCaseName, where name is the name of the TestCase class");
        try {
            Test suite = getTest(testCase);
            return doRun(suite, wait);
        } catch (Exception e) {
            throw new Exception("Could not create and run test suite: " + e);
        }
    }

    protected void runFailed(String message) {
        System.err.println(message);
        System.exit(-1);
    }

    protected PrintStream out() {
        return this.out;
    }
}
