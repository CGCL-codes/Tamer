package org.newsml.toolkit.conformance;

import org.newsml.toolkit.BaseNode;
import org.newsml.toolkit.NewsMLException;

/**
 * Abstract base class for all NewsML conformance tests.
 *
 * <p>This class provides some convenience methods for reporting
 * errors and warnings.  Subclasses must implement the {@link #run}
 * method to run the actual tests.</p>
 *
 * @author Reuters PLC
 * @version 2.0
 * @see NewsMLTestManager
 */
public abstract class TestBase {

    /**
     * Run tests.
     *
     * <p>All NewsML conformance tests must implement this method.  The
     * test will receive the node to test (one of the nodes matching
     * the XPath expression provided to {@link
     * NewsMLTestManager#addTest}) and a flag indicating whether the
     * test is allowed to reference resources outside the current
     * NewsML document (such as URL references).</p>
     *
     * <p>To report an error or warning, the test must throw an
     * exception: it can use a {@link ConformanceWarning} to report a
     * warning, or any other {@link NewsMLException} to report an
     * error; either of these may be wrapping another exception of
     * some kind.  Since the test uses exceptions, only the first
     * error or warning it finds will be reported.  The test may also
     * throw a java.lang.ClassCastException if the node is not of the
     * expected type: this exception will flow right up to the client
     * application, since it should only happen when there is a design
     * flaw in the client. </p>
     *
     * <p>The {@link NewsMLTestManager} will catch the exceptions and
     * pass them on to the client application through the {@link
     * ErrorVisitor} interface provided by the client.</p>
     *
     * @param contextNode The NewsML BaseNode to test.  The test may
     * case it to the appropriate type.
     * @param useExternal true if the test is allowed to reference
     * resources outside the NewsML document.
     * @exception ConformanceWarning if the test reports a warning.
     * @exception NewsMLException if the test reports an error.
     */
    public abstract void run(BaseNode contextNode, boolean useExternal) throws NewsMLException;

    /**
     * Report a warning.
     *
     * <p>The client application can also simply throw a {@link
     * ConformanceWarning} direction.</p>
     *
     * @param message The warning message.
     * @exception ConformanceWarning always thrown.
     */
    public static void warning(String message) throws ConformanceWarning {
        throw new ConformanceWarning(message);
    }

    /**
     * Report an error.
     *
     * <p>The client application can also simply throw a {@link
     * NewsMLException} direction.</p>
     *
     * @param message The error message.
     * @exception NewsMLException always thrown.
     */
    public static void error(String message) throws NewsMLException {
        throw new NewsMLException(message);
    }

    /**
     * Report an internal error (shouldn't happen).
     *
     * <p>The client application can also simply throw a {@link
     * RuntimeException} directly.</p>
     *
     * @param message The internal error message.
     * @exception RuntimeException always thrown.
     */
    public static void internal(String message) throws RuntimeException {
        throw new RuntimeException("INTERNAL ERROR!!! " + message);
    }
}
