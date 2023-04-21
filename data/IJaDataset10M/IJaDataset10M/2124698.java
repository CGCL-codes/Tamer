package jolie.runtime;

import jolie.process.TransformationReason;

public class CastStringExpression implements Expression {

    private final Expression expression;

    public CastStringExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression cloneExpression(TransformationReason reason) {
        return new CastStringExpression(expression.cloneExpression(reason));
    }

    public Value evaluate() {
        return Value.create(expression.evaluate().strValue());
    }
}
