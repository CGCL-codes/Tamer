package org.mars_sim.msp.core;

public interface UnitManagerListener {

    /**
	 * Catch unit manager update event.
	 * @param event the unit event.
	 */
    public void unitManagerUpdate(UnitManagerEvent event);
}
