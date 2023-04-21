package architecture.common.exception;

import org.apache.commons.lang.exception.NestableError;

public class RuntimeError extends NestableError {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2766280859418487829L;

    public RuntimeError() {
        super();
    }

    public RuntimeError(String msg) {
        super(msg);
    }

    public RuntimeError(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RuntimeError(Throwable cause) {
        super(cause);
    }
}
