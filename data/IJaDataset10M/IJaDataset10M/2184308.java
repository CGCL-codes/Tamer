package com.thesett.aima.logic.fol.prolog.builtins;

import com.thesett.aima.logic.fol.Clause;

/**
 * FirstStepBuiltIn takes a query (a headless clause) in first order logic, and creates continuation steps for each
 * conjunctive component of the query, as a proof step on a functor. The first step of a proof is different from the
 * other steps, because it operates on a query clause and not on a functor.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Turn a query into a conjunctive set of subsequent proof states.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class FirstStepBuiltIn extends BaseBuiltIn {

    /**
     * Creates a first step built-in to implement the first step of turning a query clause into a conjunction of goals
     * to be proved.
     */
    public FirstStepBuiltIn() {
        super(null);
    }

    /** {@inheritDoc} */
    public boolean proofStep(ResolutionState state) {
        Clause query = state.getCurrentClause();
        for (int i = query.getBody().length - 1; i >= 0; i--) {
            BuiltInFunctor newGoal = state.getBuiltInTransform().apply(query.getBody()[i]);
            state.getGoalStack().offer(newGoal);
        }
        return true;
    }
}
