package net.sf.iwant.entry2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.security.Permission;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import net.sf.iwant.entry.Iwant3NetworkMock;
import net.sf.iwant.entry.WsRootFinder;
import net.sf.iwant.testarea.TestArea;

public class Iwant2Test extends TestCase {

    private static final String LINE_SEPARATOR_KEY = "line.separator";

    private TestArea testArea;

    private SecurityManager origSecman;

    private InputStream originalIn;

    private PrintStream originalOut;

    private PrintStream originalErr;

    private ByteArrayOutputStream out;

    private ByteArrayOutputStream err;

    private String originalLineSeparator;

    private Iwant3NetworkMock network;

    private Iwant2 iwant2;

    /**
	 * TODO a reusable main-method testing tools project
	 */
    public void setUp() {
        origSecman = System.getSecurityManager();
        System.setSecurityManager(new ExitCatcher());
        originalIn = System.in;
        originalOut = System.out;
        originalErr = System.err;
        originalLineSeparator = System.getProperty(LINE_SEPARATOR_KEY);
        System.setProperty(LINE_SEPARATOR_KEY, "\n");
        startOfOutAndErrCapture();
        testArea = new IwantEntry2TestArea();
        network = new Iwant3NetworkMock(testArea);
        iwant2 = Iwant2.using(network);
    }

    private void startOfOutAndErrCapture() {
        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    private static class ExitCalledException extends SecurityException {

        private final int status;

        public ExitCalledException(int status) {
            this.status = status;
        }

        @SuppressWarnings("unused")
        public int status() {
            return status;
        }
    }

    private static class ExitCatcher extends SecurityManager {

        @Override
        public void checkPermission(Permission perm) {
        }

        @Override
        public void checkExit(int status) {
            throw new ExitCalledException(status);
        }
    }

    @Override
    public void tearDown() {
        System.setSecurityManager(origSecman);
        System.setIn(originalIn);
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setProperty(LINE_SEPARATOR_KEY, originalLineSeparator);
        System.err.print("== out:\n" + out());
        System.err.print("== err:\n" + err());
    }

    private String out() {
        return out.toString();
    }

    private String err() {
        return err.toString();
    }

    private static String mockedIwantTestRunner() {
        StringBuilder b = new StringBuilder();
        b.append("package net.sf.iwant.testrunner;\n");
        b.append("\n");
        b.append("import java.util.Arrays;\n");
        b.append("\n");
        b.append("public class IwantTestRunner {\n");
        b.append("\n");
        b.append("	public static void main(String[] args) {\n");
        b.append("		System.out.println(\"Mocked IwantTestRunner\");\n");
        b.append("		System.out.println(\"args: \" + Arrays.toString(args));\n");
        b.append("	}\n");
        b.append("\n");
        b.append("}\n");
        return b.toString();
    }

    private static String mockedFailingIwantTestRunner() {
        StringBuilder b = new StringBuilder();
        b.append("package net.sf.iwant.testrunner;\n");
        b.append("\n");
        b.append("public class IwantTestRunner {\n");
        b.append("\n");
        b.append("  public static void main(String[] args) {\n");
        b.append("    junit.framework.Assert.fail(\"Simulated failure.\");");
        b.append("  }\n");
        b.append("\n");
        b.append("}\n");
        return b.toString();
    }

    private static File mockedIwantRunnerJava() {
        return new File(WsRootFinder.mockWsRoot(), "iwant-testrunner/src/main/java/net/sf/iwant/testrunner/IwantTestRunner.java");
    }

    private static void mockedIwantRunnerShallSucceed() {
        try {
            new FileWriter(mockedIwantRunnerJava()).append(mockedIwantTestRunner()).close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void mockedIwantRunnerShallFail() {
        try {
            new FileWriter(mockedIwantRunnerJava()).append(mockedFailingIwantTestRunner()).close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void testIwant2CompilesIwantAndRunsTestSuiteAndFailsWhenItFails() {
        mockedIwantRunnerShallFail();
        try {
            iwant2.evaluate(WsRootFinder.mockWsRoot(), "args", "to be", "passed");
            fail();
        } catch (RuntimeException e) {
            InvocationTargetException ite = (InvocationTargetException) e.getCause();
            AssertionFailedError afe = (AssertionFailedError) ite.getCause();
            assertEquals("Simulated failure.", afe.getMessage());
        }
        assertEquals("", out());
    }

    public void testIwant2CompilesIwantAndCallsIwant3AfterTestSuiteSuccess() {
        mockedIwantRunnerShallSucceed();
        iwant2.evaluate(WsRootFinder.mockWsRoot(), "args", "to be", "passed");
        assertEquals("Mocked net.sf.iwant.entry3.Iwant3\n" + "args: [args, to be, passed]\n", out());
        assertTrue(err().contains("Mocked IwantTestRunner\n" + "args: [net.sf.iwant.entry3.IwantEntry3Suite]\n"));
    }
}
