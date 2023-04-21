package com.inetmon.jn.ui.actions;

import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

public class EthernetPacketGenerator extends PacketType {

    public EthernetPacketGenerator(int Addr1, int Addr2, int Addr3, int Addr4, byte[] mac, byte[] byteArray, Packet PG) {
        super(Addr1, Addr2, Addr3, Addr4, mac, byteArray, PG);
    }

    public EthernetPacketGenerator() {
        super();
    }

    public Packet packetDetail(int Addr1, int Addr2, int Addr3, int Addr4, byte[] mac, byte[] byteArray, Packet PG) {
        Packet p = new Packet();
        p.data = "data".getBytes();
        EthernetPacket ether = new EthernetPacket();
        ether.frametype = EthernetPacket.ETHERTYPE_IP;
        ether.src_mac = mac;
        ether.dst_mac = byteArray;
        p.datalink = ether;
        PG = p;
        return PG;
    }
}
