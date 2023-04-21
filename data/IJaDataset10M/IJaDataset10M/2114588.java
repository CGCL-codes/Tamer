package com.anzsoft.client.XMPP.log;

import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppMessage;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppPresence;
import com.anzsoft.client.XMPP.XmppQuery;
import com.anzsoft.client.XMPP.XmppStatus;

public class XmppLogHelper {

    public static void writeStatus(final XmppStatus xmppStatus, final LoggerOuput output) {
        output.log("[status - id: " + xmppStatus.getID() + "]");
    }

    public static void writePacket(final XmppPacket packet, final LoggerOuput output) {
        output.log(packet.toXML());
    }

    public static void writeMessage(final XmppMessage message, final LoggerOuput output) {
        writePacket(message, output);
    }

    public static void writePresence(final XmppPresence presence, final LoggerOuput output) {
        writePacket(presence, output);
    }

    static void writeID(final XmppID id, final LoggerOuput output) {
    }

    public static void writeQuery(final XmppQuery query, final LoggerOuput output) {
        writePacket(query, output);
        output.log("[query - xmlns: " + query.getQueryXMLNS() + "]");
    }
}
