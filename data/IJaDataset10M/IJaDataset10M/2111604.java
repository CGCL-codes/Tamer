package org.bd.banglasms.control.event;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Event class can be used to contain information about any event happened.
 * Event contains a name and source, which generated the event. Event can also
 * carry any number of parameter-value pairs to pass information specific to
 * that event.
 */
public class Event {

    private String name;

    private Hashtable paramValuePairs;

    private Object source;

    /**
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 *             if <code>name</code> or <code>source</code> is null
	 */
    public Event(String name, Object source) {
        if (name == null) {
            throw new IllegalArgumentException("Event name cannot be null");
        }
        if (source == null) {
            throw new IllegalArgumentException("Event source cannot be null");
        }
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public Object getSource() {
        return source;
    }

    public Object getValue(String parameter) {
        if (paramValuePairs == null) {
            return null;
        }
        return paramValuePairs.get(parameter);
    }

    /**
	 * 
	 * @param parameter
	 * @param value
	 * @throws IllegalArgumentException
	 *             if <code>parameter</code> is null
	 */
    public void setValue(String parameter, Object value) {
        if (parameter == null) {
            throw new IllegalArgumentException("Parameter cannot be null");
        }
        if (paramValuePairs == null) {
            paramValuePairs = new Hashtable();
        }
        paramValuePairs.put(parameter, value);
    }

    public Enumeration getParameters() {
        if (paramValuePairs == null) {
            return new Vector(0).elements();
        } else {
            return paramValuePairs.keys();
        }
    }
}
