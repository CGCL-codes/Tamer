package net.sf.webwarp.util.exception;

import org.apache.commons.lang.Validate;

/**
 * Business rule exception (precondition violation) with parameters and internationalizable message text, and may be a cause. This class serves for the
 * framework user as a base class for defining business rule exceptions. Objects directly of this class can be thrown, too, but such exceptions cannot be
 * catched individually nor get internationalized.
 */
public class SystemException extends Exception implements SystemFailure {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1237046686809304659L;

    private final String defaultMessageTextPattern;

    /** Positional parameters of this exception, or null if no parameters are given. */
    private final Object[] parameters;

    private String errorCode;

    private ExceptionType exceptionType = ExceptionType.UNKNOWN;

    public SystemException(ExceptionType type, String errorCode, Object... parameters) {
        this(type, errorCode, null, null, parameters);
    }

    /**
     * Constructs an Exc with a default message text pattern, a cause, and exception parameters as an polymorphic Object[]. When to give a cause to an Exc is
     * discussed in the class description.
     * <P>
     * Example of defining an Exc with default text, cause and parameters:
     * 
     * <PRE>
     * public static class UserUnknownExc extends Exc {
     *     
     *     public UserUnknownExc(final Throwable cause, final String userName) {
     *         super(&quot;User ''{0}'' does not exist in this system.&quot;, cause, new Object[] { userName });
     *     }
     * }//UserUnknownExc
     * </PRE>
     * 
     * Instead of creating an Object[] yourself you can use the corresponding convenience constructors.
     * 
     * @param defaultMessageTextPattern
     *            The default message text pattern in the syntax of java.text.MessageFormat or null, if message text pattern shall only be taken from a
     *            ResourceBundle using the class name as key.
     * @param cause
     *            The causing Throwable object for providing the diagnostics causer chain. null is allowed here, if no cause is available or necessary.
     * @param parameters
     *            Exception parameters as a polymorphic Object[], which can be inserted into the message text pattern by placeholders {0} ... {9}. null is
     *            allowed here, if you do not want to provide exception parameters.
     */
    public SystemException(ExceptionType type, String errorCode, Throwable cause, Object... parameters) {
        this(type, errorCode, null, cause, parameters);
    }

    public SystemException(ExceptionType type, String errorCode, String defaultMessageTextPattern, Object... parameters) {
        this(type, errorCode, defaultMessageTextPattern, null, parameters);
    }

    public SystemException(ExceptionType type, String errorCode, String defaultMessageTextPattern, Throwable cause, Object... parameters) {
        super(cause);
        Validate.notNull(exceptionType, "exceptionType may not be null");
        Validate.notNull(errorCode, "errorCode may not be null");
        this.exceptionType = type;
        this.errorCode = errorCode;
        this.defaultMessageTextPattern = "".equals(defaultMessageTextPattern) ? null : defaultMessageTextPattern;
        this.parameters = parameters;
    }

    public String getMessage() {
        return Util.getUserInformation(this);
    }

    public final boolean hasParameters() {
        return this.parameters != null && this.parameters.length > 0;
    }

    /**
     * Returns the parameters of the exception. By default exception type and id are delivered as parameters 0,1.
     * 
     * @see net.sf.webwarp.util.exception.SystemFailure#getParameters()
     * @return The parameter array
     */
    public final Object[] getParameters() {
        return this.parameters;
    }

    public final String getDefaultMessageTextPattern() {
        return this.defaultMessageTextPattern;
    }

    public final String getErrorCode() {
        return errorCode;
    }

    public final ExceptionType getExceptionType() {
        return exceptionType;
    }

    /**
     * Prints the chained, compact stack traces of this <code>Failure</code> object to io_printer.
     * 
     * @see #printStackTrace(java.io.PrintWriter)
     */
    public void printStackTrace(java.io.PrintStream io_printer) {
        final StringBuffer result = new StringBuffer();
        Util.appendCompactStackTrace(result, this);
        io_printer.print(result);
    }

    /**
     * Prints the chained, compact stack traces of this <code>Failure</code> object to io_printer.
     */
    public void printStackTrace(java.io.PrintWriter io_printer) {
        final StringBuffer result = new StringBuffer();
        Util.appendCompactStackTrace(result, this);
        io_printer.print(result);
    }
}
