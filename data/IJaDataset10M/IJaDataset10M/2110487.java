package repast.simphony.query.space.projection;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.projection.Projection;
import repast.simphony.space.projection.ProjectionPredicate;

/**
 * Abstract implementation of Predicate interface. All the implemented
 * methods return false. Particular predicate implementations can
 * override one or more of these methods as appropriate. For example,
 * "within" only applies to networks, so the Within predicate would
 * override evaluate(Network).
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class SpatialPredicate implements ProjectionPredicate {

    public boolean evaluate(Projection projection) {
        return false;
    }

    /**
	 * Evaluates the Network against this predicate.
	 *
	 * @param network the projection to evaluate against.
	 * @return false
	 */
    public boolean evaluate(Network network) {
        return false;
    }

    /**
	 * Evaluates the Grid against this predicate.
	 *
	 * @param grid the Grid to evaluate against.
	 * @return false
	 */
    public boolean evaluate(Grid grid) {
        return false;
    }

    /**
	 * Evaluates the ContinuousSpace against this predicate.
	 *
	 * @param space the continuous space to evaluate against.
	 * @return false
	 */
    public boolean evaluate(ContinuousSpace space) {
        return false;
    }

    /**
	 * Evaluates the Geography against this predicate.
	 *
	 * @param geography the geography to evaluate against.
	 * @return true if this predicate is true for the specified projection otherwise false.
	 */
    public boolean evaluate(Geography geography) {
        return false;
    }
}
