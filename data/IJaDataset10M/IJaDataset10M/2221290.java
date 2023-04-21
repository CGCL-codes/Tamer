package com.metamech.wocky;

import java.util.HashMap;
import java.util.Iterator;
import com.metamech.jabber.xml.Packet;
import com.metamech.jabber.xml.PacketListener;
import com.metamech.jabber.xml.PacketQueue;
import com.metamech.log.Log;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class TestThread extends Thread {

    PacketQueue packetQueue = new PacketQueue();

    public PacketQueue getQueue() {
        return packetQueue;
    }

    HashMap packetListeners = new HashMap();

    public boolean addListener(PacketListener listener, String element) {
        if (listener == null || element == null) {
            return false;
        }
        packetListeners.put(listener, element);
        return true;
    }

    public boolean removeListener(PacketListener listener) {
        packetListeners.remove(listener);
        return true;
    }

    public void run() {
    }

    JabberModel model;

    public void setModel(JabberModel newModel) {
        model = newModel;
    }

    Packet waitFor(String element, String type) {
        for (Packet packet = packetQueue.pull(); packet != null; packet = packetQueue.pull()) {
            notifyHandlers(packet);
            if (packet.getElement().equals(element)) {
                if (type != null) {
                    if (packet.getType().equals(type)) {
                        return packet;
                    }
                } else {
                    return packet;
                }
            }
        }
        return null;
    }

    void notifyHandlers(Packet packet) {
        try {
            Packet child;
            String matchString;
            if (packet.getElement().equals("iq")) {
                child = packet.getFirstChild("query");
                if (child == null) {
                    matchString = "iq";
                } else {
                    matchString = child.getNamespace();
                }
            } else {
                matchString = packet.getElement();
            }
            synchronized (packetListeners) {
                Iterator iter = packetListeners.keySet().iterator();
                while (iter.hasNext()) {
                    PacketListener listener = (PacketListener) iter.next();
                    String listenerString = (String) packetListeners.get(listener);
                    if (listenerString.equals(matchString)) {
                        listener.notify(packet);
                    }
                }
            }
        } catch (Exception ex) {
            Log.error("TestThread: ", ex);
        }
    }
}
