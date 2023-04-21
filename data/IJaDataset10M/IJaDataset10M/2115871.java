package org.ceno.protocol.event;

import java.io.Serializable;
import org.ceno.model.Developer;
import org.ceno.model.Resource;
import org.ceno.protocol.ITrackerProtocol;

/**
 * @author Andre Albert &lt;andre.albert82@googlemail.com&gt;
 * @created 15.07.2009
 * @version 0.0.1
 * 
 */
public class DeactivateResourceEvent extends ResourceEvent implements ITrackerProtocol, Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * @param resource
	 * @param sender
	 */
    public DeactivateResourceEvent(Resource resource, Developer sender) {
        super(resource, sender);
    }

    /**
	 * {@inheritDoc}
	 */
    public short getSignalId() {
        return DEACTIVATE_SIGNAL;
    }
}
