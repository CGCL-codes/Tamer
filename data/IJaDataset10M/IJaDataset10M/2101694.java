package jecl.operators;

import jecl.*;
import java.util.*;

/**
 * Tournament selection operator.  The best individual from a randomly-chosen 
 * subset is selected.
 */
public class TournamentSelector implements Selector {

    int size;

    Random random;

    public TournamentSelector(int size) {
        this(size, new Random());
    }

    public TournamentSelector(int size, Random random) {
        this.size = size;
        this.random = random;
    }

    public int select(List<Chromosome> parents) {
        int best = random.nextInt(parents.size());
        for (int i = 1; i < size; i++) {
            int trial = random.nextInt(parents.size());
            if (parents.get(trial).getFitness() < parents.get(best).getFitness()) best = trial;
        }
        return best;
    }
}
