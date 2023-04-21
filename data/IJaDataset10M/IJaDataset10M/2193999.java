package net.grinder.testutility;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestSuite;

/**
 * Various test utilities for setting the context for different Jython
 * installations.
 *
 * @author Philip Aston
 * @version $Revision:$
 */
public class JythonVersionUtilities {

    private static final List<String> GRINDER_AND_PYTHON_CLASSES = Arrays.asList("net.grinder.*", "org.python.*", "test.*", "+net.grinder.util.weave.agent.*");

    public static TestSuite jython21Suite(Class<?> suite) throws Exception {
        final TestSuite result = new TestSuite(suite.getName() + " [Jython 2.1]");
        result.addTest(jythonSuite(suite, "jython2_1.dir"));
        return result;
    }

    public static TestSuite jython25Suite(Class<?> suite) throws Exception {
        final TestSuite result = new TestSuite(suite.getName() + " [Jython 2.5]");
        result.addTest(jythonSuite(suite, "jython2_5_2.dir"));
        result.addTest(jythonSuite(suite, "jython2_5_0.dir"));
        result.addTest(jythonSuite(suite, "jython2_5_1.dir"));
        return result;
    }

    private static TestSuite jythonSuite(Class<?> suite, String pythonHomeProperty) throws Exception {
        final String oldPythonHome = System.getProperty("python.home");
        final String pythonHome = System.getProperty(pythonHomeProperty);
        if (pythonHome == null) {
            System.err.println("***** " + pythonHomeProperty + " not set, skipping tests for Jython version.");
            return new TestSuite();
        }
        System.setProperty("python.home", pythonHome);
        try {
            final URL jythonJarURL = new URL("file://" + pythonHome + "/jython.jar");
            final ClassLoader classLoader = BlockingClassLoader.createClassLoader(GRINDER_AND_PYTHON_CLASSES, Arrays.asList(jythonJarURL));
            return new TestSuite(classLoader.loadClass(suite.getName()), pythonHome);
        } finally {
            if (oldPythonHome != null) {
                System.setProperty("python.home", oldPythonHome);
            } else {
                System.clearProperty("python.home");
            }
        }
    }
}
