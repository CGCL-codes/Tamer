package org.vizzini.math;

import java.util.logging.Logger;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.vizzini.util.SystemProperties;
import org.vizzini.util.TestFinder;

/**
 * Provides unit tests for the <code>VectorFormat</code> class.
 *
 * <p>By default, all test methods (methods names beginning with <code>
 * test</code>) are run. Run individual tests from the command line using the
 * <code>main()</code> method. Specify individual test methods to run using the
 * <code>suite()</code> method.</p>
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.3
 * @see      TestFinder
 * @since    v0.3
 */
public class VectorFormatTest extends TestCase {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(VectorFormatTest.class.getName());

    /**
     * Construct this object with the given parameter.
     *
     * @param  method  Method to run.
     *
     * @since  v0.3
     */
    public VectorFormatTest(String method) {
        super(method);
    }

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.3
     */
    public static void main(String[] args) {
        TestFinder.getInstance().run(VectorFormatTest.class, args);
    }

    /**
     * @return  a suite of tests to run.
     *
     * @since   v0.3
     */
    public static TestSuite suite() {
        TestSuite suite = new TestSuite(VectorFormatTest.class);
        return suite;
    }

    /**
     * Test the <code>format()</code> method.
     *
     * @since  v0.3
     */
    public void testFormat() {
        SystemProperties sysProps = new SystemProperties();
        LOGGER.info("sysProps.getJavaRuntime() = " + sysProps.getJavaRuntime());
        LOGGER.info("sysProps.getJavaVersion() = " + sysProps.getJavaVersion());
        LOGGER.info("sysProps.getJavaVirtualMachine() = " + sysProps.getJavaVirtualMachine());
        VectorFormat formatter = new VectorFormat();
        String expected = "(+0.00E00,+0.00E00,+0.00E00)";
        if (sysProps.getJavaVersion().startsWith("1.4")) {
            expected = "(+0.00E+00,+0.00E+00,+0.00E+00)";
        }
        String result = formatter.format(Vector.ZERO);
        assertEquals(expected, result);
        expected = "(-1.00E00,-1.00E-01,-1.00E-02)";
        if (sysProps.getJavaVersion().startsWith("1.4")) {
            expected = "(-1.00E+00,-1.00E-01,-1.00E-02)";
        }
        result = formatter.format(new Vector(-1.0, -0.1, -0.01));
        assertEquals(expected, result);
    }

    /**
     * Test the <code>format()</code> method.
     *
     * @since  v0.3
     */
    public void testFormat0() {
        SystemProperties sysProps = new SystemProperties();
        VectorFormat formatter = new VectorFormat(0);
        String expected = "(+0.E00,+0.E00,+0.E00)";
        if (sysProps.getJavaVersion().startsWith("1.4")) {
            expected = "(+0E+00,+0E+00,+0E+00)";
        }
        String result = formatter.format(Vector.ZERO);
        assertEquals(expected, result);
        expected = "(-1.E00,-1.E-01,-1.E-02)";
        if (sysProps.getJavaVersion().startsWith("1.4")) {
            expected = "(-1E+00,-1E-01,-1E-02)";
        }
        result = formatter.format(new Vector(-1.0, -0.1, -0.01));
        assertEquals(expected, result);
    }

    /**
     * Test the <code>format()</code> method.
     *
     * @since  v0.3
     */
    public void testFormat4() {
        SystemProperties sysProps = new SystemProperties();
        VectorFormat formatter = new VectorFormat(4);
        String expected = "(+0.0000E00,+0.0000E00,+0.0000E00)";
        if (sysProps.getJavaVersion().startsWith("1.4")) {
            expected = "(+0.0000E+00,+0.0000E+00,+0.0000E+00)";
        }
        String result = formatter.format(Vector.ZERO);
        assertEquals(expected, result);
        expected = "(-1.0000E00,-1.0000E-01,-1.0000E-02)";
        if (sysProps.getJavaVersion().startsWith("1.4")) {
            expected = "(-1.0000E+00,-1.0000E-01,-1.0000E-02)";
        }
        result = formatter.format(new Vector(-1.0, -0.1, -0.01));
        assertEquals(expected, result);
    }
}
