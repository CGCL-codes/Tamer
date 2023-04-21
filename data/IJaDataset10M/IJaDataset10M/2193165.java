package org.dishevelled.swarm.exit;

import org.dishevelled.swarm.ExitStrategy;
import org.dishevelled.swarm.Particle;
import org.dishevelled.swarm.ParticleSwarm;

/**
 * Fitness threshold exit strategy.  Exits as soon as at least one particle has
 * a fitness score above a set threshold.
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class FitnessThresholdExitStrategy implements ExitStrategy {

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
    public boolean evaluate(final ParticleSwarm swarm, final int epoch) {
        for (Particle particle : swarm) {
            if (particle.getFitness() > threshold) {
                return true;
            }
        }
        return false;
    }
}
