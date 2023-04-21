package de.suse.swamp.core.conditions;

import java.util.*;
import de.suse.swamp.core.workflow.*;

public class EventCondition extends Condition {

    private int workflowId;

    private Event event;

    private boolean state;

    public EventCondition(Event event, boolean state) {
        this.event = event;
        this.state = state;
        setModified(true);
    }

    public void setWorkflowId(int workflowId) {
        if (this.workflowId == 0) {
            this.workflowId = workflowId;
        }
    }

    public void handleEvent(Event e) {
        if (e.equals(event)) {
            setState(true);
        }
    }

    public boolean evaluate() {
        return state;
    }

    /**
     * Reset condition to original state.
     */
    public void reset() {
        setState(false);
    }

    public String toString() {
        return "[EventCondition: " + event.getType() + " " + state + "]";
    }

    public String getEventString() {
        String eventString = event.getType();
        if (state) eventString += " (true)";
        return eventString;
    }

    public String toXML() {
        return "<EventCondition type=\"" + event.toXML() + "\" state=\"" + state + "\" />";
    }

    public String getConditionType() {
        return "EVENT";
    }

    public List getChildConditions() {
        return new ArrayList();
    }

    private void setState(boolean newState) {
        if (this.state != newState) {
            this.state = newState;
            setModified(true);
        }
    }
}
