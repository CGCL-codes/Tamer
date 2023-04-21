package net.kano.joscar.snaccmd.icbm;

import net.kano.joscar.ByteBlock;
import net.kano.joscar.DefensiveTools;
import net.kano.joscar.LiveWritable;
import net.kano.joscar.OscarTools;
import net.kano.joscar.StringBlock;
import net.kano.joscar.flapcmd.SnacPacket;
import net.kano.joscar.snaccmd.CapabilityBlock;
import net.kano.joscar.tlv.TlvChain;
import net.kano.joscar.tlv.TlvTools;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A SNAC command used to send a rendezvous command to another user.
 *
 * @snac.src client
 * @snac.cmd 0x04 0x06
 *
 * @see RecvRvIcbm
 */
public class SendRvIcbm extends AbstractRvIcbm implements SendIcbm {

    /** The screenname of the recipient of this rendezvous command. */
    private final String sn;

    /**
     * Generates a new send-rendezvous command from the given incoming SNAC
     * packet.
     *
     * @param packet an incoming send-rendezvous packet
     */
    protected SendRvIcbm(SnacPacket packet) {
        super(IcbmCommand.CMD_SEND_ICBM, packet);
        DefensiveTools.checkNull(packet, "packet");
        ByteBlock channelData = getChannelData();
        StringBlock snInfo = OscarTools.readScreenname(channelData);
        sn = snInfo.getString();
        ByteBlock tlvBlock = channelData.subBlock(snInfo.getTotalSize());
        TlvChain chain = TlvTools.readChain(tlvBlock);
        processRvTlvs(chain);
    }

    /**
     * Creates a new outgoing rendezvous command with the given properties.
     *
     * @param sn the screenname to whom to send this rendezvous
     * @param icbmMessageId an ICBM message ID to attach to this command
     * @param status a status code, like {@link #RVSTATUS_REQUEST}
     * @param rvSessionId the ID of the rendezvous session on which this
     *        rendezvous is being sent
     * @param cap the capability block associated with this rendezvous command
     * @param rvDataWriter an object used to write the rendezvous-specific
     *        data to the connection
     */
    public SendRvIcbm(String sn, long icbmMessageId, int status, long rvSessionId, CapabilityBlock cap, LiveWritable rvDataWriter) {
        super(IcbmCommand.CMD_SEND_ICBM, icbmMessageId, status, rvSessionId, cap, rvDataWriter);
        DefensiveTools.checkNull(sn, "sn");
        this.sn = sn;
    }

    /**
     * Creates a new outgoing rendezvous to the given user with the properties
     * given by the given <code>RvCommand</code>.
     *
     * @param sn the screenname to whom this rendezvous command is being sent
     * @param icbmMessageId an ICBM message ID for this RV ICBM
     * @param rvSessionId a rendezvous session ID on which this rendezvous
     *        exists
     * @param command a rendezvous command that will be used to create this
     *        rendezvous packet
     */
    public SendRvIcbm(String sn, long icbmMessageId, long rvSessionId, RvCommand command) {
        super(IcbmCommand.CMD_SEND_ICBM, icbmMessageId, rvSessionId, command);
        DefensiveTools.checkNull(sn, "sn");
        this.sn = sn;
    }

    /**
     * Returns the screenname of the user to whom this rendezvous is addressed.
     *
     * @return the receiver's screenname
     */
    public final String getScreenname() {
        return sn;
    }

    protected final void writeChannelData(OutputStream out) throws IOException {
        OscarTools.writeScreenname(out, sn);
        writeRvTlvs(out);
    }

    public String toString() {
        return "SendRvIcbm to " + sn + ": <" + getCapability() + ">, status=" + getRvStatus() + ", sessid=" + getRvSessionId() + " -- " + super.toString();
    }
}
