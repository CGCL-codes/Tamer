package net.kano.joscar.snaccmd.invite;

import net.kano.joscar.DefensiveTools;
import net.kano.joscar.flapcmd.SnacPacket;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A SNAC command sent to acknowledge that an invitation email has been sent.
 * Sent in response to an {@link InviteFriendCmd}.
 *
 * @snac.src server
 * @snac.cmd 0x06 0x03
 */
public class InviteFriendAck extends InviteCommand {

    /**
     * Generates a new invitation acknowledgement command from the given
     * incoming SNAC packet.
     *
     * @param packet the incoming invitation acknowledgement packet
     */
    protected InviteFriendAck(SnacPacket packet) {
        super(CMD_INVITE_ACK);
        DefensiveTools.checkNull(packet, "packet");
    }

    /**
     * Creates a new outgoing invitation acknowledgement command.
     */
    public InviteFriendAck() {
        super(CMD_INVITE_ACK);
    }

    public void writeData(OutputStream out) throws IOException {
    }

    public String toString() {
        return "InviteFriendAck";
    }
}
