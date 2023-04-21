package org.opt4j.operator.crossover;

import org.opt4j.common.random.Rand;
import com.google.inject.Inject;

/**
 * The {@code MidpointCrossoverOperator} performs a crossover for two integer
 * values with the result being the integer midpoint between them.
 * 
 * @author glass
 * 
 */
public class MidpointCrossoverOperator implements IntegerCrossoverOperator {

    protected final Rand random;

    /**
	 * Constructs the {@code MidpointCrossoverOperator}.
	 * 
	 * @param random the random number generator
	 */
    @Inject
    public MidpointCrossoverOperator(Rand random) {
        this.random = random;
    }

    public int crossover(int x, int y) {
        int z;
        if (y > x) {
            z = ((y - x) / 2) + x;
        } else {
            z = ((x - y) / 2) + y;
        }
        return z;
    }
}
