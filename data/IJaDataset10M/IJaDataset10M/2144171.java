package de.fzi.mappso.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import de.fzi.mappso.align.impl.AlignmentParticle;

/**
 * An observer which is notified if base matcher weights change.
 * Several {@link AlignmentParticle}s can be registered,
 * which are all notified in case the weights change.
 *  
 * @author bock
 *
 */
public class WeightsChangeObserver {

    private Set<AlignmentParticle> observables;

    private static WeightsChangeObserver instance = null;

    /**
     * This class can only be used as singleton, thus the constructor is private.
     */
    private WeightsChangeObserver() {
        observables = new HashSet<AlignmentParticle>();
    }

    /**
     * Gets the singleton instance for this class. 
     * @return The singleton instance of this class.
     */
    public static WeightsChangeObserver getInstance() {
        if (instance == null) instance = new WeightsChangeObserver();
        return instance;
    }

    /**
     * Registers a new observable {@link AlignmentParticle}
     * to be notified if weights change.
     * @param observable The evaluator to be registered.
     */
    public void registerObservable(AlignmentParticle observable) {
        observables.add(observable);
    }

    /**
     * Method to be called if weights change. This method notifies all registered
     * {@link AlignmentParticle}s about the weights change.
     * @param weights New weights mapped to the names of the base matchers they refer to.
     */
    public void weightsChanged(Map<String, Double> weights) {
        for (AlignmentParticle particle : observables) particle.changeBaseMatcherWeights(weights);
    }
}
