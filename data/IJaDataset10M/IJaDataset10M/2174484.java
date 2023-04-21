package com.peterhi.client.nio.handlers;

import com.peterhi.client.nio.DatagramHandler;
import com.peterhi.client.nio.DatagramSession;
import com.peterhi.client.nio.NetworkManager;
import com.peterhi.client.nio.events.NetWhiteboardEvent;
import com.peterhi.elements.Pixels;
import com.peterhi.net.Protocol;
import com.peterhi.net.packet.Packet;

public class NetPixelsHandler implements DatagramHandler {

    public void handle(NetworkManager man, DatagramSession session, Packet packet) throws Exception {
        Pixels o = new Pixels();
        o.deserialize(packet.getBuf(), Protocol.RUDP_CURSOR);
        man.fireOnElementAdded(new NetWhiteboardEvent(this, o));
    }
}
