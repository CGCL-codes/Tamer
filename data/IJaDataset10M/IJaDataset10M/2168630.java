package edu.purdue.rcac.service.dispatch;

/**
 * Exception class for submitter runtime failure. If {@link Submitter} throws a
 * {@link SubmissionException}, this exception should be throw to upper level.
 * 
 * @author Han Zhang
 * 
 */
@SuppressWarnings("serial")
public class SubmitterRuntimeException extends Exception {

    public SubmitterRuntimeException() {
        super();
    }

    public SubmitterRuntimeException(String message) {
        super(message);
    }

    public SubmitterRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubmitterRuntimeException(Throwable cause) {
        super(cause);
    }
}
