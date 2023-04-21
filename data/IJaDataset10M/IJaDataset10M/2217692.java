package tchoukstats.events;

import java.util.EventListener;

/**
 * Interface that describes which elements must have the classes that want to listen
 * time update events.
 */
public interface TimeUpdateEventListener extends EventListener {

    /**
	 * The time has been updated.
	 *
	 * @param e		The time update event.
	 */
    public void timeUpdated(TimeUpdateEvent e);

    /**
	 * A set has started.
	 *
	 * @param setNumber	The number of the new set.
	 */
    public void newSet(int setNumber);
}
