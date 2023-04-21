package seqSamoa.exceptions;

/**
 * A <CODE>InterruptedSchedulerException</CODE> is raised when the scheduler
 *  is interrupted, i.e. when one thread managed by the scheduler is interrupted.
 */
@SuppressWarnings("serial")
public class InterruptedSchedulerException extends RuntimeException {

    /**
     * Constructs an <i>InterruptedComputationException </i> without a details
     * message.
     */
    public InterruptedSchedulerException(long cid) {
        super("InterruptedComputationException " + cid + ":");
    }

    /**
     * Constructs an <i>InterruptedComputationException </i> with the details
     * message given. <br>
     * 
     * @param s
     *            the details message
     */
    public InterruptedSchedulerException(long cid, String s) {
        super("InterruptedComputationException " + cid + ":" + s);
    }
}

;
