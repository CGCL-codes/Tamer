package com.ryanhirsch.xmpp.xml;

import java.util.HashMap;
import java.util.Iterator;
import com.ryanhirsch.xmpp.Session;
import com.ryanhirsch.log.Log;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author
 * @version 1.0
 */
public class QueueThread extends Thread {

    PacketQueue packetQueue;

    public QueueThread(PacketQueue queue) {
        packetQueue = queue;
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
        for (Packet packet = packetQueue.pull(); packet != null; packet = packetQueue.pull()) {
            try {
                if (packet.getFrom() == null) {
                    packet.setFrom(packet.getSession().getJID().toString());
                }
                String matchString = packet.getElement();
                synchronized (packetListeners) {
                    Iterator iter = packetListeners.keySet().iterator();
                    while (iter.hasNext()) {
                        PacketListener listener = (PacketListener) iter.next();
                        String listenerString = (String) packetListeners.get(listener);
                        if (listenerString.equals(matchString) || listenerString.length() == 0) {
                            listener.notify(packet);
                            return;
                        }
                    }
                }
            } catch (Exception ex) {
                Log.error("QueueThread: ", ex);
            }
        }
    }
}
