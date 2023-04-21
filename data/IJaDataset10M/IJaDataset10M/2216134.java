package org.opennms.core.queue;

/**
 * This exception is used to represent an error condition where an attempt is
 * made to add an element to a closed {@link ClosableFifoQueue queue}.
 * 
 * @author <a href="mailto:weave@oculan.com">Brian Weaver </a>
 * @author <a href="http://www.opennms.org/">OpenNMS </a>
 * 
 */
public class FifoQueueClosedException extends FifoQueueException {

    /**
     * 
     */
    private static final long serialVersionUID = -9088896767584630679L;

    /**
     * Constructs a default instance of the excpetion.
     */
    public FifoQueueClosedException() {
        super();
    }

    /**
     * Constructs a new exception with the passed explination.
     * 
     * @param why
     *            The explination message.
     */
    public FifoQueueClosedException(String why) {
        super(why);
    }
}
