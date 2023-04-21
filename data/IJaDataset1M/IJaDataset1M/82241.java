package net.sf.jxpilot.net.packet;

import net.sf.jgamelibrary.util.ByteBuffer;

/**
 * Holds data from an End packet.
 * @author Vlad Firoiu
 */
public final class EndPacket extends XPilotPacketAdaptor {

    private int loops;

    public int getLoops() {
        return loops;
    }

    @Override
    public void readPacket(ByteBuffer in) throws PacketReadException {
        pkt_type = in.getByte();
        loops = in.getInt();
    }

    @Override
    public String toString() {
        return "End Packet\npacket type = " + pkt_type + "\nloops = " + loops;
    }
}
