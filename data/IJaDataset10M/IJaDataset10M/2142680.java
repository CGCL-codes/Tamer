package com.rapidminer.operator;

/**
 * Exception class whose instances are thrown during the creation of operators.
 * 
 * @author Ingo Mierswa, Peter B. Volk
 *          ingomierswa Exp $
 */
public class OperatorCreationException extends Exception {

    private static final long serialVersionUID = 805882946295847566L;

    public static final int INSTANTIATION_ERROR = 0;

    public static final int ILLEGAL_ACCESS_ERROR = 1;

    public static final int NO_CONSTRUCTOR_ERROR = 2;

    public static final int CONSTRUCTION_ERROR = 3;

    public static final int NO_DESCRIPTION_ERROR = 4;

    public static final int NO_UNIQUE_DESCRIPTION_ERROR = 5;

    public static final int OPERATOR_DISABLED_ERROR = 6;

    /**
	 * Code must be one of the constants of this class. The classname should
	 * define the operator.
	 */
    public OperatorCreationException(int code, String className, Throwable cause) {
        super(createMessage(code, className, cause), cause);
    }

    private static String createMessage(int code, String className, Throwable cause) {
        switch(code) {
            case INSTANTIATION_ERROR:
                return "Cannot instantiate '" + className + "': " + cause.getMessage();
            case ILLEGAL_ACCESS_ERROR:
                return "Cannot access '" + className + "':" + cause.getMessage();
            case NO_CONSTRUCTOR_ERROR:
                return "No public one-argument constructor for operator descriptions: '" + className + "': " + cause.getMessage();
            case CONSTRUCTION_ERROR:
                return "Operator cannot be constructed: '" + className + "': " + cause.getCause().getMessage();
            case NO_DESCRIPTION_ERROR:
                return "No operator description object given for '" + className + (cause != null ? "': " + cause.getMessage() : "'");
            case NO_UNIQUE_DESCRIPTION_ERROR:
                return "No unique operator description object available for class '" + className + "': " + cause.getMessage();
            case OPERATOR_DISABLED_ERROR:
                return "Operator " + className + " is disabled.";
            default:
                return "Error during operator creation of '" + className + "': " + ((cause != null) ? cause.getMessage() : "");
        }
    }
}
