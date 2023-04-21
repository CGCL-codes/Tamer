package j_waste.network;

import java.nio.ByteBuffer;

/**
 * This class represents the InitialPacket2 used during authentication.
 *
 * @author Fredrik Hultin (frehul@users.sourceforge.net)
 * @version 0.2
 */
public class InitialPacket2 extends Packet {

    private byte[] sPubCrypted;

    private static final int size = 512;

    /**
     * Constructs a new InitialPacket2.
     * @param sPubCrypted The sPubCrypted of this packet.
     */
    public InitialPacket2(byte[] sPubCrypted) {
        this.sPubCrypted = sPubCrypted;
    }

    /**
     * Returns the sPubCrypted of this packet.
     * @return the sPubCrypted of this packet.
     */
    public byte[] getSPubCrypted() {
        return sPubCrypted;
    }

    /**
     *
     */
    public static int getSize() {
        return size;
    }

    /**
     * Returns a byte array representation of the packet.
     * @return a byte array representation of the packet.
     */
    public byte[] toByteArray() {
        return packet;
    }

    /**
     * Finalizes the packet.
     */
    public void finalizePacket() {
        ByteBuffer packet = ByteBuffer.allocate(size);
        packet.put(sPubCrypted);
        this.packet = packet.array();
    }
}
