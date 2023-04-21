package org.jquantlib.pricingengines;

import org.jquantlib.math.randomnumbers.RandomNumberGenerator;
import org.jquantlib.math.statistics.Statistics;
import org.jquantlib.methods.montecarlo.Variate;

/**
 *
 * Base class for Monte Carlo engines
 * <p>
 * Eventually this class might offer greeks methods. Deriving a class from McSimulation gives an easy way to write a Monte Carlo
 * engine.
 *
 * @ee McVanillaEngine
 *
 * @author Richard Gomes
 */
public class MCSimulation<MC extends Variate, RNG extends RandomNumberGenerator, S extends Statistics> {

    public MCSimulation() {
        throw new UnsupportedOperationException();
    }
}
