package net.sf.metarbe;

public class EventClassMatch implements RuleConstraint {

    private Class<? extends RuleSessionEvent> eventClass;

    public EventClassMatch(Class<? extends RuleSessionEvent> clazz) {
        eventClass = clazz;
    }

    public boolean match(Object aValue) {
        return eventClass.isAssignableFrom(aValue.getClass());
    }
}
