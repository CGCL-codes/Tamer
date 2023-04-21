package simpleorm.core;

/**
 * This Exception thrown by user written code to indicate user data entry
 * errors, especially in the business rule methods. The idea is that the calling
 * method can trap them and present neat messages to the user. Do not use this
 * exception for mysterious internal errors, only for well defined user errors
 * that do not require a stack trace.
 * <p>
 * 
 * ### Should add hooks for localizing messages etc.
 */
public class SValidationException extends RuntimeException {

    static final long serialVersionUID = 3L;

    SRecordInstance instance;

    /** message is formatted with MessageFormat and param. */
    public SValidationException(String message, Object param, SRecordInstance inst) {
        super(prettyMessage(message, param));
        instance = inst;
    }

    public SValidationException(String message, Object param) {
        this(message, param, null);
    }

    public SValidationException(String message) {
        this(message, null);
    }

    static String prettyMessage(String message, Object param) {
        Object[] params = null;
        if (param instanceof Object[]) params = (Object[]) param; else if (param == null) params = new Object[0]; else params = new Object[] { param };
        return java.text.MessageFormat.format(message, params);
    }

    public SRecordInstance getRecordInstance() {
        return instance;
    }
}
