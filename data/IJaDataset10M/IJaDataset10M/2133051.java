package org.opt4j.optimizer.ea;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.opt4j.common.random.Rand;
import org.opt4j.core.Individual;
import org.opt4j.operator.diversity.Diversity;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;

/**
 * The {@code PairCouplerSimilarity} selects one parent and searches randomly
 * for a most similar parent in the parents to form a pair.
 * 
 * @author glass
 * 
 */
public class PairCouplerSimilarity implements PairCoupler {

    @Retention(RUNTIME)
    @BindingAnnotation
    protected @interface Candidates {
    }

    protected final Diversity diversity;

    protected final Rand random;

    protected final int candidates;

    /**
	 * Constructs a {@code PairCouplerSimilarity} with a {@code Diversity}
	 * operator, a {@code Random} number generator, and the number of candidates
	 * that should be tested for similarity.
	 * 
	 * @param diversity
	 *            the diversity operator
	 * 
	 * @param random
	 *            the random number generator
	 * 
	 * @param candidates
	 *            the number of candidates that are tested for similarity
	 * 
	 */
    @Inject
    public PairCouplerSimilarity(Diversity diversity, Rand random, @Candidates int candidates) {
        this.diversity = diversity;
        this.random = random;
        this.candidates = candidates;
    }

    public Collection<Pair<Individual>> getCouples(int size, List<Individual> parents) {
        Collection<Pair<Individual>> couples = new ArrayList<Pair<Individual>>();
        while (couples.size() < size) {
            Individual first = parents.get(random.nextInt(parents.size()));
            Individual second = parents.get(random.nextInt(parents.size()));
            double minDistance = 1;
            for (int i = 0; i < candidates; i++) {
                Individual candidate = parents.get(random.nextInt(parents.size()));
                double candidatesDistance = diversity.diversity(first.getGenotype(), candidate.getGenotype());
                if (candidatesDistance < minDistance && candidatesDistance != 0.0) {
                    second = candidate;
                    minDistance = candidatesDistance;
                }
            }
            Pair<Individual> pair = new Pair<Individual>(first, second);
            couples.add(pair);
        }
        return couples;
    }
}
