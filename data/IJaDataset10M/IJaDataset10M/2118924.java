package net.jxta.impl.resolver.resolverMeter;

import net.jxta.peer.*;
import net.jxta.endpoint.*;
import net.jxta.util.documentSerializable.*;
import net.jxta.document.*;
import net.jxta.util.*;
import net.jxta.impl.meter.*;
import java.util.*;

/**
 * Metrics for a specific Query Destination for a specific handler 
 **/
public class QueryDestinationMetric implements DocumentSerializable {

    private PeerID peerID;

    private int errorWhileProcessingQuery;

    private int queryProcessed;

    private int errorWhileProcessingResponse;

    private int responseProcessed;

    private int responseSentViaUnicast;

    private int querySentViaUnicast;

    private int queryToUnregisteredHandler;

    private int responseToUnregisteredHandler;

    public QueryDestinationMetric(PeerID pid) {
        this.peerID = pid;
    }

    public QueryDestinationMetric(QueryDestinationMetric prototype) {
        this.peerID = prototype.peerID;
    }

    public QueryDestinationMetric() {
    }

    public PeerID getPeerID() {
        return peerID;
    }

    void querySentViaUnicast() {
        querySentViaUnicast++;
    }

    /** Get Queries Sent via Unicast to this destinations **/
    public int getQueriesSentViaUnicast() {
        return querySentViaUnicast;
    }

    void responseSentViaUnicast() {
        responseSentViaUnicast++;
    }

    /** Get Responses Sent via Unicast to this destinations **/
    public int getResponsesSentViaUnicast() {
        return responseSentViaUnicast;
    }

    void responseToUnregisteredHandler() {
        responseToUnregisteredHandler++;
    }

    /** Get Responses Recieved to this handler when not registered **/
    public int getResponseToUnregisteredHandler() {
        return responseToUnregisteredHandler;
    }

    void responseProcessed() {
        responseProcessed++;
    }

    /** Get Responses received and processed locally **/
    public int getResponsesProcessed() {
        return responseProcessed;
    }

    void errorWhileProcessingResponse() {
        errorWhileProcessingResponse++;
    }

    /** Get Responses received but failing when processed locally **/
    public int getErrorsWhileProcessingResponse() {
        return errorWhileProcessingResponse;
    }

    void queryProcessed() {
        queryProcessed++;
    }

    /** Get Queries received and processed locally **/
    public int getQueriesProcessed() {
        return queryProcessed;
    }

    void queryToUnregisteredHandler() {
        queryToUnregisteredHandler++;
    }

    /** Get Queries Recieved to this handler when not registered **/
    public int getQueryToUnregisteredHandler() {
        return queryToUnregisteredHandler;
    }

    void errorWhileProcessingQuery() {
        errorWhileProcessingQuery++;
    }

    /** Get Queries received but failing when processed locally **/
    public int getErrorsWhileProcessingQuery() {
        return errorWhileProcessingQuery;
    }

    public void serializeTo(Element element) throws DocumentSerializationException {
        if (peerID != null) {
            DocumentSerializableUtilities.addString(element, "peerID", peerID.toString());
        }
        if (errorWhileProcessingQuery != 0) {
            DocumentSerializableUtilities.addInt(element, "errorWhileProcessingQuery", errorWhileProcessingQuery);
        }
        if (queryProcessed != 0) {
            DocumentSerializableUtilities.addInt(element, "queryProcessed", queryProcessed);
        }
        if (errorWhileProcessingResponse != 0) {
            DocumentSerializableUtilities.addInt(element, "errorWhileProcessingResponse", errorWhileProcessingResponse);
        }
        if (responseProcessed != 0) {
            DocumentSerializableUtilities.addInt(element, "responseProcessed", responseProcessed);
        }
        if (responseSentViaUnicast != 0) {
            DocumentSerializableUtilities.addInt(element, "responseSentViaUnicast", responseSentViaUnicast);
        }
        if (querySentViaUnicast != 0) {
            DocumentSerializableUtilities.addInt(element, "querySentViaUnicast", querySentViaUnicast);
        }
        if (queryToUnregisteredHandler != 0) {
            DocumentSerializableUtilities.addInt(element, "queryToUnregisteredHandler", queryToUnregisteredHandler);
        }
        if (responseToUnregisteredHandler != 0) {
            DocumentSerializableUtilities.addInt(element, "responseToUnregisteredHandler", responseToUnregisteredHandler);
        }
    }

    public void initializeFrom(Element element) throws DocumentSerializationException {
        for (Enumeration e = element.getChildren(); e.hasMoreElements(); ) {
            Element childElement = (TextElement) e.nextElement();
            String tagName = (String) childElement.getKey();
            if (tagName.equals("peerID")) {
                String peerIDText = DocumentSerializableUtilities.getString(childElement);
                peerID = MetricUtilities.getPeerIdFromString(peerIDText);
            } else if (tagName.equals("errorWhileProcessingQuery")) {
                errorWhileProcessingQuery = DocumentSerializableUtilities.getInt(childElement);
            } else if (tagName.equals("queryProcessed")) {
                queryProcessed = DocumentSerializableUtilities.getInt(childElement);
            } else if (tagName.equals("errorWhileProcessingResponse")) {
                errorWhileProcessingResponse = DocumentSerializableUtilities.getInt(childElement);
            } else if (tagName.equals("responseProcessed")) {
                responseProcessed = DocumentSerializableUtilities.getInt(childElement);
            } else if (tagName.equals("responseSentViaUnicast")) {
                responseSentViaUnicast = DocumentSerializableUtilities.getInt(childElement);
            } else if (tagName.equals("querySentViaUnicast")) {
                querySentViaUnicast = DocumentSerializableUtilities.getInt(childElement);
            } else if (tagName.equals("queryToUnregisteredHandler")) {
                queryToUnregisteredHandler = DocumentSerializableUtilities.getInt(childElement);
            } else if (tagName.equals("responseToUnregisteredHandler")) {
                responseToUnregisteredHandler = DocumentSerializableUtilities.getInt(childElement);
            }
        }
    }

    public void mergeMetrics(QueryDestinationMetric otherQueryDestinationMetric) {
        this.errorWhileProcessingQuery += otherQueryDestinationMetric.errorWhileProcessingQuery;
        this.queryProcessed += otherQueryDestinationMetric.queryProcessed;
        this.errorWhileProcessingResponse += otherQueryDestinationMetric.errorWhileProcessingResponse;
        this.responseProcessed += otherQueryDestinationMetric.responseProcessed;
        this.responseSentViaUnicast += otherQueryDestinationMetric.responseSentViaUnicast;
        this.querySentViaUnicast += otherQueryDestinationMetric.querySentViaUnicast;
        this.queryToUnregisteredHandler += otherQueryDestinationMetric.queryToUnregisteredHandler;
        this.responseToUnregisteredHandler += otherQueryDestinationMetric.responseToUnregisteredHandler;
    }

    @Override
    public int hashCode() {
        return peerID.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof QueryDestinationMetric) {
            QueryDestinationMetric otherQueryDestinationMetric = (QueryDestinationMetric) other;
            return peerID.equals(otherQueryDestinationMetric.peerID);
        } else {
            return false;
        }
    }
}
