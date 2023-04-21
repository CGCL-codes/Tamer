package org.ikasan.framework.component.endpoint;

import javax.resource.ResourceException;
import org.ikasan.common.Payload;
import org.ikasan.framework.component.Event;
import org.ikasan.framework.payload.service.PayloadPublisher;

/**
 * <code>Endpoint</code> implementation that delegates in order all the <code>Event</code>'s <code>Payload</code>'s to a
 * <code>PayloadPublisher</code>
 * 
 * @author Ikasan Development Team
 */
public class PayloadPublisherEndpoint implements Endpoint {

    /** The payload publisher */
    protected PayloadPublisher payloadPublisher;

    /**
     * Constructor
     * 
     * @param payloadPublisher The payload publisher
     */
    public PayloadPublisherEndpoint(PayloadPublisher payloadPublisher) {
        super();
        this.payloadPublisher = payloadPublisher;
    }

    public void onEvent(Event event) throws EndpointException {
        for (Payload payload : event.getPayloads()) {
            try {
                payloadPublisher.publish(payload);
            } catch (ResourceException e) {
                throw new EndpointException(e);
            }
        }
    }
}
