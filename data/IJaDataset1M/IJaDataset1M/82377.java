package org.davic.net.ca;

/**
 * This event is sent for a specific session, at the moment that the state of
 * the module (as far as relevant to that session) has changed. The module state
 * may change due to events out of the scope of the session, e.g. when the
 * module is accessed by other parts of the STB software.
 * <p>
 * Any messages sent by the application to the CA module in this session to
 * which the CA module has not responded yet, may or may not have been delivered
 * to the module and will not be responded to any more due to the state change.
 */
public class ModuleStateChangedEvent extends MessageEvent {

    /**
	 * Constructor for the event
	 * @param SessionID the sessionID of the session concerned
	 * @param caModule the CAModule object representing the module that is the
	 *            source of the event
	 */
    public ModuleStateChangedEvent(int SessionID, Object caModule) {
    }

    /**
	 * Returns the CAModule that is the source of the event
	 * @return the CAModule object representing the CA module that is is the
	 *         source of the event
	 */
    public Object getSource() {
        return null;
    }
}
