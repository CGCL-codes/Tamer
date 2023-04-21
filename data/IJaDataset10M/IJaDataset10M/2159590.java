package org.gcreator.pineapple.pinedl.statements;

import org.gcreator.pineapple.pinedl.Leaf;

/**
 * Represents a negation operation, such as -x.
 *
 * @author Serge Humphrey
 */
public class NegationOperation extends Operation {

    public Expression exp = null;

    public NegationOperation() {
    }

    public NegationOperation(Expression e) {
        this.exp = e;
    }

    @Override
    public Leaf optimize() {
        exp = (Expression) exp.optimize();
        return this;
    }

    @Override
    public String toString() {
        return "-" + exp;
    }
}
