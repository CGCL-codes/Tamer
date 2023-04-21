package jreceiver.j2me.common.rec;

import jreceiver.j2me.common.JRecException;

/**
 * The base exception class for the JReceiver project.
 * <p>
 * This is the beginnings of the application of a Java
 * Idiom found at http://c2.com/cgi/wiki?HomogenizeExceptions
 * <p>
 * See also at http://c2.com/cgi/wiki?NestedExceptions
 * <p>
 * Create a single type of exception for each package
 * and only propagate exceptions of that type or some
 * sub-type out of methods in the package.
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/12/29 00:44:09 $
 */
public class RecException extends JRecException {

    /**
     * <p>
     * This will create an <code>Exception</code> with the given message.
     * </p>
     * <p>
     * @param message <code>String</code> message indicating
     *                the problem that occurred.
     */
    public RecException(String message) {
        super(message);
    }

    /**
     * <p>
     * This will create an <code>Exception</code> with the given message
     *   and wrap another <code>Exception</code>.  This is useful when
     *   the originating <code>Exception</code> should be held on to.
     * </p>
     * <p>
     * @param message <code>String</code> message indicating
     *                the problem that occurred.
     * @param exception <code>Exception</code> that caused this
     *                  to be thrown.
     */
    public RecException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
