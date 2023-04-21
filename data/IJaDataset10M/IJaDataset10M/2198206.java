package net.sf.jxpilot.net.packet;

import net.sf.jxpilot.game.MissileHolder;
import net.sf.jgamelibrary.util.ByteBuffer;

/**
 * Holds data from a Missile packet.
 * @author Vlad Firoiu
 */
public class MissilePacket extends MissileHolder implements XPilotPacket {

    private byte pkt_type;

    @Override
    public byte getPacketType() {
        return pkt_type;
    }

    @Override
    public void readPacket(ByteBuffer in) throws PacketReadException {
        pkt_type = in.getByte();
        x = in.getShort();
        y = in.getShort();
        len = in.getUnsignedByte();
        dir = in.getUnsignedByte();
    }

    @Override
    public String toString() {
        return "Missile Packet\npacket type = " + pkt_type + "\nx = " + x + "\ny = " + y + "\nlen = " + len + "\ndir = " + dir;
    }
}
