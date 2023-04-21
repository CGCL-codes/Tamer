package be.vanvlerken.bert.packetdistributor.common;

import java.io.IOException;

/**
 * Exception thrown by a ITrafficDestination implementer to notify the caller of any failure
 * of delivery of the datachunk
 */
public class DeliveryException extends IOException {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3977304338739640629L;

    public DeliveryException(String message) {
        super(message);
    }
}
