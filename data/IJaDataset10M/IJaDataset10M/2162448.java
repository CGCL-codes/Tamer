package org.jquantlib.math.randomnumbers;

import org.jquantlib.QL;
import org.jquantlib.math.distributions.InverseCumulativeNormal;

/**
 * @author Richard Gomes
 */
public class PseudoRandom extends GenericPseudoRandom<MersenneTwisterUniformRng, InverseCumulativeNormal> {

    public PseudoRandom(final Class<? extends UniformRandomSequenceGenerator> classRNG, final Class<? extends InverseCumulative> classIC) {
        super(classRNG, classIC);
    }

    @Override
    public InverseCumulativeRsg<RandomSequenceGenerator<MersenneTwisterUniformRng>, InverseCumulativeNormal> makeSequenceGenerator(final int dimension, final long seed) {
        QL.validateExperimentalMode();
        return super.makeSequenceGenerator(dimension, seed);
    }
}
