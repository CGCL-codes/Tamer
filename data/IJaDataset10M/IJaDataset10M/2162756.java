package edu.gatech.cc.concolic.constraint;

import edu.gatech.cc.concolic.expression.SymbolicExpression;

public abstract class BinaryConstraint implements Constraint {

    protected final SymbolicExpression lhs;

    protected final SymbolicExpression rhs;

    public BinaryConstraint(SymbolicExpression lhs, SymbolicExpression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public SymbolicExpression left() {
        return lhs;
    }

    public SymbolicExpression right() {
        return rhs;
    }

    public abstract String toString();

    public int hashCode() {
        int result = 11;
        result = 37 * result + lhs.hashCode();
        result = 37 * result + rhs.hashCode();
        return result;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(getClass().isInstance(o))) return false;
        BinaryConstraint other = (BinaryConstraint) o;
        return lhs.equals(other.lhs) && rhs.equals(other.rhs);
    }
}
