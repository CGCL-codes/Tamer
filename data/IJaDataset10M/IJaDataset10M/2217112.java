package org.mov.parser.expression;

import org.mov.parser.Expression;
import org.mov.parser.EvaluationException;
import org.mov.parser.TypeMismatchException;
import org.mov.parser.Variables;
import org.mov.quote.QuoteBundle;
import org.mov.quote.Symbol;

/**
 * An expression which represents the <code>while</code> command.
 * e.g. <pre>while(i++ < 10) {
 *   a += i;
 *}</pre>
 */
public class WhileExpression extends BinaryExpression {

    private static final int CONDITION = 0;

    private static final int COMMAND = 1;

    /**
     * Construct a <code>while</code> expression.
     * <pre>while(condition) {
     *   command
     *}</pre>
     * @param	condition loop condition test.
     * @param	command	the command to loop.
     */
    public WhileExpression(Expression condition, Expression command) {
        super(condition, command);
    }

    public double evaluate(Variables variables, QuoteBundle quoteBundle, Symbol symbol, int day) throws EvaluationException {
        double value = 0.0D;
        while (getChild(CONDITION).evaluate(variables, quoteBundle, symbol, day) >= Expression.TRUE_LEVEL) {
            value = getChild(COMMAND).evaluate(variables, quoteBundle, symbol, day);
        }
        return value;
    }

    public String toString() {
        String string = ("while(" + getChild(CONDITION) + ";" + ")");
        string = string.concat(ClauseExpression.toString(getChild(COMMAND)));
        return string;
    }

    /**
     * Check the input arguments to the expression. The arguments can be any
     * type except for the condition argument which must be {@link #BOOLEAN_TYPE}.
     *
     * @return	the type of the command argument.
     */
    public int checkType() throws TypeMismatchException {
        getChild(COMMAND).checkType();
        if (getChild(CONDITION).checkType() != BOOLEAN_TYPE) throw new TypeMismatchException(); else return getType();
    }

    /**
     * Get the type of the expression.
     *
     * @return the type of the command argument.
     */
    public int getType() {
        return getChild(COMMAND).getType();
    }

    public Object clone() {
        return new WhileExpression((Expression) getChild(0).clone(), (Expression) getChild(1).clone());
    }
}
