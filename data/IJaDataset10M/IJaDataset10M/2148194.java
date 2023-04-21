package org.sepp.stm.messages.routing.ariadne;

import iaik.utils.Base64Exception;
import iaik.utils.Util;
import org.sepp.stm.messages.Message;
import org.sepp.stm.messages.SeppMessage;
import org.sepp.stm.utils.ByteArray;
import org.sepp.stm.utils.XMLTags;
import org.sepp.stm.utils.XMLUtils;
import org.w3c.dom.Document;

public class KeyRequest implements Message {

    public static final int type = 2787;

    private String source;

    private String destination;

    private long creationTime;

    private byte[] mac;

    public KeyRequest(SeppMessage message) {
        parseMessage(message.getData());
    }

    public KeyRequest(String source, String destination) {
        this.source = source;
        this.destination = destination;
        this.creationTime = System.currentTimeMillis();
    }

    public String getSource() {
        return this.source;
    }

    public String getDestination() {
        return this.destination;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public byte[] getMac() {
        return this.mac;
    }

    public void setMac(byte[] mac) {
        this.mac = mac;
    }

    public int getType() {
        return type;
    }

    public byte[] getBytes() {
        return Util.toASCIIBytes(toString());
    }

    public byte[] getBytesForMac() {
        ByteArray buffer = new ByteArray();
        buffer.appendBytes(source.getBytes());
        buffer.appendBytes(destination.getBytes());
        buffer.appendBytes(Long.valueOf(creationTime).byteValue());
        return buffer.getBytes();
    }

    /**
	 * Get a string representation of the message.
	 * 
	 * @return Returns the message as a string.
	 */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<" + XMLTags.ARIADNE_KEY_REQUEST + ">\n");
        buffer.append("\t<" + XMLTags.SOURCE + ">" + source + "</" + XMLTags.SOURCE + ">\n");
        buffer.append("\t<" + XMLTags.DESTINATION + ">" + destination + "</" + XMLTags.DESTINATION + ">\n");
        buffer.append("\t<" + XMLTags.CREATION_TIME + ">" + creationTime + "</" + XMLTags.CREATION_TIME + ">\n");
        if (mac != null) buffer.append("\t<" + XMLTags.MAC + ">" + Util.toBase64String(mac) + "</" + XMLTags.MAC + ">\n");
        buffer.append("</" + XMLTags.ARIADNE_KEY_REQUEST + ">\n");
        return buffer.toString();
    }

    public void parseMessage(String message) {
        Document messageDocument = XMLUtils.xmlUtils.getDocument(message);
        source = XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.ARIADNE_KEY_REQUEST + "/" + XMLTags.SOURCE, messageDocument);
        destination = XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.ARIADNE_KEY_REQUEST + "/" + XMLTags.DESTINATION, messageDocument);
        creationTime = Long.valueOf(XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.ARIADNE_KEY_REQUEST + "/" + XMLTags.CREATION_TIME, messageDocument)).longValue();
        try {
            mac = Util.fromBase64String(XMLUtils.xmlUtils.getStringFromXPath(XMLTags.ARIADNE_KEY_REQUEST + "/" + XMLTags.MAC, messageDocument));
        } catch (Base64Exception e) {
        }
    }
}
