package org.ujac.util.exi;

/**
 * Name: NoOperandException<br>
 * Description: The base class for operand related exceptions<br/>
 * 
 * @author lauerc
 */
public class OperandException extends ExpressionException {

    /** The serial version UID. */
    static final long serialVersionUID = -6796126490721961533L;

    /**
   * Constructs a NoOperandException instance with no specific arguments.
   */
    public OperandException() {
    }

    /**
   * Constructs a NoOperandException instance with specific arguments.
   * @param msg The error message.
   */
    public OperandException(String msg) {
        super(msg);
    }

    /**
   * Constructs a NoOperandException instance with specific arguments.
   * @param msg The error message.
   * @param cause The original cause.
   */
    public OperandException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
