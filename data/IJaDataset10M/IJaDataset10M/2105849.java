package de.suse.swamp.core.workflow;

/**
  * The Eventtemplate.
  *
  * @author Sonja Krause-Harder &lt;skh@suse.de&gt;
  * @version $Id$
  *
  */
public class EventTemplate {

    public EventTemplate(String type) {
        this.type = type;
    }

    public Event getEvent() {
        return new Event(type, 0, 0);
    }

    private String type;
}
