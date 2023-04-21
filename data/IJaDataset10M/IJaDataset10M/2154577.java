package org.vesuf.model.uml.behavior.statemachines;

import org.vesuf.model.uml.foundation.core.*;
import java.util.*;

/**
 *  An event is a specification of a type of observable occurrence.
 *  The occurrence that generates an event instance is assumed to 
 *  take place at an instant in time with no duration.
 */
public class Event extends ModelElement implements IEvent {

    /** The parameters. */
    protected Vector parameters;

    /**
	 *  Create a new Event.
	 */
    public Event(String name, Namespace namespace) {
        super(name, namespace);
        this.parameters = new Vector();
    }

    /**
	 *  Get the parameters.
	 *  @return The parameters.
	 */
    public IParameter[] getParameters() {
        IParameter[] ret = new IParameter[parameters.size()];
        parameters.copyInto(ret);
        return ret;
    }
}
