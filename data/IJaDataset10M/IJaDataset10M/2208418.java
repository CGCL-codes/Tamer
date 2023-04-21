package org.ourgrid.common.executor;

import org.ourgrid.common.exception.OurgridException;

/**
 * This exception must be used to signalize an execution problem, found by the
 * implementors of org.ourgrid.common.executor.Executor interface
 */
public class ExecutorException extends OurgridException {

    private static final long serialVersionUID = 33L;

    /**
	 * Constructor without paramethers
	 */
    public ExecutorException() {
        super();
    }

    /**
	 * @param command The command in which execution the Executor failed. It
	 *        will be used to compose better this getMessage method.
	 */
    public ExecutorException(String command) {
        super("Command: " + command);
    }

    /**
	 * @param command The command in which execution the Executor failed. It
	 *        will be used to compose better this getMessage method.
	 * @param detail an object that specifies the exception details
	 */
    public ExecutorException(String command, Throwable detail) {
        super("Command: " + command, detail);
    }

    /**
	 * @param detail an object that specifies the exception details
	 */
    public ExecutorException(Throwable detail) {
        super(detail);
    }

    /**
	 * Returns the message of this exception
	 * 
	 * @return The message
	 */
    public String getMessage() {
        return super.getMessage();
    }
}
