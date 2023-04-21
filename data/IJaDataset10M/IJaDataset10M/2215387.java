package jopt.csp.solution;

import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Solution to an double variable
 */
public class DoubleSolution implements VariableSolution {

    private CspDoubleVariable var;

    private double min;

    private double max;

    /**
     * Creates solution for a variable
     */
    protected DoubleSolution(CspDoubleVariable var) {
        this.var = var;
        store();
    }

    public CspVariable getVariable() {
        return var;
    }

    public boolean isBound() {
        return min == max;
    }

    /**
     * Retrieves minimum value of solution
     */
    public double getMin() {
        return min;
    }

    /**
     * Retrieves maximum value of solution
     */
    public double getMax() {
        return max;
    }

    /**
     * Sets minimum value of solution
     */
    public void setMin(double min) {
        this.min = min;
    }

    /**
     * Sets maximum value of solution
     */
    public void setMax(double max) {
        this.max = max;
    }

    /**
     * Sets both the min / max value of solution to a single value 
     */
    public void setValue(double val) {
        this.min = val;
        this.max = val;
    }

    /**
     * Returns the value of the solution.  This function will
     * throw an exception if the variable is not already bound. 
     */
    public double getValue() {
        if (!isBound()) throw new RuntimeException("variable is not bound to a single value");
        return min;
    }

    public void store() {
        this.min = var.getMin();
        this.max = var.getMax();
    }

    public void restore() throws PropagationFailureException {
        var.setMin(min);
        var.setMax(max);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (var.getName() == null) buf.append("~"); else buf.append(var.getName());
        buf.append(":");
        buf.append("[");
        if (isBound()) {
            buf.append(min);
        } else {
            buf.append(min);
            buf.append("...");
            buf.append(max);
        }
        buf.append("]");
        return buf.toString();
    }

    public Object clone() {
        DoubleSolution s = new DoubleSolution(var);
        s.min = min;
        s.max = max;
        return s;
    }

    public int hashCode() {
        return var.getName().hashCode();
    }

    public boolean equals(Object obj) {
        if (!getClass().isInstance(obj)) return false;
        DoubleSolution n = (DoubleSolution) obj;
        if (!n.var.equals(var)) return false;
        if (n.isBound() != isBound()) return false;
        if (n.min != min) return false;
        if (n.max != max) return false;
        return true;
    }
}
