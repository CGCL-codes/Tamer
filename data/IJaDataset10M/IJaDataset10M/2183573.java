package calclipse.core.util;

import calclipse.lib.math.rpn.Expression;
import calclipse.lib.math.rpn.Operand;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.Variable;
import calclipse.msg.MathMessages;

/**
 * A parameterized expression.
 * @author T. Sommerland
 */
public class Statement {

    private final Expression expression;

    private final Variable[] variables;

    public Statement(final Expression expression, final Variable... variables) {
        this.expression = expression;
        this.variables = variables.clone();
    }

    public Expression getExpression() {
        return expression;
    }

    public Variable[] getVariables() {
        return variables.clone();
    }

    /**
     * Evaluates this statement.
     * The parameters (variables) of this statement will be temporarily
     * assigned the given arguments before the evaluation.
     * @return a result containing the produced value or exception.
     * @throws RPNException if the wrong number of arguments was given.
     */
    public Result evaluate(final boolean interceptReturn, final Object... args) throws RPNException {
        if (args.length != variables.length) {
            final String message = MathMessages.getFunctionExpectsNArgs(this, variables.length);
            throw new RPNException(message);
        }
        final Object[] oldVals = new Object[variables.length];
        for (int i = 0; i < variables.length; i++) {
            oldVals[i] = variables[i].getValue();
            variables[i].setValue(args[i]);
        }
        try {
            final Operand op = expression.evaluate(interceptReturn);
            return new Result(op.getValue());
        } catch (final RPNException ex) {
            return new Result(ex);
        } finally {
            for (int i = 0; i < variables.length; i++) {
                variables[i].setValue(oldVals[i]);
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("statement(");
        for (int i = 0; i < variables.length; i++) {
            builder.append(variables[i].getName());
            if (i < variables.length - 1) {
                builder.append(", ");
            }
        }
        builder.append(")");
        return builder.toString();
    }
}
