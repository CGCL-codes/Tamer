package com.google.uritemplate.gregorio;

/**
 * The exception thrown when an operator is used the wrong way. For all of the
 * difference conditions, check {@link Usage}.
 * 
 * @author Wilfred Springer
 * 
 */
@SuppressWarnings("serial")
public class OperatorUsageException extends URITemplateParserException {

    /**
     * The different wrong ways of using the operator.
     * 
     */
    public enum Usage {

        MoreThanOneVariable, NonListVariable, NoVariables, UndefinedVariable
    }

    /**
     * The operator that is used the wrong way.
     */
    private String operator;

    /**
     * The wrong way of using the operator.
     */
    private Usage usage;

    /**
     * Constructs a new instance.
     * 
     * @param operator
     *            The operator that is used the wrong way.
     * @param usage
     *            The specific wrong way of using it.
     * @param position
     *            The position of the expansion containing the operator.
     */
    public OperatorUsageException(String operator, Usage usage, int position) {
        super(getMessage(usage, operator), position);
        this.operator = operator;
        this.usage = usage;
    }

    /**
     * The operator that is used the wrong way.
     * 
     * @return The operator that is used the wrong way.
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Returns the message.
     * 
     * @param usage
     *            The wrong way of using the operator.
     * @param operator
     *            The operator that is used wrongly.
     * @return A human readable message stating the error condition.
     */
    private static String getMessage(Usage usage, String operator) {
        switch(usage) {
            case MoreThanOneVariable:
                return "Operator '" + operator + "' allows only one variable.";
            case NonListVariable:
                return "Variable used with operator '" + operator + "' is expected too be list variable.";
            case NoVariables:
                return "Operator '" + operator + "' is missing variables.";
            case UndefinedVariable:
                return "Operator '" + operator + "' refers to undefined varible.";
            default:
                return null;
        }
    }

    /**
     * Returns the wrong way of using the operator.
     * 
     * @return The wrong way of using the operator.
     */
    public Usage getUsage() {
        return usage;
    }
}
