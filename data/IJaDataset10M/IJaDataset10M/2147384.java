package org.robinfinch.clasj.universals;

/**
 * This exception indicates things have happened in
 * the wrong order.
 * @author Mark Hoogenboom
 */
public class TimeLineException extends Exception {

    public TimeLineException(String message) {
        super(message);
    }

    public TimeLineException(String message, Throwable cause) {
        super(message, cause);
    }
}
