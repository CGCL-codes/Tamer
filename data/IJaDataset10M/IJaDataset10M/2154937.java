package tudresden.ocl20.core.parser.astlib;

import tudresden.ocl20.core.jmi.ocl.expressions.OclExpression;

/**
 *
 * AST node for an "inv" classifier constraint. Specialized subclass of 
 * OclClassifierConstraint.
 *
 * @author Ansgar Konermann
 * @version
 */
public class OclInvariantClassifierConstraint extends OclClassifierConstraint {

    /**
     * Holds value of property invariant.
     */
    private OclExpression invariant;

    /** Creates new OclInvariantClassifierConstraint */
    public OclInvariantClassifierConstraint() {
        super(OclClassifierConstraint.ConstraintType.INVARIANT);
    }

    /**
     * Getter for property invariant.
     * @return Value of property invariant.
     */
    public OclExpression getInvariant() {
        return this.invariant;
    }

    /**
     * Setter for property invariant.
     * @param invariant New value of property invariant.
     */
    public void setInvariant(OclExpression invariant) {
        this.invariant = invariant;
    }
}
