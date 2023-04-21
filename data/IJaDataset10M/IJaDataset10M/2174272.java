package org.opt4j.operator.neighbor;

import java.util.Random;
import org.opt4j.common.random.Rand;
import org.opt4j.genotype.DoubleGenotype;
import org.opt4j.operator.normalize.NormalizeDouble;
import com.google.inject.Inject;

/**
 * The {@link NeighborDouble} operator for the {@link DoubleGenotype}.
 * 
 * @author lukasiewycz
 * 
 */
public class NeighborDouble implements Neighbor<DoubleGenotype> {

    protected final Random random;

    protected final NormalizeDouble normalize;

    /**
	 * Constructs a {@link NeighborDouble}.
	 * 
	 * @param normalize
	 *            the operator for normalization
	 * @param random
	 *            the random number generator
	 */
    @Inject
    public NeighborDouble(final NormalizeDouble normalize, Rand random) {
        this.normalize = normalize;
        this.random = random;
    }

    @Override
    public void neighbor(DoubleGenotype genotype) {
        int size = genotype.size();
        int i = random.nextInt(size);
        double value = genotype.get(i) + random.nextDouble() * 0.1 - 0.05;
        genotype.set(i, value);
        normalize.normalize(genotype);
    }
}
