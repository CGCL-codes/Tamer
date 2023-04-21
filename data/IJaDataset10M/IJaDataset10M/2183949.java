package org.velma.event;

import java.util.EventListener;

/**
 * The user can set filters that limit which sequences or which alignment
 * positions are displayed. Any display that needs to update based on these
 * filters can register as a FilterListener to be notified of all filter
 * updates.
 * 
 * @author Andy Walsh
 */
public interface FilterListener extends EventListener {

    /**
	 * The function that controls what happens when a change of filter occurs.
	 * 
	 * @param e
	 *            The {@link FilterEvent} associated with the change of filter.
	 */
    public abstract void filterChanged(FilterEvent e);
}
