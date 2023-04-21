package gumbo.core.update;

import gumbo.core.util.EventPhase;

/**
 * An UpdateBean that can be exposed as a delegate of a host UpdateBean. The
 * exposed beans expose the state of the host, in part or whole.
 * @author jonb
 */
public interface ExposableBean extends UpdateBean {

    /**
	 * Factory method for the update event sourced by this exposed bean for a
	 * given update phase. The event property ID should match that exposed by
	 * this exposed bean. Called by the system and bean exposers. Should not be
	 * called by the client.
	 * @param phase Update phase when the listener is notified. Never null.
	 * @return Shared exposed event. Never null.
	 */
    public UpdateEvent getExposedUpdateEvent(EventPhase.Describe phase);
}
