package net.bervini.rasael.mathexplorer.Tree;

import net.bervini.rasael.mathexplorer.math.Frazione;

/**
 *
 * @author Rasael
 */
public final class ConstantNode extends NumberNode {

    private String constant = "";

    private double value = 0;

    public ConstantNode(String constant, double value) {
        super(0);
        setConstant(constant);
        setValue(value);
    }

    @Override
    public ConstantNode clone() {
        return new ConstantNode(constant, value);
    }

    @Override
    public boolean equals(Object b) {
        if (!(b instanceof ConstantNode)) {
            return false;
        }
        return ((ConstantNode) b).getValue().equals(this.getValue());
    }

    public ConstantNode(String constant) {
        super(0);
        setConstant(constant);
        if (getConstant().equals("PI")) {
            setValue(Math.PI);
        }
        if (getConstant().equals("E")) {
            setValue(Math.E);
        }
    }

    public ConstantNode(double value) {
        super(0);
        setValue(value);
    }

    /**
     * @return the constant
     */
    public String getConstant() {
        return constant;
    }

    /**
     * @param constant the constant to set
     */
    public void setConstant(String constant) {
        this.constant = constant.toUpperCase();
    }

    /**
     * @return the value
     */
    public double doubleValue() {
        return value;
    }

    @Override
    public Frazione getValue() {
        return new Frazione(value);
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    public String toString() {
        if (getConstant() != null) {
            return getConstant();
        }
        return "" + getValue();
    }
}
