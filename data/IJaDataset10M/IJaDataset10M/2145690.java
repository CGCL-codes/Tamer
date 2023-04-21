package backend.parser;

import entity.Event;
import entity.Rsvp;

public interface EventParser {

    public void parseEvent(Event event, String text) throws EventParseException;

    public void parseRsvp(Rsvp rsvp, String text) throws EventParseException;
}
