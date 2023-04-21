package org.opensourcephysics.ejs.control.value;

/**
 * Indicates that an error occured in parser operation, and the operation
 * could not be completed. Used internally in <code>Parser</code> class.
 *
 * @see Parser
 */
public final class ParserException extends Exception {

    public static final int SYNTAX_ERROR = -1;

    private int errorcode;

    /**
   * The constructor of <code>ParserException</code>.
   *
   * @param code the error code
   */
    public ParserException(int code) {
        super();
        errorcode = code;
    }

    public ParserException(String msg) {
        super(msg);
        errorcode = SYNTAX_ERROR;
    }

    /**
   * Gets the error code.
   *
   * @return the error code
   */
    public int getErrorCode() {
        return errorcode;
    }
}
