package it.unipr.ce.dsg.deus.core;

/**
 * This interface is used to implement a listening method that event can use to
 * be notified when one of their referenced event is scheduled for the
 * execution.
 * 
 * @author Matteo Agosti
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public interface SchedulerListener {

    /**
	 * This method is invoked when a referenced event is scheduled.
	 * 
	 * @param parentEvent
	 *            the event to whom the current referenced event is associated.
	 * @param newEvent
	 *            the scheduled referenced event.
	 */
    public void newEventScheduled(Event parentEvent, Event newEvent);
}
