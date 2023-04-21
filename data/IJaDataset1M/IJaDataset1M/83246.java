package org.gcreator.pineapple.pinedl.statements;

import org.gcreator.pineapple.pinedl.Leaf;

/**
 * Represents a throw statement
 * @author Luís Reis
 */
public class ThrowStatement extends Leaf {

    public Expression value = null;

    @Override
    public Leaf optimize() {
        value = (Expression) value.optimize();
        return this;
    }

    @Override
    public String toString() {
        return "throw[" + value + "]";
    }
}
