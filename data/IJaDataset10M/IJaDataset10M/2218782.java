package org.sepp.messages.routing.dsr;

import iaik.utils.Util;
import java.util.List;
import org.sepp.messages.MessageInterface;
import org.sepp.messages.common.Message;
import org.sepp.routing.Route;
import org.sepp.routing.dsr.DSRRoute;
import org.sepp.utils.XMLTags;
import org.sepp.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class DSRRouteReply implements MessageInterface {

    public static final int type = 2002;

    private String source;

    private String destination;

    private long requestId;

    private List hops;

    public DSRRouteReply(Message message) {
        parseMessage(message.getData());
    }

    public DSRRouteReply(byte[] message) {
        parseMessage(new String(message));
    }

    public DSRRouteReply(long requestId, String source, String destination, List hops) {
        this.source = source;
        this.destination = destination;
        this.hops = hops;
        this.requestId = requestId;
    }

    public DSRRouteReply(DSRRouteRequest request) {
        this.source = request.getDestination();
        this.destination = request.getSource();
        this.hops = request.getHops();
        this.requestId = request.getRequestId();
    }

    public int getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List getHops() {
        return hops;
    }

    public void setHops(List hops) {
        this.hops = hops;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public Route getRoute() {
        return new DSRRoute(source, hops);
    }

    @Override
    public String toString() {
        StringBuffer message = new StringBuffer();
        message.append("<" + XMLTags.ROUTE_REPLY + ">\n");
        message.append("\t<" + XMLTags.SOURCE + ">" + source + "</" + XMLTags.SOURCE + ">\n");
        message.append("\t<" + XMLTags.DESTINATION + ">" + destination + "</" + XMLTags.DESTINATION + ">\n");
        message.append("\t<" + XMLTags.REQUEST_ID + ">" + requestId + "</" + XMLTags.REQUEST_ID + ">\n");
        for (int index = 0; index < hops.size(); index++) {
            message.append("\t<" + XMLTags.HOP + ">" + hops.get(index) + "</" + XMLTags.HOP + ">\n");
        }
        message.append("</" + XMLTags.ROUTE_REPLY + ">");
        return message.toString();
    }

    public byte[] getBytes() {
        return Util.toASCIIBytes(toString());
    }

    public void parseMessage(String message) {
        Document messageDocument = XMLUtils.xmlUtils.getDocument(message);
        source = XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.ROUTE_REPLY + "/" + XMLTags.SOURCE, messageDocument);
        destination = XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.ROUTE_REPLY + "/" + XMLTags.DESTINATION, messageDocument);
        requestId = XMLUtils.xmlUtils.getLongFromXPath("/" + XMLTags.ROUTE_REPLY + "/" + XMLTags.REQUEST_ID, messageDocument);
        NodeList nodes = XMLUtils.xmlUtils.getNodeListFromXPath("/" + XMLTags.ROUTE_REPLY + "/" + XMLTags.HOP, messageDocument);
        hops = XMLUtils.xmlUtils.getValues(nodes);
    }
}
