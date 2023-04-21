package net.kano.joscar.snaccmd.invite;

import net.kano.joscar.ByteBlock;
import net.kano.joscar.DefensiveTools;
import net.kano.joscar.flapcmd.SnacPacket;
import net.kano.joscar.tlv.Tlv;
import net.kano.joscar.tlv.TlvChain;
import net.kano.joscar.tlv.TlvTools;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A SNAC command used to request that AOL send an email to someone to invite
 * him or her to join AIM. Normally responded-to with an {@link
 * InviteFriendAck}.
 *
 * @snac.src client
 * @snac.cmd 0x06 0x02
 */
public class InviteFriendCmd extends InviteCommand {

    /** A TLV type containing the email address to invite. */
    private static final int TYPE_EMAIL = 0x0011;

    /** A TLV type containing an invitation message. */
    private static final int TYPE_MSG = 0x0015;

    /** The email address to whom this invitation is addressed. */
    private final String email;

    /** The message to include in the invitation. */
    private final String message;

    /**
     * Generates a new invite-a-friend command from the given incoming SNAC
     * packet.
     *
     * @param packet an incoming invite-a-friend packet
     */
    protected InviteFriendCmd(SnacPacket packet) {
        super(CMD_INVITE_FRIEND);
        DefensiveTools.checkNull(packet, "packet");
        ByteBlock block = packet.getData();
        TlvChain chain = TlvTools.readChain(block);
        email = chain.getString(TYPE_EMAIL);
        message = chain.getString(TYPE_MSG);
    }

    /**
     * Creates a new outgoing invite-a-friend request to the given email address
     * and with the given invitation message.
     *
     * @param email the email address of the person to whom the invitation
     *        should be sent
     * @param message a message, like "HAY JOIN AIMZ D00D"
     */
    public InviteFriendCmd(String email, String message) {
        super(CMD_INVITE_FRIEND);
        this.email = email;
        this.message = message;
    }

    /**
     * Returns the email address to whom an invitation should be sent.
     *
     * @return the email address to whom an invitatoin should be sent
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Returns an "invitation message" to include in the invitation.
     *
     * @return the invitation message to send
     */
    public final String getMessage() {
        return message;
    }

    public void writeData(OutputStream out) throws IOException {
        if (email != null) {
            Tlv.getStringInstance(TYPE_EMAIL, email).write(out);
        }
        if (message != null) {
            Tlv.getStringInstance(TYPE_MSG, message).write(out);
        }
    }

    public String toString() {
        return "InviteFriendCmd for " + email + ", message: " + message;
    }
}
