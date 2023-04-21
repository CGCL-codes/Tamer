package net.sourceforge.ondex.export;

import net.sourceforge.ondex.event.ONDEXEvent;
import net.sourceforge.ondex.event.type.EventType;

/**
 * Implements an ONDEX export event.
 * 
 * @author taubertj
 * 
 */
public class ExportEvent extends ONDEXEvent {

    /**
	 * Default serialisation id
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Constructor for a given Object and corresponding event type.
	 * 
	 * @param o
	 *            Object
	 * @param e
	 *            EventType
	 */
    public ExportEvent(Object o, EventType e) {
        super(o, e);
    }
}
