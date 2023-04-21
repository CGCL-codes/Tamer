package org.inigma.utopia.paper.events;

public class UnknownEvent extends AbstractEvent {

    private String unknownText;

    public UnknownEvent() {
    }

    public UnknownEvent(String text) {
        this.unknownText = text;
    }

    @Override
    public AbstractEvent getEvent(String eventText) {
        if (eventText.length() > 1) {
            return new UnknownEvent(eventText);
        }
        return null;
    }

    @Override
    public String toString() {
        return unknownText;
    }
}
