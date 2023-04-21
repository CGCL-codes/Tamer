package com.fisionlabs.analizer.chain.link;

import com.fisionlabs.queue.PacketQueue;
import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

public class UDPLink extends PacketLink {

    public UDPLink() {
    }

    @Override
    public boolean action(Packet packet) {
        if (packet instanceof UDPPacket) {
            System.out.println("UDP Packet");
            UDPPacket udpPacket = (UDPPacket) packet;
            PacketQueue.getInstance().addPacket(udpPacket);
            return true;
        } else if (next != null) next.action(packet);
        return false;
    }
}
