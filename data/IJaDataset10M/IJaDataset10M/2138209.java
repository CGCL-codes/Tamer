package com.impact.xbm.events;

import com.impact.xbm.exceptions.XbmException;
import com.impact.xbm.server.LoggerControl;
import com.impact.xbm.utils.Xml;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Base class for all XBM events
 */
public abstract class XbmEvent implements Serializable {

    EventParticipant source;

    String sourceName;

    String eventId;

    /**
     *
     */
    String requestId;

    /**
     *
     */
    Long simulationTime = null;

    /**
     * Sequence number set by source of the event
     */
    long sourceSequenceNumber;

    /**
     * Sequence number of listener's events, set by the EventManager
     */
    long listenerSequenceNumber;

    Logger logger;

    /**
     * Constructor with simulationTime
     *
     * @param requestId ID of request being processed
     * @param source Source of the event
     * @param simulationTime Simulated time of event's occurence
     */
    public XbmEvent(String requestId, EventParticipant source, Long simulationTime) {
        this(requestId, source);
        this.simulationTime = simulationTime;
    }

    /**
     * Constructor without simulationTime
     *
     * @param requestId ID of request being processed
     * @param source Source of the event
     */
    public XbmEvent(String requestId, EventParticipant source) {
        this.requestId = requestId;
        this.source = source;
        setEventId();
        try {
            this.logger = LoggerControl.getLogger(requestId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Serialize an instance of this class.
     * @return A serialized event
     * @throws XbmException
     */
    public String serialize() throws XbmException {
        try {
            Document doc = Xml.getDocument();
            Element root = doc.createElement("XbmEvent");
            String eventType = this.getClass().getName();
            eventType = eventType.substring(eventType.lastIndexOf('.') + 1);
            root.setAttribute("EventType", eventType);
            root.setAttribute("RequestId", this.requestId);
            root.setAttribute("EventId", this.eventId);
            root.setAttribute("SourceSequenceNumber", String.valueOf(this.sourceSequenceNumber));
            root.setAttribute("ListenerSequenceNumber", String.valueOf(this.listenerSequenceNumber));
            root.setAttribute("SourceName", this.source.instanceName);
            doc.appendChild(root);
            return Xml.getStringFromDocument(doc);
        } catch (Exception ex) {
            this.logger.log(Level.SEVERE, "Exception in XbmEvent.serialize", ex);
            throw new XbmException(ex);
        }
    }

    /**
     * Desrialize provided serialized event to an XbmEvent
     * @param eventData The serialized event string
     * @return The deserialized event
     */
    public static XbmEvent deserialize(String eventData) {
        XbmEvent xbmEvent = null;
        try {
            Document doc = Xml.readXmlFromString(eventData);
            Element root = doc.getDocumentElement();
            String eventType = root.getAttribute("EventType");
            String requestId = root.getAttribute("RequestId");
            String eventId = root.getAttribute("EventId");
            String sourceName = root.getAttribute("SourceName");
            int sourceSequenceNumber = Integer.parseInt(root.getAttribute("SourceSequenceNumber"));
            int listenerSequenceNumber = Integer.parseInt(root.getAttribute("ListenerSequenceNumber"));
            if (eventType.equals("ProcessingCompletedEvent")) {
                String exceptionMessage = root.getAttribute("ExceptionMessage");
                if (!exceptionMessage.isEmpty()) {
                    XbmException ex = new XbmException(exceptionMessage);
                    xbmEvent = new ProcessingCompletedEvent(requestId, null, ex);
                } else {
                    HashMap<String, Double> output = new HashMap<String, Double>();
                    NodeList outputNodes = Xml.xpathEvaluateNodeList("//XbmEvent//OutputList/Output", doc);
                    int numOutputs = outputNodes.getLength();
                    for (int i = 0; i < numOutputs; i++) {
                        Element outputEl = (Element) outputNodes.item(i);
                        String name = outputEl.getAttribute("Name");
                        double value = Double.valueOf(outputEl.getAttribute("Value"));
                        output.put(name, value);
                    }
                    xbmEvent = new ProcessingCompletedEvent(requestId, null, output);
                }
            } else if (eventType.equals("StateCompletedEvent")) {
                xbmEvent = new StateCompletedEvent(requestId, null);
                System.out.println("XbmEvent.deserialize: made a StateCompletedEvent");
            } else if (eventType.equals("StateInitializingEvent")) {
                ArrayList<String> requestedOutputs = new ArrayList<String>();
                NodeList outputNodes = Xml.xpathEvaluateNodeList("//XbmEvent//RequestedOutputs/Output", doc);
                int numOutputs = outputNodes.getLength();
                for (int i = 0; i < numOutputs; i++) {
                    Element outputEl = (Element) outputNodes.item(i);
                    String name = outputEl.getAttribute("Name");
                    requestedOutputs.add(name);
                }
                xbmEvent = new StateInitializingEvent(requestId, null, requestedOutputs);
            } else if (eventType.equals("StateStartingEvent")) {
                xbmEvent = new StateStartingEvent(requestId, null);
            } else if (eventType.equals("StepStartingEvent")) {
                int stepNumber = 0;
                xbmEvent = new StepStartingEvent(requestId, null, stepNumber);
            } else if (eventType.equals("UpdateEvent")) {
                ArrayList<String> xmlData = new ArrayList<String>();
                NodeList dataNodes = Xml.xpathEvaluateNodeList("//XbmEvent//XmlData/Data", doc);
                int numOutputs = dataNodes.getLength();
                for (int i = 0; i < numOutputs; i++) {
                    Element dataEl = (Element) dataNodes.item(i);
                    String data = dataEl.getAttribute("Data");
                    xmlData.add(data);
                }
                xbmEvent = new UpdateEvent(requestId, null, xmlData);
            } else if (eventType.equals("UpdateProcessedEvent")) {
                String eventProcessedId = root.getAttribute("eventProcessedId");
                xbmEvent = new UpdateProcessedEvent(requestId, null, eventProcessedId);
                System.out.println("XbmEvent.deserialize: made a UpdateProcessedEvent");
            } else {
                System.out.println("\tUnrecognized event ... can't deserialize");
            }
            xbmEvent.setSourceSequenceNumber(sourceSequenceNumber);
            xbmEvent.setListenerSequenceNumber(listenerSequenceNumber);
            xbmEvent.setSourceName(sourceName);
            xbmEvent.setEventId(eventId);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return xbmEvent;
        }
    }

    /**
     * Returns source of the event
     * @return Source of the event
     */
    public EventParticipant getSource() {
        return this.source;
    }

    public void setSource(EventParticipant source) {
        this.source = source;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    /**
     * Get the event's requestId
     * @return The event's requestId
     */
    public String getRequestId() {
        return this.requestId;
    }

    /**
     * Get the event's simulation time
     * @return The event's simulation time (null, if not defined)
     */
    public Long getSimulationTime() {
        return this.simulationTime;
    }

    /**
     * Get the event's source sequence number
     * @return The event's source sequence number
     */
    public long getSourceSequenceNumber() {
        return this.sourceSequenceNumber;
    }

    /**
     * Set the event's source sequence number
     * @param n Source sequence number to set
     */
    public void setSourceSequenceNumber(long n) {
        this.sourceSequenceNumber = n;
    }

    /**
     * Get the event's listener sequence number
     * @return The event's listener sequence number
     */
    public long getListenerSequenceNumber() {
        return this.listenerSequenceNumber;
    }

    /**
     * Set the event's listener sequence number
     * @param n Listener sequence number to set
     */
    public void setListenerSequenceNumber(long n) {
        this.listenerSequenceNumber = n;
    }

    /**
     * Get the event's ID
     * @return The event's eventId
     */
    public String getEventId() {
        return this.eventId;
    }

    /**
     * Sets the event's eventId to a guid
     */
    private void setEventId() {
        this.eventId = UUID.randomUUID().toString();
    }

    /**
     * Sets the event's ID to specified value.
     * Used when deserializing an event, to recover the original eventId, rather
     * than using a new one.
     * @param eventId The event ID to use for this event
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Base applyFilters doesn't do any filtering
     * @param filters List of XbmEventFilters
     * @return A filtered event - but for this base class, doesn't filter;
     * derived classes can override to do filtering for particular type of events.
     */
    public XbmEvent applyFilters(ArrayList<XbmEventFilter> filters) {
        return this;
    }
}
