package org.opt4j.optimizer.sa;

import org.opt4j.config.annotations.Info;
import org.opt4j.core.optimizer.MaxIterations;
import org.opt4j.core.optimizer.OptimizerModule;

/**
 * This module binds the {@link SimulatedAnnealing} optimizer.
 * 
 * @author lukasiewycz
 * 
 */
@Info("A probabilistic optimization heuristic that simulates the physical process of annealing." + "Adds the objective values in case of a multi-objective optimization.")
public class SimulatedAnnealingModule extends OptimizerModule {

    @Info("The number of iterations.")
    @MaxIterations
    protected int iterations = 100000;

    /**
	 * Returns the number of iterations.
	 * 
	 * @see #setIterations
	 * @return the number of iterations
	 */
    public int getIterations() {
        return iterations;
    }

    /**
	 * Sets the number of iterations.
	 * 
	 * @see #getIterations
	 * @param iterations
	 *            the number of iterations
	 */
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    @Override
    public void config() {
        bindOptimizer(SimulatedAnnealing.class);
    }
}
