package ru.satseqsys.parser;

import java.util.List;
import ru.satseqsys.model.Event;

public class EventParser {

    List<IParser<Event>> parsers;

    public void setParsers(List<IParser<Event>> parsers) {
        this.parsers = parsers;
    }

    public Event parse(String message) throws Exception {
        for (IParser<Event> parser : parsers) {
            if (parser.matches(message)) {
                return parser.parse();
            }
        }
        return null;
    }
}
