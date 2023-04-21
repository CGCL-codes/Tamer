package org.jplate.foundation.exception;

import org.jplate.foundation.FoundationException;

/**

    Thrown when removing something fails.

    <p/>

    <pre>
Modifications:
    $Author: sfloess $
    $Date: 2008-12-02 12:32:45 -0500 (Tue, 02 Dec 2008) $
    $Revision: 479 $
    $HeadURL: http://jplate.svn.sourceforge.net/svnroot/jplate/trunk/src/dev/java/org/jplate/foundation/exception/RemoveException.java $
    </pre>

*/
public class RemoveException extends FoundationException {

    /**
     *
     * Default constructor.
     *
     */
    public RemoveException() {
    }

    /**
     *
     * This constructor sets the message.
     *
     * @param msg A message about why self was raised.
     *
     */
    public RemoveException(final String msg) {
        super(msg);
    }

    /**
     *
     * This constructor sets the root cause.
     *
     * @param rootCause The exception that caused self to be raised.
     *
     */
    public RemoveException(final Throwable rootCause) {
        super(rootCause);
    }

    /**
     *
     * This constructor sets the message and root cause.
     *
     * @param msg       A message about why self was raised.
     *
     * @param rootCause The exception that caused self to be raised.
     *
     */
    public RemoveException(final String msg, final Throwable rootCause) {
        super(msg, rootCause);
    }
}
