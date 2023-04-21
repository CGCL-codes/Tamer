package net.kano.joscar.snaccmd.rooms;

import net.kano.joscar.DefensiveTools;
import net.kano.joscar.flapcmd.SnacPacket;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A SNAC command sent to request a list of {@linkplain
 * net.kano.joscar.snaccmd.ExchangeInfo chat exchanges} available on the server
 * along with the maximum number of rooms a user can simultaneously occupy.
 * Normally responded-to with a {@link RoomResponse}.
 *
 * @snac.src client
 * @snac.cmd 0x0d 0x02
 *
 * @see RoomResponse
 */
public class RoomRightsRequest extends RoomCommand {

    /**
     * Generates a room rights request from the given incoming SNAC packet.
     *
     * @param packet the incoming room rights request
     */
    protected RoomRightsRequest(SnacPacket packet) {
        super(CMD_RIGHTS_REQ);
        DefensiveTools.checkNull(packet, "packet");
    }

    /**
     * Creates a new outgoing room rights request.
     */
    public RoomRightsRequest() {
        super(CMD_RIGHTS_REQ);
    }

    public void writeData(OutputStream out) throws IOException {
    }

    public String toString() {
        return "RoomRightsRequest";
    }
}
