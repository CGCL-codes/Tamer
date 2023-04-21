package seqSamoa.exceptions;

/**
 * A <CODE>NotInAComputationException</CODE> is raised when a service call
 * or a service response is issued outside the protocol stack (i.e. outside a computation).
 * Note that in order to issue a call or a response outside the protocol stack, one has to
 * use the methods {@link seqSamoa.Service.externalCall externalCall}
 * and {@link seqSamoa.Service.externalResponse externalResponse}
 * implemented by the {@link seqSamoa.Service Service} class.
 */
@SuppressWarnings("serial")
public class NotInAComputationException extends RuntimeException {

    /**
     * Constructs an <i>NotScheduledComputationException </i> with the details
     * message given.
     * 
     * @param m
     *            the details message
     */
    public NotInAComputationException(String m) {
        super("NotInAComputationException: " + m);
    }
}

;
