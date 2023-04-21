package net.sf.derquinsej.stats;

import static net.sf.derquinsej.math.RealFunctions.inverseErf;
import java.util.Locale;
import net.sf.derquinsej.math.PartialRealFunction;
import com.google.common.base.Preconditions;

/**
 * Long population quantile function based on normal distribution.
 * @author Andres Rodriguez
 */
final class NormalLongQuantile implements PartialRealFunction {

    private static final double SR2 = Math.sqrt(2);

    private final LongPopulation population;

    NormalLongQuantile(LongPopulation population) {
        this.population = Preconditions.checkNotNull(population, "A population must be provided");
    }

    public double apply(double input) {
        if (input == 1.0) {
            return population.getMax();
        }
        return population.getMean() + population.getSigma() * SR2 * inverseErf(2 * input - 1);
    }

    public boolean isDefinedAt(double input) {
        return input >= 0 && input <= 1;
    }

    @Override
    public String toString() {
        return String.format((Locale) null, "qtl[N(%f,%f)]:{%f,%f,%f,%f,%f,%f,%f,%f}", population.getMean(), population.getSigma(), apply(0.1), apply(0.2), apply(0.3), apply(0.4), apply(0.5), apply(0.6), apply(0.7), apply(0.8), apply(0.9));
    }
}
