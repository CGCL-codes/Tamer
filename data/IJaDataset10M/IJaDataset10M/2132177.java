package de.tum.in.botl.math.interfaces;

import java.util.Set;

/**
 * @author marschal
 */
public interface ExpressionParserInterface {

    /**
   * Parse the given expression. An expression parser must parse an expression bevore it can be evaluated with getValue()
   * @param s The expression to be parsed
   */
    public void parseExpression(String s);

    /**
   * @return true if any error occured during parsing, evaluation, etc. so far, false otherwise
   */
    public boolean hasError();

    /**
   * @return A set that contains a string representation of all variable names that occur in the parsed expression.
   * Returns null if the parsed String is no valid expression.
   */
    public Set getVarNames();

    /**
   * @return true if the parsed expression contains any user defined functions, false otherwise.
   * Returns false if the parsed String is no valid expression.
   */
    public boolean containsCustomFunctions();

    /**
   * Get information about an error
   * @return Information about the last error that occured, if any error did occur, null otherwise
   */
    public String getErrorInfo();

    /**
   * @return true if the parsed expression is a constant value (like e.g. 42, true, "Hello"), 
   * false otherwise (e.g. a+b, 13-8, "He"+"llo", true && true)
   * Returns false if the parsed String is no valid expression.
   */
    public boolean isConstantValue();

    /**
   * @return true if the parsed expression is a singele variable (e.g. a, var17, ...),
   * false otherwise (e.g. a + 3, sin(a), pi, false, ...).
   * Returns false if the parsed String is no valid expression.
   */
    public boolean isVariable();

    /**
   * Returns a string representation of the expression that contains no decimals
   * E.g. 10.76 is converted into (10+76*10^(-2)).
   * Returns false if the parsed String is no valid expression.
   * @return The decimal number free representation of the expression
   */
    public String getDecimalFreeRepresentation();

    public void addVariable(String name, Object value);

    /**
 * @return The value of the evaluated expression.
 * Returns null if the parsed String is no valid expression or cannot be evaluated.
 */
    public Object getValue();
}
