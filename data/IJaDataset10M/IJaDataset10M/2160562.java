package org.mobicents.media.server.mgcp.controller.signal;

import java.util.ArrayList;
import java.util.Arrays;
import org.mobicents.media.server.utils.Text;

/**
 * Event thrown by endpoint or connection.
 * 
 * Event has one more associated handlers or action. When <code>match(Text)</code> 
 * is called the requested action is selected for execution when this event will 
 * be detected.
 * 
 * @author kulikov
 */
public class Event {

    private Text name;

    private Text evtName = new Text();

    private Text actionName = new Text();

    private Text[] descriptor = new Text[] { evtName, actionName };

    private boolean isActive;

    private ArrayList<EventAction> actions = new ArrayList();

    private EventAction requestedAction;

    /**
     * Constructs new event name.
     * 
     * @param eventName 
     */
    public Event(Text eventName) {
        this.name = eventName;
    }

    /**
     * Gets the name of this event.
     * 
     * @return name of this event.
     */
    public Text getName() {
        return name;
    }

    /**
     * Adds handlers for this event.
     * 
     * @param action the event handler.
     */
    public void add(EventAction action) {
        this.actions.add(action);
    }

    /**
     * Checks that this event objects matches to the specified event descriptor.
     * 
     * @param event the event descriptor which includes the event name and 
     * requested action.
     * 
     * @return true if event name specified in descriptor matches to this 
     * name of this event object.
     */
    public boolean matches(Text eventDescriptor) {
        this.requestedAction = null;
        eventDescriptor.divide(new char[] { '(', ')' }, descriptor);
        evtName.trim();
        actionName.trim();
        this.isActive = evtName.equals(this.name);
        if (!this.isActive) {
            return false;
        }
        for (EventAction a : actions) {
            if (a.getName().equals(actionName)) {
                this.requestedAction = a;
                break;
            }
        }
        this.isActive = this.requestedAction != null;
        return this.isActive;
    }

    /**
     * Is this event requested or not.
     * 
     * @return true if this event requested for detection or false otherwise.
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * Executes action associated with this event
     */
    public void fire(Signal s, Text options) {
        if (this.isActive) {
            this.requestedAction.perform(s, this, options);
        }
    }

    public void reset() {
        this.isActive = false;
        this.requestedAction = null;
    }
}
