package org.jheuristics.ga.operators;

import org.jheuristics.Individual;
import org.jheuristics.ga.GAConfig;
import org.jheuristics.ga.GAStatus;
import org.jheuristics.ga.Population;

public interface Selector {

    /**
	 * TODO
	 *
	 * @param population
	 * @param howMany
	 * @param status
	 * @param config
	 * @return
	 */
    public Individual[] select(Population population, int howMany, GAStatus status, GAConfig config);
}
