package jade.domain.introspection;

import jade.util.leap.List;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.core.AID;
import jade.content.AgentAction;

/**

  This class represents the <code>stop-notify</code> action,
  requesting to end a continuous notification of some events via ACL
  messages.

  @author Giovanni Rimassa - Universita' di Parma
  @version $Date: 2005-02-16 18:18:28 +0100 (mer, 16 feb 2005) $ $Revision: 5552 $

*/
public class StopNotify implements AgentAction {

    private AID observed;

    private List events = new ArrayList();

    /**
       Default constructor. A default constructor is necessary for
       ontological classes.
    */
    public StopNotify() {
    }

    /**
       Set the <code>observed</code> slot of this action.
       @param id The agent identifier of the agent whose events were
       being notified so far.
    */
    public void setObserved(AID id) {
        observed = id;
    }

    /**
       Retrieve the value of the <code>observed</code> slot of this
       event, containing the agent identifier of the agent whose
       events were being notified so far.
       @return The value of the <code>observed</code> slot, or
       <code>null</code> if no value was set.
    */
    public AID getObserved() {
        return observed;
    }

    /**
       Add an event name to the <code>events</code> slot collection of
       this object.
       @param evName The event name to add to the collection.
    */
    public void addEvents(String evName) {
        events.add(evName);
    }

    /**
       Remove all event names from the <code>events</code> slot
       collection of this object.
    */
    public Iterator getAllEvents() {
        return events.iterator();
    }
}
