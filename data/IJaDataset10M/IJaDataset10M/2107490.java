package org.engine;

public class IllegalGameActionException extends Exception {

    /**
	 *
	 */
    private static final long serialVersionUID = 5273938527978246474L;

    public IllegalGameActionException() {
        super();
    }

    public IllegalGameActionException(String string, Throwable cause) {
        super(string, cause);
    }

    public IllegalGameActionException(Throwable cause) {
        super(cause);
    }

    public IllegalGameActionException(String string) {
        super(string);
    }
}
