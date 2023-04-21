package org.apache.java.lang;

/**
 * This Exception is thrown when a specified timeout is reached without
 * suceeding in completing the action.
 *
 * @author <a href="mailto:akosut@apache.org>Alexei Kosut</a>
 * @version $Revision: 1.2 $ $Date: 2005/01/12 00:29:00 $
 */
public class TimeoutException extends Exception {

    /**
     * Default constructor.
     */
    public TimeoutException() {
        super();
    }

    /**
     * Constructor with a message associated.
     */
    public TimeoutException(String message) {
        super(message);
    }
}
