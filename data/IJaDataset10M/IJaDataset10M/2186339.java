package com.thesett.junit.extensions;

import java.io.PrintStream;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.textui.ResultPrinter;

/**
 * A ResultPrinter that prints nothing. This exists, in order to provide a replacement to JUnit's ResultPrinter, which
 * is refered to directly by JUnit code, rather that as an abstracted TestListener. JUnit's text ui TestRunner must have
 * a ResultPrinter. This provides an implementation of it that prints nothing, so that a better mechanism can be used
 * for providing feedback to the console instead.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td>
 * </table></pre>
 *
 * @author Rupert Smith
 * @todo   See todo in TKTestRunner about completely replacing the test ui runner. Doing things like this in order to
 *         extend JUnit is not nice, and there needs to be a better way to do it. Delete this class and use a listener
 *         instead.
 */
public class NullResultPrinter extends ResultPrinter {

    /**
     * Builds a fake ResultPrinter that prints nothing.
     *
     * @param writer The writer to send output to.
     */
    public NullResultPrinter(PrintStream writer) {
        super(writer);
    }

    /**
     * Does nothing.
     *
     * @param test Ignored.
     * @param t    Ignored.
     */
    public void addError(Test test, Throwable t) {
    }

    /**
     * Does nothing.
     *
     * @param test Ignored.
     * @param t    Ignored.
     */
    public void addFailure(Test test, AssertionFailedError t) {
    }

    /**
     * Does nothing.
     *
     * @param test Ignored.
     */
    public void endTest(Test test) {
    }

    /**
     * Does nothing.
     *
     * @param test Ignored.
     */
    public void startTest(Test test) {
    }
}
