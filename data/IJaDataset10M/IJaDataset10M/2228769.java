package org.pprun.hjpetstore.service.rest;

/**
 * A validation RuntimeException defined by a <b>errorCode</b> and freestyle message.
 * The errorCode can be used by the client to interpret a particular error.
 *
 * @author <a href="mailto:quest.run@gmail.com">pprun</a>
 */
public class CodedValidationException extends RuntimeException {

    private String errorCode;

    /**
     * Creates a new instance of <code>CodedValidationException</code> without detail message.
     */
    public CodedValidationException() {
    }

    /**
     * Constructs an instance of <code>CodedValidationException</code> with the specified detail message.
     * @param message the detail message.
     */
    public CodedValidationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CodedValidationException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.getClass().getName()).append("[");
        buffer.append("errorCode=").append(errorCode);
        buffer.append(", message=").append(getMessage());
        buffer.append("]");
        return buffer.toString();
    }
}
