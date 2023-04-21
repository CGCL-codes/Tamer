package org.opt4j.genotype;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Random;
import org.opt4j.core.Genotype;

/**
 * The {@link DoubleGenotype} consists of double values that can be used as a
 * {@link Genotype}.
 * 
 * @author lukasiewycz
 * 
 */
@SuppressWarnings("serial")
public class DoubleGenotype extends ArrayList<Double> implements ListGenotype<Double> {

    protected final Bounds<Double> bounds;

    /**
	 * Constructs a {@link DoubleGenotype} with lower bounds {@code 0.0} and
	 * upper bounds {@code 1.0}.
	 */
    public DoubleGenotype() {
        this(0, 1);
    }

    /**
	 * Constructs a {@link DoubleGenotype} with a specified lower and upper
	 * bound for all values.
	 * 
	 * @param lowerBound
	 *            the lower bound
	 * @param upperBound
	 *            the upper bound
	 */
    public DoubleGenotype(double lowerBound, double upperBound) {
        this(new FixedBounds<Double>(lowerBound, upperBound));
    }

    /**
	 * Constructs a {@link DoubleGenotype} with the given {@link Bounds}.
	 * 
	 * @param bounds
	 *            the bounds
	 */
    public DoubleGenotype(Bounds<Double> bounds) {
        this.bounds = bounds;
    }

    /**
	 * Returns the lower bound for the {@code i}-th element.
	 * 
	 * @param index
	 *            the {@code i}-th element
	 * @return the lower bound of the {@code i}-th element
	 */
    public double getLowerBound(int index) {
        return bounds.getLowerBound(index);
    }

    /**
	 * Returns the upper bound for the {@code i}-th element.
	 * 
	 * @param index
	 *            the {@code i}-th element
	 * @return the upper bound of the {@code i}-th element
	 */
    public double getUpperBound(int index) {
        return bounds.getUpperBound(index);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <G extends Genotype> G newInstance() {
        try {
            Constructor<? extends DoubleGenotype> cstr = this.getClass().getConstructor(Bounds.class);
            return (G) cstr.newInstance(bounds);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Initialize this genotype with {@code n} random values.
	 * 
	 * @param random
	 *            the random number generator
	 * @param n
	 *            the number of elements in the resulting genotype
	 */
    public void init(Random random, int n) {
        for (int i = 0; i < n; i++) {
            double lo = getLowerBound(i);
            double hi = getUpperBound(i);
            double value = lo + random.nextDouble() * (hi - lo);
            if (i >= size()) {
                add(value);
            } else {
                set(i, value);
            }
        }
    }
}
