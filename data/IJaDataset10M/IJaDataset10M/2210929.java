package de.uniba.kinf.cityguide.controller.geomanager.algorithm;

import java.util.List;
import android.location.Location;
import de.uniba.kinf.cityguide.controller.IDataController;
import de.uniba.kinf.cityguide.entities.CGTour;
import de.uniba.kinf.cityguide.entities.CGPOI;

/**
 * <p>Interface with methods for TSP-solution (heuristic) via simulated annealing</p>
 * @author bhofmann
 *
 */
public interface ICGGeoSimAnnealing {

    /**
	 * <p>Main function for simulated annealing algorithm</p>
	 * @param start The current Location of the user
	 * @param pois The list of pois to compute a reasonable tour from
	 * @return The generated Tour
	 */
    public CGTour simAnnealing(Location start, List<CGPOI> pois);

    /**
	 * <p>Set start values for the simulated annealing algorithm</p>
	 * @param startValue The value to start the algorithm wiht
	 * @param minValue The minimum value, i.e the algorithm stopps here
	 * @param reductionFactor The factor the start value is reduced in each step
	 * @param d The connection to stored data (e.g. pois, entrypoints, neighbourhood of sections)
	 */
    public void setStartValues(double startValue, double minValue, double reductionFactor, IDataController d);

    /**
	 * <p>Tests whether the order or a tour is ok, i.e. if a path is possible looking
	 * at the neighbourhood and entrypoint information</p>
	 * @param tour The tour that should be examined
	 * @return Whether or not the tour is was accepted
	 */
    public boolean acceptTourOrder(CGTour tour);
}
