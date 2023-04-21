package com.metamech.wocky;

import com.metamech.jabber.JabberID;
import com.metamech.jabber.Session;
import com.metamech.jabber.xml.Packet;
import com.metamech.jabber.xml.PacketListener;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class OpenStreamHandler implements PacketListener {

    public void notify(Packet packet) {
        Session session = packet.getSession();
        session.setStreamID(packet.getID());
        session.setJID(new JabberID(packet.getTo()));
        session.setStatus(Session.STREAMING);
    }
}
