package org.gcreator.pineapple.pinedl.statements;

import org.gcreator.pineapple.pinedl.Leaf;

/**
 * Represents a subtraction operation
 * @author Luís Reis
 */
public class SubtractionOperation extends BinaryOperation {

    public SubtractionOperation() {
    }

    public SubtractionOperation(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Leaf optimize() {
        left = (Expression) left.optimize();
        right = (Expression) right.optimize();
        if ((left instanceof IntConstant) && (right instanceof IntConstant)) {
            return new IntConstant(((IntConstant) left).value - ((IntConstant) right).value);
        }
        return this;
    }

    @Override
    public String toString() {
        return "-[" + left + ", " + right + "]";
    }
}
