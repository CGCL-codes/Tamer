package net.kano.joscar.snaccmd.rooms;

import net.kano.joscar.ByteBlock;
import net.kano.joscar.DefensiveTools;
import net.kano.joscar.flapcmd.SnacPacket;
import net.kano.joscar.snaccmd.MiniRoomInfo;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A SNAC command used to request more information about a chat room whose
 * {@linkplain MiniRoomInfo "mini room information"} is known. This request is
 * normally answered with a {@link RoomResponse}. This command can be used after
 * being invited to a chat room and receiving a <code>MiniRoomInfo</code> from
 * the associated {@link net.kano.joscar.rvcmd.chatinvite.ChatInvitationRvCmd}.
 *
 * @snac.src client
 * @snac.cmd 0x0d 0x04
 *
 * @author Stephen Flynn
 */
public class RoomInfoReq extends RoomCommand {

    /** The miniature room information block contained in this command. */
    private MiniRoomInfo roomInfo;

    /**
     * Generates a more-room-information command from the given incoming SNAC
     * packet.
     *
     * @param packet an incoming room more info request packet
     */
    protected RoomInfoReq(SnacPacket packet) {
        super(CMD_MORE_ROOM_INFO);
        DefensiveTools.checkNull(packet, "packet");
        ByteBlock snacData = packet.getData();
        roomInfo = MiniRoomInfo.readMiniRoomInfo(snacData);
    }

    /**
     * Creates a new outgoing room-more-info request for the room described by
     * the given miniature room information block.
     *
     * @param roomInfo a miniature room information block describing the room
     *        whose information is being requested
     */
    public RoomInfoReq(MiniRoomInfo roomInfo) {
        super(CMD_MORE_ROOM_INFO);
        this.roomInfo = roomInfo;
    }

    public void writeData(OutputStream out) throws IOException {
        if (roomInfo != null) roomInfo.write(out);
    }

    public String toString() {
        return "RoomInfoReq: " + roomInfo;
    }
}
