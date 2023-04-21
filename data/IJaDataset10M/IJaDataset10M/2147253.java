package ec.app.tutorial3;

import ec.*;
import ec.util.*;

public class OurSelection extends SelectionMethod {

    public static final String P_OURSELECTION = "our-selection";

    public Parameter defaultBase() {
        return new Parameter(P_OURSELECTION);
    }

    public static final String P_MIDDLEPROBABILITY = "middle-probability";

    public double middleProbability;

    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state, base);
        Parameter def = defaultBase();
        middleProbability = state.parameters.getDoubleWithMax(base.push(P_MIDDLEPROBABILITY), def.push(P_MIDDLEPROBABILITY), 0.0, 1.0);
        if (middleProbability < 0.0) state.output.fatal("Middle-Probability must be between 0.0 and 1.0", base.push(P_MIDDLEPROBABILITY), def.push(P_MIDDLEPROBABILITY));
    }

    public int produce(final int subpopulation, final EvolutionState state, final int thread) {
        if (state.random[thread].nextBoolean(middleProbability)) {
            Individual[] inds = state.population.subpops[subpopulation].individuals;
            int one = state.random[thread].nextInt(inds.length);
            int two = state.random[thread].nextInt(inds.length);
            int three = state.random[thread].nextInt(inds.length);
            if (inds[two].fitness.betterThan(inds[one].fitness)) {
                if (inds[three].fitness.betterThan(inds[two].fitness)) return two; else if (inds[three].fitness.betterThan(inds[one].fitness)) return three; else return one;
            } else if (inds[three].fitness.betterThan(inds[one].fitness)) return one; else if (inds[three].fitness.betterThan(inds[two].fitness)) return three; else return two;
        } else {
            return state.random[thread].nextInt(state.population.subpops[subpopulation].individuals.length);
        }
    }
}
