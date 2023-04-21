package ec.rule.breed;

import ec.rule.*;
import ec.*;
import ec.util.*;

/**
 *
 RuleMutationPipeline is a BreedingPipeline which implements a simple default Mutation
 for RuleIndividuals.  Normally it takes an individual and returns a mutated 
 child individual. RuleMutationPipeline works by calling mutateRules(...) on each RuleSet in the 
 parent individual.
 
 <p><b>Typical Number of Individuals Produced Per <tt>produce(...)</tt> call</b><br>
 1

 <p><b>Number of Sources</b><br>
 1

 <p><b>Default Base</b><br>
 rule.mutate (not that it matters)

 * @author Sean Luke
 * @version 1.0
 */
public class RuleMutationPipeline extends BreedingPipeline {

    public static final String P_MUTATION = "mutate";

    public static final int INDS_PRODUCED = 1;

    public static final int NUM_SOURCES = 1;

    public Parameter defaultBase() {
        return RuleDefaults.base().push(P_MUTATION);
    }

    /** Returns 1 */
    public int numSources() {
        return NUM_SOURCES;
    }

    public int typicalIndsProduced() {
        return (INDS_PRODUCED);
    }

    public int produce(final int min, final int max, final int start, final int subpopulation, final Individual[] inds, final EvolutionState state, final int thread) {
        int n = sources[0].produce(min, max, start, subpopulation, inds, state, thread);
        if (!state.random[thread].nextBoolean(likelihood)) return reproduce(n, start, subpopulation, inds, state, thread, false);
        if (!(sources[0] instanceof BreedingPipeline)) for (int q = start; q < n + start; q++) inds[q] = (Individual) (inds[q].clone());
        for (int q = start; q < n + start; q++) {
            ((RuleIndividual) inds[q]).preprocessIndividual(state, thread);
            ((RuleIndividual) inds[q]).mutate(state, thread);
            ((RuleIndividual) inds[q]).postprocessIndividual(state, thread);
            ((RuleIndividual) inds[q]).evaluated = false;
        }
        return n;
    }
}
