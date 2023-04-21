package org.simbrain.network.synapses;

import org.simbrain.network.interfaces.Synapse;
import org.simbrain.network.interfaces.SynapseUpdateRule;
import org.simbrain.network.util.RandomSource;

/**
 * <b>RandomSynapse</b>.
 */
public class RandomSynapse extends SynapseUpdateRule {

    /** Randomizer. */
    private RandomSource randomizer = new RandomSource();

    @Override
    public void init(Synapse synapse) {
    }

    @Override
    public String getDescription() {
        return "Random";
    }

    @Override
    public SynapseUpdateRule deepCopy() {
        RandomSynapse rs = new RandomSynapse();
        rs.randomizer = new RandomSource(randomizer);
        return rs;
    }

    @Override
    public void update(Synapse synapse) {
        randomizer.setUpperBound(synapse.getUpperBound());
        randomizer.setLowerBound(synapse.getLowerBound());
        synapse.setStrength(synapse.clip(randomizer.getRandom()));
    }

    /**
     * @return Returns the randomizer.
     */
    public RandomSource getRandomizer() {
        return randomizer;
    }
}
