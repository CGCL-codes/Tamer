package abbot.script;

import junit.extensions.abbot.*;
import javax.swing.*;

public class LaunchTest extends ComponentTestFixture {

    private static volatile boolean terminate;

    private Launch launch;

    private ClassLoader classLoader;

    protected void setUp() {
        Resolver r = getResolver();
        launch = new Launch(r, null, DummyLaunch.class.getName(), "main", new String[] { "[]" });
        launch.setClasspath("classes");
        terminate = false;
        classLoader = Thread.currentThread().getContextClassLoader();
    }

    protected void tearDown() {
        terminate = true;
        launch.terminate();
    }

    public void testLaunchState() throws Throwable {
        launch.launch(new StepRunner());
        assertTrue("Launch should be launched", launch.isLaunched());
    }

    public void testRunState() throws Throwable {
        launch.runStep();
        assertTrue("Launch should be launched", launch.isLaunched());
    }

    public void testGetContextBeforeRunning() {
        ClassLoader cl = launch.getContextClassLoader();
        assertTrue("Launch did not provide a class loader", cl != classLoader);
    }

    public void testRunWithNewContext() throws Throwable {
        ClassLoader cl = launch.getContextClassLoader();
        launch.runStep();
        assertTrue("Launch did not install a class loader: " + cl, cl != classLoader);
        launch.terminate();
        assertEquals("Launch did not uninstall the class loader", classLoader, Thread.currentThread().getContextClassLoader());
        launch.runStep();
        ClassLoader cl2 = launch.getContextClassLoader();
        assertTrue("Each launch should use a fresh class loader: " + cl, cl != cl2);
    }

    public void testContextThreaded() throws Throwable {
        launch.setThreaded(true);
        launch.setMethodName("mainThreaded");
        launch.runStep();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        assertTrue("Launch did not install a class loader", cl != classLoader);
        launch.terminate();
        assertEquals("Launch did not uninstall the class loader", classLoader, Thread.currentThread().getContextClassLoader());
    }

    private boolean error;

    public void testContextLoaderOnlyInstalledAtRun() throws Throwable {
        final ClassLoader cl = launch.getContextClassLoader();
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                error = Thread.currentThread().getContextClassLoader() == cl;
            }
        });
        assertFalse("Class loader should not yet be installed as EDT context", error);
        launch.runStep();
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                error = Thread.currentThread().getContextClassLoader() != cl;
            }
        });
        assertFalse("Class loader should be installed as EDT context when run", error);
    }

    public static void main(String[] args) {
        TestHelper.runTests(args, LaunchTest.class);
    }

    public static class DummyLaunch {

        public static void main(String[] args) {
        }

        public static void mainThreaded(String[] args) {
            while (!terminate) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
