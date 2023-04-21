package org.rakiura.micro;

/**
 * Represents an abstract Event.
 * 
 *<br><br>
 * Event.java<br>
 * Created: Wed Mar 28 14:27:04 2001<br>
 *
 * @author Mariusz Nowostawski   (mariusz@rakiura.org)
 * @version $Revision: 1.1 $ $Date: 2006/09/20 09:34:10 $
 */
public abstract class Event extends org.rakiura.util.EventObject {

    protected Agent source;

    /** 
   * Creates a new event with the given agent as a source. 
   *@param agent agent 
   */
    public Event(Agent agent) {
        super(agent);
    }

    /**
   * Returns the agent who originated that event.
   * @return this agent
   */
    public Agent getAgent() {
        return (Agent) getSource();
    }
}
