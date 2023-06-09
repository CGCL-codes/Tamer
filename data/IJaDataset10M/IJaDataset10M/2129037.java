package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ValueType;

/**
 * A negate operation
 *
 * @author nickmain
 */
public final class NegateExpression extends Expression {

    NegateExpression(Expression value) {
        super(value);
    }

    /** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visitNegate(child(0));
    }

    /** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
    @Override
    public ValueType type() {
        return child(0).type();
    }
}
