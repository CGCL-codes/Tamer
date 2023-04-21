package com.internetcds.jdbc.tds;

public class PacketResult {

    public static final String cvsVersion = "$Id: PacketResult.java,v 1.2 2001-08-31 12:47:20 curthagenlocher Exp $";

    private byte packetType = 0;

    public PacketResult(byte type) {
        setPacketType(type);
    }

    public byte getPacketType() {
        return packetType;
    }

    public void setPacketType(byte value) {
        packetType = value;
    }
}
