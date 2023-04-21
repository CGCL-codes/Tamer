package net.sourceforge.strategema.games;

import net.sourceforge.strategema.games.timers.ComputationTimer;

/**
 * A lightweight interface for informing an object that an event has occurred.
 * 
 * @see ComputationTimer WallTimer
 * @author Lizzy
 */
public interface Notifiable {

    /**
	 * Notifies this Notifiable that an event has occurred.
	 * @param event The event ID number.
	 */
    public void notifyEvent(int event);
}
