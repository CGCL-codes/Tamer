package org.granite.messaging.service.security;

import org.granite.config.flex.Destination;
import flex.messaging.messages.Message;

/**
 * @author Franck WOLFF
 */
public abstract class AbstractSecurityContext {

    private final Message message;

    private final Destination destination;

    public AbstractSecurityContext(Message message, Destination destination) {
        this.message = message;
        this.destination = destination;
    }

    public Message getMessage() {
        return message;
    }

    public Destination getDestination() {
        return destination;
    }

    public abstract Object invoke() throws Exception;
}
