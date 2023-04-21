package whf.framework.workflow.exception;

import whf.framework.exception.AppException;

/**
 * @author king
 *
 */
@SuppressWarnings("serial")
public class WorkflowException extends AppException {

    public WorkflowException() {
        super();
    }

    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkflowException(String message) {
        super(message);
    }

    public WorkflowException(Throwable cause) {
        super(cause);
    }
}
