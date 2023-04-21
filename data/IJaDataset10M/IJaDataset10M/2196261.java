package fr.itris.glips.rtda.animaction.eventsmanager;

import fr.itris.glips.rtda.animaction.*;

/**
 * the abstract class of all the events managers
 * @author Jordi SUC
 */
public abstract class EventsManager {

    /**
	 * the action this events manager monitors
	 */
    protected Action action;

    /**
     * the method called when the data to which the action is registered, is modified.
     * @param evt an event
     */
    public void dataChanged(DataEvent evt) {
    }
}
