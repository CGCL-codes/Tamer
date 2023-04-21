package org.sepp.messages.routing.ariadne;

import iaik.utils.Base64Exception;
import iaik.utils.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.sepp.messages.MessageInterface;
import org.sepp.messages.common.Message;
import org.sepp.routing.ariadne.AriadneRoute;
import org.sepp.utils.ByteArray;
import org.sepp.utils.XMLTags;
import org.sepp.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author Martin Krammer <martin.krammer@student.TUGraz.at>
 * @author Franz Roeck <franz.roeck@student.TUGraz.at>
 * @author Matthias Hilpold <matthias.hilpold@student.TUGraz.at>
 * @author Markus Lanthaler <markus.lanthaler@student.TUGraz.at>
 * @author Stefan Kraxberger <stefan.kraxberger@TUGraz.at>
 */
public class AriadneRouteRequest implements MessageInterface {

    public static final int type = 2790;

    protected long creationTime;

    private String source;

    private String destination;

    private List hops;

    protected long plannedTimeOfArrival;

    protected Vector macList;

    protected byte[] hashChain;

    public AriadneRouteRequest(Message message) {
        source = null;
        destination = null;
        plannedTimeOfArrival = 0;
        hops = new ArrayList();
        macList = new Vector();
        hashChain = null;
        parseMessage(message.getData());
    }

    public AriadneRouteRequest(String source, String destination) {
        this.source = source;
        this.destination = destination;
        creationTime = System.currentTimeMillis();
        hops = new ArrayList();
        macList = new Vector();
        hashChain = null;
    }

    public void setPlannedTimeOfArrival(long timeInterval) {
        this.plannedTimeOfArrival = timeInterval;
    }

    public long getPlannedTimeOfArrival() {
        return plannedTimeOfArrival;
    }

    public List getHops() {
        return hops;
    }

    public List getReversedHops() {
        List reversedHops = new ArrayList();
        for (int i = 0; i < hops.size(); i++) reversedHops.add((String) hops.get(i));
        return reversedHops;
    }

    public void setHops(List hops) {
        this.hops = hops;
    }

    public void addHop(String hop) {
        this.hops.add(hop);
    }

    public AriadneRoute getRoute() {
        return new AriadneRoute(destination, hops);
    }

    public int getType() {
        return type;
    }

    public void setMacs(Vector macs) {
        this.macList = macs;
    }

    public Vector getMacs() {
        return macList;
    }

    public void appendMac(byte[] mac) {
        this.macList.add(mac);
    }

    /**
	 * this method delivers bytes to initialize the hash chain field.
	 * 
	 * @return bytes to initialize hash chain field
	 */
    public byte[] getBytesForMAC() {
        ByteArray bytesForMAC = new ByteArray();
        bytesForMAC.appendBytes(String.valueOf(type).getBytes());
        bytesForMAC.appendBytes(source.getBytes());
        bytesForMAC.appendBytes(destination.getBytes());
        bytesForMAC.appendBytes(Long.valueOf(creationTime).byteValue());
        bytesForMAC.appendBytes(Long.valueOf(plannedTimeOfArrival).byteValue());
        bytesForMAC.appendBytes(hashChain);
        for (int i = 0; i < hops.size(); i++) {
            bytesForMAC.appendBytes(((String) hops.get(i)).getBytes());
        }
        for (int u = 0; u < macList.size(); u++) {
            bytesForMAC.appendBytes((byte[]) macList.get(u));
        }
        return bytesForMAC.getBytes();
    }

    public void setHashChain(byte[] hash) {
        this.hashChain = hash;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public byte[] getHashChain() {
        return hashChain;
    }

    public byte[] getBytes() {
        return Util.toASCIIBytes(toString());
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<" + XMLTags.ARIADNE_ROUTE_REQUEST + ">\n");
        buffer.append("\t<" + XMLTags.SOURCE + ">" + source + "</" + XMLTags.SOURCE + ">\n");
        buffer.append("\t<" + XMLTags.DESTINATION + ">" + destination + "</" + XMLTags.DESTINATION + ">\n");
        buffer.append("\t<" + XMLTags.CREATION_TIME + ">" + creationTime + "</" + XMLTags.CREATION_TIME + ">\n");
        buffer.append("\t<" + XMLTags.TIME_INTERVAL + ">" + String.valueOf(plannedTimeOfArrival) + "</" + XMLTags.TIME_INTERVAL + ">\n");
        buffer.append("\t<" + XMLTags.HASH_CHAIN + ">" + Util.toBase64String(hashChain) + "</" + XMLTags.HASH_CHAIN + ">\n");
        for (int index = 0; index < hops.size(); index++) {
            buffer.append("\t<" + XMLTags.HOP + ">" + hops.get(index) + "</" + XMLTags.HOP + ">\n");
        }
        buffer.append("\t<" + XMLTags.MACS + ">");
        for (int key_index = 0; key_index < macList.size(); key_index++) {
            buffer.append(Util.toBase64String((byte[]) macList.get(key_index)));
            if (key_index != macList.size() - 1) buffer.append(";");
        }
        buffer.append("</" + XMLTags.MACS + ">\n");
        buffer.append("</" + XMLTags.ARIADNE_ROUTE_REQUEST + ">\n");
        return buffer.toString();
    }

    public void parseMessage(String message) {
        Document messageDocument = XMLUtils.xmlUtils.getDocument(message);
        source = XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.ARIADNE_ROUTE_REQUEST + "/" + XMLTags.SOURCE, messageDocument);
        destination = XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.ARIADNE_ROUTE_REQUEST + "/" + XMLTags.DESTINATION, messageDocument);
        NodeList nodes = XMLUtils.xmlUtils.getNodeListFromXPath("/" + XMLTags.ARIADNE_ROUTE_REQUEST + "/" + XMLTags.HOP, messageDocument);
        hops = XMLUtils.xmlUtils.getValues(nodes);
        plannedTimeOfArrival = Long.valueOf(XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.ARIADNE_ROUTE_REQUEST + "/" + XMLTags.TIME_INTERVAL, messageDocument)).longValue();
        creationTime = Long.valueOf(XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.ARIADNE_ROUTE_REQUEST + "/" + XMLTags.CREATION_TIME, messageDocument)).longValue();
        try {
            hashChain = Util.fromBase64String(XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.ARIADNE_ROUTE_REQUEST + "/" + XMLTags.HASH_CHAIN, messageDocument));
        } catch (Base64Exception e) {
        }
        String tmp = XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.ARIADNE_ROUTE_REQUEST + "/" + XMLTags.MACS, messageDocument);
        if (tmp != "") {
            String[] keys = tmp.split(";");
            try {
                for (int i = 0; i < keys.length; i++) {
                    macList.add(Util.fromBase64String(keys[i]));
                }
            } catch (Base64Exception e) {
            }
        }
    }
}
