package net.sourceforge.jasymcaandroid.jasymca;

public abstract class Variable {

    public abstract Algebraic deriv(Variable x) throws JasymcaException;

    public abstract boolean equals(Object x);

    public abstract boolean smaller(Variable v) throws JasymcaException;

    public abstract Algebraic value(Variable var, Algebraic x) throws JasymcaException;

    public Variable cc() throws JasymcaException {
        return this;
    }
}
