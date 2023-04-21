package dataflow.core;

public class EventParserEndEvent implements IEvent {

    private final EventParser parser;

    public EventParserEndEvent(EventParser parser) {
        this.parser = parser;
    }

    @Override
    public void execute() {
        parser.stop();
    }
}
