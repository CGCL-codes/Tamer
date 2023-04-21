package JCL.OLD;

import JCL.*;

/**
 * Consistency solution manager interface for 
 * the Java Constraint Library.
 *
 * @author Marc Torrens
 * @version 15-11-96
 */
public interface SolutionManagerInterfaceConsistency {

    public void NotifyStart(CSP problem, SolverConsistency solver, SolutionAttributesConsistency attributes);

    public void NotifyRemoveValue();

    public void NotifyRemoveConstraint();

    public void NotifyEnd(boolean ac, CSP problem);
}
