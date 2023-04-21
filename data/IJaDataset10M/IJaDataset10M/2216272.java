package tefkat.engine.events;

public class EventTuple {

    public static EventBinding f(Class<?> type, Object ref) {
        return new EventBinding(type, ref);
    }
}
