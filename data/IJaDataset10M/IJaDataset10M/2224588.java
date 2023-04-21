package gov.nist.atlas;

/**
 * ConstraintManager defines the behavior of elements to which
 * Constraints can be applied.
 *
 * @version $Revision: 1.6 $
 * @author Christophe Laprun
 *
 * @see Constraint
 */
public interface ConstraintManager {

    /**
   * Returns the set of the Constraints defined for this ConstraintManager.
   *
   * @return a possibly empty ConstraintSet containing all Constraints defined
   * for this ConstraintManager
   *
   * @see ConstraintSet
   */
    ConstraintSet getAllConstraints();

    /**
   * Returns the set of enabled Constraints for this ConstraintManager.
   *
   * @return a possibly empty ConstraintSet containing only this
   * ConstraintManager's enabled Constraints
   *
   * @see ConstraintSet
   */
    ConstraintSet getEnabledConstraints();

    /**
   * Enables or disables the Constraint managed by this ConstraintManager and
   * identified by the specified name.
   *
   * @param name the name of the Constraint which activation
   * status is to be set
   * @param enabled a boolean indicating the wished status for the named
   * Constraint: <code>true</code> to activate the
   * specified Constraint, <code>false</code> to de-activate it.
   *
   * @return    <code>true</code> if the status of the named Constraint has
   * been changed, <code>false</code> otherwise
   * (this includes the case where this ConstraintManager doesn't
   * manage any Constraint with the given name).
   */
    boolean enableConstraint(String name, boolean enabled);
}
