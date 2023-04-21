package neon.core.event;

import neon.util.fsm.Event;

public class MessageEvent implements Event {

    public String text;

    public MessageEvent(String text) {
        this.text = text;
    }

    public String getEventID() {
        return text;
    }
}
