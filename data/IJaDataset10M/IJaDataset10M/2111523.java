package jdice.expression;

import jdice.values.Function;
import jdice.values.ListValue;
import jdice.values.Value;

/**
 * Base class for expressions that natively evaluate to a function value.
 * @author phatonin
 *
 */
public abstract class AbstractFunctionExpression extends AbstractExpression {

    @Override
    public abstract Function evaluateFunction(EvaluationContext ctx, Scope scope);

    @Override
    public int evaluateInt(EvaluationContext ctx, Scope scope) {
        return evaluateFunction(ctx, scope).getInt();
    }

    @Override
    public ListValue evaluateList(EvaluationContext ctx, Scope scope) {
        return evaluateFunction(ctx, scope).getList();
    }

    @Override
    public String evaluateString(EvaluationContext ctx, Scope scope) {
        return evaluateFunction(ctx, scope).getString();
    }

    @Override
    public Value evaluate(EvaluationContext ctx, Scope scope) {
        return evaluateFunction(ctx, scope);
    }

    @Override
    public boolean evaluateBoolean(EvaluationContext ctx, Scope scope) {
        return evaluateFunction(ctx, scope).getBoolean();
    }
}
