package edu.vub.at.actors.eventloops;

/**
 * A Future Event has an associated Future object to which the event
 * will pass the return value or exception of a certain computation to be
 * specified by a subclass.
 *
 * @author tvcutsem
 */
public abstract class FutureEvent extends Event {

    private final Future future_;

    /**
	 * Verbode constructor which takes a future to be resolved by this event as well as a 
	 * descriptive string describing this event for debugging purposes. 
	 * @param description a description of the event for debugging purposes.
	 * @param reply the future which will be resolved when this event has been executed.
	 */
    public FutureEvent(String description, Future reply) {
        super(description);
        future_ = reply;
    }

    /**
	 * Default constructor which takes a future to be resolved by this event. 
	 * @param reply the future which will be resolved when this event has been executed.
	 */
    public FutureEvent(Future reply) {
        future_ = reply;
    }

    public void process(Object owner) {
        try {
            Object result = this.execute(owner);
            future_.resolve(result);
        } catch (Exception e) {
            future_.ruin(e);
        }
    }

    /**
	 * Template method to be overwritten by concrete instantiations of this class. This method
	 * is called by the {@link FutureEvent#process(Object)} method and its outcome is used to
	 * resolve or ruin the Future attached to this event.
	 */
    public abstract Object execute(Object owner) throws Exception;
}
