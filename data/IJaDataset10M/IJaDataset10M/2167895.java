package de.iritgo.aktera.query;

import de.iritgo.aktera.core.exception.NestedException;

/**
 *
 * @author Michael Nash
 */
public class QueryException extends NestedException {

    /**
	 * Default constructor
	 */
    public QueryException() {
        super();
    }

    /**
	 * Constructor with a single message, no key
	 *
	 * @param   s exception message
	 */
    public QueryException(String s) {
        super(s);
    }

    /**
	 * String message and error key
	 */
    public QueryException(String s, String newErrorKey) {
        super(s, newErrorKey);
    }

    /**
	 * Constructor with a message and a nested exception
	 *
	 * @param   s The exception message
	 * @param   newNested The nested item
	 */
    public QueryException(String message, Throwable newNested) {
        super(message, newNested);
    }

    /**
	 * Constructor with a single message and a nested exception with error key
	 *
	 * @param   s The exception message
	 * @param   newNested The nested item
	 * @param   errorKey A string key to the messages bundle
	 */
    public QueryException(String message, Throwable newNested, String newErrorKey) {
        super(message, newNested, newErrorKey);
    }

    /**
	 * Constructor with no message and a nested exception
	 *
	 * @param   newNested The nested exception
	 */
    public QueryException(Throwable newNested) {
        super(newNested);
    }

    /**
	 * Constructor with no message and a nested exception, but with an error key
	 *
	 * @param   newNested The nested exception
	 */
    public QueryException(Throwable newNested, String newErrorKey) {
        super(newNested, newErrorKey);
    }
}
