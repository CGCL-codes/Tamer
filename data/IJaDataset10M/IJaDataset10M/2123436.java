package org.commsuite.notification;

import java.io.Serializable;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.commsuite.model.Message;

/**
 * Notification object sent to SAP servers - it notifies sap server about state of sent message.
 * 
 * @since 1.0
 * @author Rafał Malinowski
 * @author Marcin Zduniak
 */
public class Notification implements Serializable {

    private static final long serialVersionUID = -1175279832018071091L;

    public enum State {

        SENT, DELIVERED, FAILED
    }

    ;

    private Message message;

    private State state;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        final Notification n = (Notification) o;
        if (this == n) {
            return true;
        }
        return state == n.getState() && message.getId().equals(n.getMessage().getId());
    }

    @Override
    public int hashCode() {
        final int stateHashCode = (null == state) ? 1234567 : state.hashCode();
        final int messageHashCode = (null == message) ? -1234567 : message.hashCode();
        return new HashCodeBuilder(13, 71).append(stateHashCode).append(messageHashCode).toHashCode();
    }
}
