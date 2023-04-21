package ch.unizh.ini.jaer.projects.spatiatemporaltracking.tracker.event;

import ch.unizh.ini.jaer.projects.spatiatemporaltracking.tracker.event.EventAssignable;
import net.sf.jaer.event.TypedEvent;

/**
 *
 * @author matthias
 * 
 * The interface provides methods to compute the cost to assign a TypedEvent
 * to a EventAssignment.
 */
public interface EventCostFunction {

    public double cost(EventAssignable assignable, TypedEvent e);

    /**
     * Gets true, if all required data to compute the cost function are
     * available, false otherwise.
     * 
     * @param assignment The EventAssignable the TypedEvent has to be assigned.
     * @return True, if all required data are available, false otherwise.
     */
    public boolean isComputable(EventAssignable assignable);
}
