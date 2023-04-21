package backend.event;

import backend.event.type.EventType;

/**
 * Implements an ONDEX mapping event.
 * 
 * @author hindlem
 *
 */
public class TransformerEvent extends ONDEXEvent {

    /**
	 * Default serialisation id
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Constructor for a given Object and corresponding event type.
	 * 
	 * @param o - source object
	 * @param e - event type
	 */
    public TransformerEvent(Object o, EventType e) {
        super(o, e);
    }
}
