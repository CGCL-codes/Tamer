package edu.ucsd.ncmir.jibber.events;

import edu.ucsd.ncmir.asynchronous_event.AsynchronousEvent;

/**
 *
 * @author spl
 */
public class CheckMogrifiableStatusEvent extends AsynchronousEvent {

    /** Creates a new instance of CheckMogrifiableStatusEvent */
    public CheckMogrifiableStatusEvent() {
    }

    private boolean is_mogrifiable;

    public void setMogrifiable(boolean is_mogrifiable) {
        this.is_mogrifiable = is_mogrifiable;
    }

    public boolean isMogrifiable() {
        return this.is_mogrifiable;
    }
}
