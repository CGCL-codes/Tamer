package org.rt.credential;

import org.rt.util.Debug;

/** Represents range, tree, or set type constraints. **/
public abstract class SimpleConstraint {

    static final String NL = System.getProperty("line.separator");

    abstract boolean isSatisfiable();

    abstract boolean isConstrained();

    abstract boolean subsumes(SimpleConstraint other) throws ConjunctionException;

    abstract String toProfile();

    abstract String toRTML();

    @Override
    public abstract String toString();

    @Override
    protected abstract SimpleConstraint clone();

    public static SimpleConstraint conjoin(SimpleConstraint arg1, SimpleConstraint arg2) throws ConjunctionException {
        if (!arg1.isConstrained()) {
            return arg2.clone();
        }
        if (!arg2.isConstrained()) {
            return arg1.clone();
        }
        if (arg1 instanceof RangeConstraint && arg2 instanceof RangeConstraint) {
            return RangeConstraint.conjoin((RangeConstraint) arg1, (RangeConstraint) arg2);
        } else if (arg1 instanceof TreeConstraint && arg2 instanceof TreeConstraint) {
            return TreeConstraint.conjoin((TreeConstraint) arg1, (TreeConstraint) arg2);
        } else if (arg1 instanceof SetConstraint && arg2 instanceof SetConstraint) {
            return SetConstraint.conjoin((SetConstraint) arg1, (SetConstraint) arg2);
        } else {
            if (Debug.verbosityLevel >= Debug.HIGH) {
                System.out.println(arg1.toString() + NL + arg2.toString());
            }
            throw new ConjunctionException("Tried to conjoin two SimpleConstraints of" + " different types");
        }
    }
}
