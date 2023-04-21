package org.ikasan.spec.flow;

/**
 * Interface for the <code>Flow Event</code>.
 * 
 * @author Ikasan Development Team
 *
 */
public interface FlowEvent<IDENTIFIER, PAYLOAD> {

    /**
	 * Get immutable flow event identifier.
	 * @return IDENTIFIER - event identifier
	 */
    public IDENTIFIER getIdentifier();

    /**
	 * Get the immutable created date/time of the flow event.
	 * @return long - create date time
	 */
    public long getTimestamp();

    /**
	 * Get the payload of this flow event.
	 * @return PAYLOAD payload
	 */
    public PAYLOAD getPayload();

    /**
	 * Set the payload of this flow event.
	 * @param PAYLOAD - payload
	 */
    public void setPayload(PAYLOAD payload);
}
