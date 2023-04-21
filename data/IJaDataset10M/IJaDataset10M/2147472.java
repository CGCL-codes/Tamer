package org.openkonnect.applications.openbravo.ws;

import java.io.Serializable;

public class EventResponseMessage implements Serializable {

    /**
   * 
   */
    private static final long serialVersionUID = -2390790983281050260L;

    private EventType event;

    public EventResponseMessage() {
    }

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public java.lang.String toString() {
        StringBuffer out = new StringBuffer("EventResponseMessage(");
        out.append("event=").append(event);
        out.append(")");
        return out.toString();
    }
}
