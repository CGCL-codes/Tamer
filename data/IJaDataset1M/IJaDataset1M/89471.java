package org.columba.calendar.parser;

/**
 * SyntaxException class
 * 
 * @author fdietz
 */
public class SyntaxException extends Exception {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = -3223992115866760040L;

    /**
	 * SyntaxException default constructor 
	 */
    public SyntaxException() {
        super();
    }

    /**
	 * SyntaxException parameterized constructor
	 * @param message
	 */
    public SyntaxException(String message) {
        super(message);
    }
}
