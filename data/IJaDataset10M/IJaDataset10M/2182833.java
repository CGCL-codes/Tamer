package org.ggf.drmaa;

/**
 * The job specified by the given id does not exist.
 * @author dan.templeton@sun.com
 * @since 0.4.2
 * @version 1.0
 */
public class InvalidJobException extends DrmaaException {

    /**
     * Creates a new instance of <code>InvalidJobException</code> without detail
     * message.
     */
    public InvalidJobException() {
    }

    /**
     * Constructs an instance of <code>InvalidJobException</code> with the
     * specified detail message.
     * @param msg the detail message.
     */
    public InvalidJobException(String msg) {
        super(msg);
    }
}
