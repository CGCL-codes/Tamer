package de.lodde.jnumwu.formula;

import java.text.NumberFormat;
import java.util.Set;

/**
 * Represents an variable within an expression
 * @author Georg Lodde
 */
public class Variable extends Expression {

    private static final long serialVersionUID = -599399916863771389L;

    private final String name;

    Constant value;

    /**
     * Creates a new varialbe object
     * @param name
    
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    @Override
    public Expression evaluate() {
        if (value != null) {
            return value;
        }
        return this;
    }

    /**
     * Returns the variable name
     * @return variable name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value
     * @return value
     */
    public Constant getValue() {
        return value;
    }

    /**
     * Sets the value of this varialbe
     * @param value
     */
    public void setValue(Constant value) {
        this.value = value;
    }

    @Override
    public StringBuilder toHTMLString(StringBuilder buf, NumberFormat numberFormat) {
        if (value != null) {
            buf.append(value.toHTMLString(buf, numberFormat));
        } else {
            buf.append(HtmlOutput.VARIABLE_TAG);
            buf.append(name);
            buf.append(HtmlOutput.VARIABLE_TAG_END);
        }
        return buf;
    }

    @Override
    public String toString(NumberFormat numberFormat) {
        StringBuilder buf = new StringBuilder();
        if (value != null) buf.append(value.toString(numberFormat)); else buf.append(name);
        return buf.toString();
    }

    @Override
    public void getVariables(Set<Variable> variables) {
        variables.add(this);
    }

    @Override
    public boolean hasConstant() {
        return false;
    }

    @Override
    public boolean isBraceless(Expression parent) {
        return true;
    }

    @Override
    public boolean isAssociative(Expression parent) {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Variable other = (Variable) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public Expression evaluate(Expression which, int count) {
        if (count != 1) throw new UnsupportedOperationException("Not supported yet.");
        return which.evaluate();
    }

    @Override
    public boolean hasUnits() {
        return value != null ? value.hasUnits() : false;
    }
}
