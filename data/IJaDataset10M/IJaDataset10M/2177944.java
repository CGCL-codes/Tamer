package org.dishevelled.evolve.exit;

import java.util.Collection;
import org.dishevelled.weighted.WeightedMap;
import org.dishevelled.evolve.ExitStrategy;

/**
 * Fitness threshold exit strategy.  Exits as soon as
 * at least one individual has a fitness score above a
 * set threshold.
 *
 * @param <I> individual type
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public final class FitnessThresholdExitStrategy<I> implements ExitStrategy<I> {

    /** Threshold for this fitness threshold exit strategy. */
    private final double threshold;

    /**
     * Create a new fitness threshold exit strategy with the specified threshold.
     *
     * @param threshold threshold for this fitness threshold exit strategy
     */
    public FitnessThresholdExitStrategy(final double threshold) {
        this.threshold = threshold;
    }

    /** {@inheritDoc} */
    public boolean evaluate(final Collection<I> population, final WeightedMap<I> scores, final int time) {
        for (Double fitness : scores.values()) {
            if (fitness > threshold) {
                return true;
            }
        }
        return false;
    }
}
