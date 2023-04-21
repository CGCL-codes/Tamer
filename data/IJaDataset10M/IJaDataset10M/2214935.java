package edu.gatech.cc.concolic.expression;

public abstract class BinaryOperator implements SymbolicExpression {

    protected final SymbolicExpression lhs;

    protected final SymbolicExpression rhs;

    public BinaryOperator(SymbolicExpression lhs, SymbolicExpression rhs) {
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
        int result = 17;
        result = 37 * result + lhs.hashCode();
        result = 37 * result + rhs.hashCode();
        return result;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(getClass().isInstance(o))) return false;
        BinaryOperator other = (BinaryOperator) o;
        return lhs.equals(other.lhs) && rhs.equals(other.rhs);
    }
}
