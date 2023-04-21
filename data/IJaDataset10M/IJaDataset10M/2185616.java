package com.pp.cameleon.api.validation;

import java.util.Collection;

/**
 * ValidationFailedException
 */
public class ValidationFailedException extends Exception {

    private Collection<String> errors;

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ValidationFailedException() {
    }

    /**
     * Creates an exception with know errors.
     *
     * @param errors The list of errors.
     */
    public ValidationFailedException(Collection<String> errors) {
        this.errors = errors;
    }

    /**
     * @return The errors.
     */
    public Collection<String> getErrors() {
        return errors;
    }

    /**
     * @param errors The errors.
     */
    public void setErrors(Collection<String> errors) {
        this.errors = errors;
    }

    /**
     * Returns a short description of this throwable.
     * If this <code>Throwable</code> object was created with a non-null detail
     * message string, then the result is the concatenation of three strings:
     * <ul>
     * <li>The name of the actual class of this object
     * <li>": " (a colon and a space)
     * <li>The result of the {@link #getMessage} method for this object
     * </ul>
     * If this <code>Throwable</code> object was created with a <tt>null</tt>
     * detail message string, then the name of the actual class of this object
     * is returned.
     *
     * @return a string representation of this throwable.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("<ValidationFailedException>\n");
        buf.append("<errors>\n");
        for (String error : errors) {
            buf.append("<error>\n");
            buf.append(error);
            buf.append("\n</error>\n");
        }
        buf.append("</errors>\n");
        buf.append("</ValidationFailedException>\n");
        return buf.toString();
    }
}
