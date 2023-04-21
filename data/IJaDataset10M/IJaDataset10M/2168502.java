package exitdistributions;

import ds.ca.evac.Individual;
import ds.ca.evac.TargetCell;
import java.util.HashMap;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class ZToExitMapping extends IndividualToExitMapping {

    /**
	 * This mapping is the main part of the {@code GraphBasedIndividualToExitMapping}.
	 * It maps individuals to {@link ds.ca.TargetCell} objects, i.e. it maps
	 * an individual to the cell it should go to. The target cell represents 
	 * the exit the individual shall go to.
	 */
    private HashMap<Individual, TargetCell> individualToExitMapping;

    public ZToExitMapping(HashMap<Individual, TargetCell> individualToExitMapping) {
        this.individualToExitMapping = individualToExitMapping;
    }

    /**
	 * Returns the corresponding target cell for each individual.
	 * The target cell represents the static potential it belongs to.
	 * @param individual An individual that belongs to the cellular automaton that was set in the constructor.
	 * @return The corresponding target cell for this individual, representing the static potential leading to this target cell 
	 * (and those target cells belonging to the same exit).
	 */
    public TargetCell getExit(Individual individual) {
        return individualToExitMapping.get(individual);
    }
}
