package tigerunit.runner;

import tigerunit.framework.CompositeTest;
import tigerunit.framework.Test;
import tigerunit.framework.TestListener;
import tigerunit.framework.TestListenerAdapter;
import tigerunit.framework.TestResult;
import tigerunit.framework.TestSession;
import tigerunit.util.Config;
import tigerunit.util.FileResolver;
import tigerunit.util.Runner;
import tigerunit.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;

/**
 * Defines the lifecycle of a test runner. Takes command-line arguments,
 * which are parsed using the classes in {@link tigerunit.util.cli}. Subclasses
 * can contribute to the set of options with the addOption() static method (this
 * is best done in a static initializer), and process options by overriding the
 * init(CommandLine) method.
 * <p/>
 * Subclasses should override the setup method to perform any initialization
 * that needs to occur on execution but before tests are loaded. An example of
 * this would be creating and displaying the UI components of a GUI.
 */
public class BaseTestRunner extends TestListenerAdapter {

    public static enum ExitStatus {

        SUCCESS, FAILURE, EXCEPTION
    }

    protected static class TestExecutor extends Runner {

        public TestExecutor(final Test test, final TestResult result) {
            super(new Runnable() {

                public void run() {
                    test.run(result);
                }
            });
        }
    }

    public static final String DEFAULT_TEST_LOADER = "tigerunit.runner.HierarchyTestLoader";

    public static final String PROP_TEST_LOADER = "tigerunit.defaultTestLoader";

    public static final String PROP_MAX_MESSAGE_LENGTH = "maxmessage";

    public static final String PROP_FORK = "fork";

    public static final int DEFAULT_MAX_MESSAGE_LENGTH = 500;

    protected static void exit(ExitStatus status) {
        System.exit(status.ordinal());
    }

    protected static TestLoader getDefaultTestLoader() {
        String className = System.getProperty(PROP_TEST_LOADER);
        if (className == null) {
            className = DEFAULT_TEST_LOADER;
        }
        return getTestLoader(className);
    }

    protected static TestLoader getTestLoader(String name) {
        String className = Config.get(name, name);
        try {
            return (TestLoader) Class.forName(className).newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Cannot instantiate " + className);
        }
    }

    /**
     * Truncates a String to the maximum length.
     */
    protected static String truncate(String s) {
        int maxMessageLength = Config.get(PROP_MAX_MESSAGE_LENGTH, DEFAULT_MAX_MESSAGE_LENGTH);
        if (maxMessageLength != -1 && s.length() > maxMessageLength) {
            s = s.substring(0, maxMessageLength) + "...";
        }
        return s;
    }

    private TestLoader testLoader;

    private Map<Class<? extends ResultFormatter>, ResultFormatter> formatters;

    private transient List<ResultFormatter> mergedFormatters;

    private List<TestListener> listeners;

    private ByteArrayOutputStream redirectSysOutDest;

    private PrintStream redirectSysOut;

    private ByteArrayOutputStream redirectSysErrDest;

    private PrintStream redirectSysErr;

    private PrintStream sysOut;

    private PrintStream sysErr;

    protected BaseTestRunner() {
    }

    public TestLoader getTestLoader() {
        if (testLoader == null) {
            testLoader = getDefaultTestLoader();
        }
        return testLoader;
    }

    public void setTestLoader(TestLoader testLoader) {
        this.testLoader = testLoader;
    }

    public void setTestLoader(String className) {
        TestLoader testLoader = null;
        if (className != null) {
            testLoader = getTestLoader(className);
        }
        if (testLoader == null) {
            testLoader = getDefaultTestLoader();
        }
        setTestLoader(testLoader);
    }

    public ResultFormatter addFormatter(String name) {
        ResultFormatter formatter = createFormatter(name);
        formatter.setUseSystemOutput();
        addFormatter(formatter);
        return formatter;
    }

    public ResultFormatter addFormatter(String name, File outputFile) throws IOException {
        ResultFormatter formatter = createFormatter(name);
        formatter.setOutput(new FileOutputStream(outputFile), true);
        addFormatter(formatter);
        return formatter;
    }

    public ResultFormatter addFormatter(String name, File outputDir, String prefix, String extension) {
        ResultFormatter formatter = createFormatter(name);
        formatter.setOutput(outputDir, prefix, extension);
        addFormatter(formatter);
        return formatter;
    }

    public ResultFormatter addFormatter(String name, OutputStream output, boolean buffered) {
        ResultFormatter formatter = createFormatter(name);
        formatter.setOutput(output, buffered);
        addFormatter(formatter);
        return formatter;
    }

    public void addFormatter(ResultFormatter formatter) {
        if (this.formatters == null) {
            this.formatters = new HashMap<Class<? extends ResultFormatter>, ResultFormatter>();
        }
        this.formatters.put(formatter.getClass(), formatter);
    }

    private ResultFormatter createFormatter(String name) {
        try {
            String formatterClass = Config.get(name, name);
            Class c = Class.forName(formatterClass);
            return (ResultFormatter) c.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Override this method to provide custom default formatters.
     * @return
     */
    protected List<ResultFormatter> getDefaultFormatters() {
        return null;
    }

    protected List<ResultFormatter> getFormatters() {
        if (this.mergedFormatters == null) {
            List<ResultFormatter> formatters = new ArrayList<ResultFormatter>();
            List<ResultFormatter> defaultFormatters = getDefaultFormatters();
            if (this.formatters != null) {
                formatters.addAll(this.formatters.values());
            }
            if (defaultFormatters != null) {
                formatters.addAll(defaultFormatters);
            }
            this.mergedFormatters = formatters;
        }
        return this.mergedFormatters;
    }

    public void addListener(TestListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<TestListener>();
        }
        listeners.add(listener);
    }

    private List<TestListener> combineListeners() {
        List<ResultFormatter> formatters = getFormatters();
        List<TestListener> listeners = this.listeners;
        List<TestListener> l = new ArrayList<TestListener>(((formatters == null) ? 0 : formatters.size()) + ((listeners == null) ? 0 : listeners.size()));
        if (formatters != null) {
            l.addAll(formatters);
        }
        if (listeners != null) {
            l.addAll(listeners);
        }
        return l;
    }

    public void setIncludedCategories(List<String> includedCategories) {
        for (String cat : includedCategories) {
            getTestLoader().addIncludedCategory(cat);
        }
    }

    public void setExcludedCategories(List<String> excludedCategories) {
        for (String cat : excludedCategories) {
            getTestLoader().addExcludedCategory(cat);
        }
    }

    public void setDirectories(List<File> dirs) {
        for (File dir : dirs) {
            getTestLoader().addDirectory(dir);
        }
    }

    public void setJars(List<JarFile> jars) {
        for (JarFile jar : jars) {
            getTestLoader().addJar(jar);
        }
    }

    public void setClassNames(List<String> classes) {
        for (String className : classes) {
            getTestLoader().addClassName(className);
        }
    }

    public void initSession(boolean fork, boolean reload, int timeout) {
        TestSession session = TestSession.getInstance();
        session.setFork(fork);
        session.setReload(reload);
        session.setTimeout(timeout);
        session.begin();
    }

    public void initSession(boolean fork, boolean reloading, int timeout, String jvm, List<String> jvmArgs, List<String> classpath, Properties systemProperties, Properties environmentProperties, boolean newEnvironment, boolean onlySysClasspath, FileResolver resolver) {
        TestSession session = TestSession.getInstance();
        session.setFork(fork);
        session.setReload(reloading);
        session.setTimeout(timeout);
        session.setJvm(jvm);
        session.setJvmArgs(jvmArgs);
        session.setClasspath(classpath);
        session.setSystemProperties(systemProperties);
        session.setEnvironmentProperties(environmentProperties);
        session.setNewEnvironment(newEnvironment);
        session.setOnlySysClasspath(onlySysClasspath);
        session.setFileResolver(resolver);
        session.begin();
    }

    /**
     * Starts a test run.
     */
    public TestResult run() {
        CompositeTest suite = null;
        try {
            setup();
            suite = getTestLoader().loadTests();
            return run(suite);
        } catch (Exception e) {
            runFailed(String.format("Could not run test suite %s", (suite != null) ? suite.getName() : "<unknown>"));
            return null;
        } finally {
            TestSession.getInstance().end();
        }
    }

    /**
     * Override this to do any setup before tests are loaded and run.
     */
    protected void setup() throws Exception {
    }

    protected TestResult run(CompositeTest suite) throws Exception {
        List<TestListener> listeners = combineListeners();
        startSystemOutputRedirect();
        TestResult result = createTestResult();
        result.addListener(this);
        for (TestListener listener : listeners) {
            result.addListener(listener);
        }
        result.startTestSession(suite);
        try {
            run(suite, result);
        } finally {
            result.endTestSession(suite);
            endSystemOutputRedirect();
            afterRun(result);
        }
        return result;
    }

    private void startSystemOutputRedirect() {
        this.sysOut = System.out;
        this.sysErr = System.err;
        this.redirectSysOutDest = new ByteArrayOutputStream();
        this.redirectSysOut = createPrintStream(this.redirectSysOutDest, this.sysOut);
        this.redirectSysErrDest = new ByteArrayOutputStream();
        this.redirectSysErr = createPrintStream(this.redirectSysErrDest, this.sysErr);
        Log.startSystemOutputRedirect(this.redirectSysOut, this.redirectSysErr);
        System.setOut(this.redirectSysOut);
        System.setErr(this.redirectSysErr);
    }

    protected PrintStream createPrintStream(ByteArrayOutputStream dest, PrintStream sys) {
        return new PrintStream(dest);
    }

    private void endSystemOutputRedirect() {
        System.setOut(this.sysOut);
        System.setErr(this.sysErr);
        Log.endSystemOutputRedirect(this.redirectSysOut, this.redirectSysErr);
        this.redirectSysOut.close();
        this.redirectSysErr.close();
    }

    public void endTestSession(CompositeTest sessionSuite, TestResult result) {
        broadcastSystemStreamContents();
    }

    public void endTestSuite(CompositeTest suite, TestResult result) {
        broadcastSystemStreamContents();
    }

    /**
     * Update the formatters with any bytes that were written to the system
     * output/error streams.
     */
    private void broadcastSystemStreamContents() {
        this.redirectSysOut.flush();
        this.redirectSysErr.flush();
        String out = new String(this.redirectSysOutDest.toByteArray());
        String err = new String(this.redirectSysErrDest.toByteArray());
        ResultFormatter.setSystemOutput(out, err);
        this.redirectSysOutDest.reset();
        this.redirectSysErrDest.reset();
    }

    /**
     * Creates the TestResult to be used for the test run.
     */
    protected TestResult createTestResult() {
        return new TestResult();
    }

    protected void run(CompositeTest suite, TestResult result) throws Exception {
        Runner runner = createTestExecutor(suite, result);
        run(runner, result);
    }

    /**
     * Override to define a custom Runner that will run the test suite. This
     * should return a subclass of {@link BaseTestRunner.TestExecutor} if at
     * all possible. If not, the runner must make sure to call
     * result.setRunTime(long) after running the suite.
     *
     * @param suite
     * @param result
     * @return
     */
    protected Runner createTestExecutor(CompositeTest suite, TestResult result) {
        return new TestExecutor(suite, result);
    }

    /**
     * Subclasses should generally override this method to provide custom run
     * behavior, unless they have a good reason not to. If a subclass decides
     * not to use run tests using a subclass of
     * {@link BaseTestRunner.TestExecutor}, it needs to call
     * result.setRunTime(long) with the run time for the test suite.
     *
     * @param testRunner
     * @param result
     * @throws Exception
     */
    protected void run(Runner testRunner, TestResult result) throws Exception {
        testRunner.runTimed();
    }

    /**
     * Override to define how to handle a failed loading of
     * a test suite.
     */
    protected void runFailed(String message) {
        Log.error(message);
    }

    /**
     * Override to do any post-processing on the TestResult. This is always
     * called, regardless of whether the run method throws an exception. It is
     * run after {@TestResult#endTestSuite()}.
     *
     * @param result
     */
    protected void afterRun(TestResult result) {
    }
}
