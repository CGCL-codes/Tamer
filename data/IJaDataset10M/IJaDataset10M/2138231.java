package org.nomadpim.core.test;

import org.easymock.IArgumentMatcher;
import org.nomadpim.core.util.event.PropertyChangeEvent;

public class PropertyChangeEventMatcher implements IArgumentMatcher {

    private PropertyChangeEvent expectedEvent;

    public PropertyChangeEventMatcher(PropertyChangeEvent expectedEvent) {
        this.expectedEvent = expectedEvent;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("eqPropertyChangeEvent(");
        buffer.append(expectedEvent.getClass().getName());
        buffer.append("\"");
    }

    private boolean isDifferentOldState(PropertyChangeEvent actualEvent) {
        if (expectedEvent.getOldState() == actualEvent.getOldState()) {
            return false;
        }
        if (expectedEvent.getOldState() != null && expectedEvent.getOldState().equals(actualEvent.getOldState())) {
            return false;
        }
        return true;
    }

    private boolean isDifferentSource(PropertyChangeEvent actualEvent) {
        return !expectedEvent.getSource().equals(actualEvent.getSource());
    }

    public boolean matches(Object argument) {
        if (!(argument instanceof PropertyChangeEvent)) {
            return false;
        }
        PropertyChangeEvent actualEvent = (PropertyChangeEvent) argument;
        if (isDifferentSource(actualEvent)) {
            return false;
        }
        if (isDifferentOldState(actualEvent)) {
            return false;
        }
        if (!expectedEvent.getNewState().equals(actualEvent.getNewState())) {
            return false;
        }
        return true;
    }
}
