package org.epistem.code.expressions;

import org.epistem.code.Expression;
import org.epistem.code.type.JavaType;
import org.epistem.code.type.PrimitiveType;

/**
 * A primitive conversion
 *
 * @author nickmain
 */
public final class ConvertExpression extends UnaryOp {

    /**
     * The type to convert to
     */
    public final PrimitiveType type;

    ConvertExpression(PrimitiveType type, Expression operand) {
        super(operand);
        this.type = type;
    }

    /** @see org.epistem.code.Expression#accept(org.epistem.code.expressions.ExpressionVisitor) */
    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visitConvert(this);
    }

    /** @see org.epistem.code.Expression#type() */
    @Override
    public JavaType type() {
        return type;
    }
}
