package com.cosmos.acacia.crm.assembling;

import java.io.Serializable;

/**
 *
 * @author Miro
 */
public class ConstraintValues implements Serializable {

    public static final ConstraintValues EMPTY_CONSTRAINT_VALUES = new ConstraintValues();

    private Comparable minConstraint;

    private Comparable maxConstraint;

    private ConstraintValues() {
    }

    public ConstraintValues(Comparable minConstraint) {
        this(minConstraint, minConstraint);
    }

    public ConstraintValues(Comparable minConstraint, Comparable maxConstraint) {
        this.minConstraint = minConstraint;
        this.maxConstraint = maxConstraint;
    }

    public Comparable getMaxConstraint() {
        return maxConstraint;
    }

    public void setMaxConstraint(Comparable maxConstraint) {
        this.maxConstraint = maxConstraint;
    }

    public Comparable getMinConstraint() {
        return minConstraint;
    }

    public void setMinConstraint(Comparable minConstraint) {
        this.minConstraint = minConstraint;
    }

    public boolean isEmpty() {
        return minConstraint == null && maxConstraint == null;
    }

    @Override
    public String toString() {
        return "ConstraintValues[" + minConstraint + ", " + maxConstraint + "]";
    }
}
