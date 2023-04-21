package pdmeditor.Process;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author s020597
 */
public class ProcessInstance {

    Integer processID;

    String instanceDescription = "Simulated Instance";

    HashMap events;

    public ProcessInstance(Integer inID) {
        processID = inID;
        events = new HashMap();
    }

    public void addEvent(Event inEvent) {
        events.put(inEvent.getCounter(), inEvent);
    }

    public Element writeToEventLog(Document doc) throws IOException {
        Element processInstance = doc.createElement("ProcessInstance");
        processInstance.setAttribute("description", "Simulated process instance");
        processInstance.setAttribute("id", processID + "");
        Iterator itEvents = events.values().iterator();
        while (itEvents.hasNext()) {
            processInstance.appendChild(((Event) itEvents.next()).writeToEventLog(doc));
        }
        return processInstance;
    }
}
