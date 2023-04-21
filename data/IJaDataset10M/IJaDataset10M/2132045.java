package jlambda.term;

import java.math.BigInteger;

public class LTermInteger extends LTermNumber {

    /**
     * 
     */
    private static final long serialVersionUID = 300800493L;

    private BigInteger value;

    public LTermInteger() {
    }

    public LTermInteger(BigInteger i) {
        setValue(i);
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() {
        return value;
    }

    public String toString() {
        return value.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof LTermInteger) return (this.getValue().equals(((LTermInteger) o).getValue())); else return false;
    }

    @Override
    public LTerm copy() {
        return new LTermInteger(this.getValue());
    }

    @Override
    public boolean eq(LTerm other) {
        if (other == ConstantExpressions.NULL) return false; else if (!(other instanceof LTermInteger)) return false; else return (((LTermInteger) other).getValue().compareTo(this.getValue()) == 0);
    }

    @Override
    public boolean equal(LTerm other) {
        return (this == other);
    }
}
