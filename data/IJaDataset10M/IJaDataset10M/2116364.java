package net.kano.joscar.snaccmd.icbm;

import net.kano.joscar.ByteBlock;
import net.kano.joscar.DefensiveTools;
import net.kano.joscar.OscarTools;
import net.kano.joscar.StringBlock;
import net.kano.joscar.flapcmd.SnacPacket;
import net.kano.joscar.snaccmd.ExtraInfoBlock;
import net.kano.joscar.tlv.Tlv;
import net.kano.joscar.tlv.TlvChain;
import net.kano.joscar.tlv.TlvTools;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/**
 * A SNAC command used to send an instant message to another user.
 *
 * @snac.src client
 * @snac.cmd 0x04 0x06
 *
 * @see RecvImIcbm
 */
public class SendImIcbm extends AbstractImIcbm implements SendIcbm {

    /**
     * A TLV type present if an acknowledgement of this message was requested.
     */
    private static final int TYPE_ACK = 0x0003;

    /** The screenname of the user to whom this IM is addressed. */
    private final String sn;

    /** Whether or not an acknowledgement packet was requested. */
    private final boolean ackRequested;

    /**
     * Generates an IM send command from the given incoming SNAC packet.
     *
     * @param packet an incoming IM send packet
     */
    protected SendImIcbm(SnacPacket packet) {
        super(IcbmCommand.CMD_SEND_ICBM, packet);
        DefensiveTools.checkNull(packet, "packet");
        ByteBlock snacData = getChannelData();
        StringBlock snInfo = OscarTools.readScreenname(snacData);
        sn = snInfo.getString();
        ByteBlock rest = snacData.subBlock(snInfo.getTotalSize());
        TlvChain imTlvs = TlvTools.readChain(rest);
        processImTlvs(imTlvs);
        ackRequested = imTlvs.hasTlv(TYPE_ACK);
    }

    /**
     * Creates a new outgoing IM with the given message text to the given
     * screenname. No icon is requested, no icon hash data are provided, the
     * message is not marked as an auto-response, and an {@link MessageAck
     * acknowledgement packet} <i>is</i> requested. Also, the ICBM message ID is
     * set to <code>0</code>.
     *
     * @param sn the screenname to whom to send the given message
     * @param message the message to send
     */
    public SendImIcbm(String sn, String message) {
        this(sn, new InstantMessage(message));
    }

    /**
     * Creates a new outgoing IM with the given message to the given
     * screenname. No icon is requested, no icon hash data are provided, the
     * message is not marked as an auto-response, and an {@link MessageAck
     * acknowledgement packet} <i>is</i> requested. Also, the ICBM message ID is
     * set to <code>0</code>.
     *
     * @param sn the screenname to whom to send the given message
     * @param message the message to send
     */
    public SendImIcbm(String sn, InstantMessage message) {
        this(sn, message, false);
    }

    public SendImIcbm(String sn, String message, boolean autoresponse) {
        this(sn, new InstantMessage(message), autoresponse);
    }

    /**
     * Creates a new unencrypted outgoing IM with the given message text to
     * the given screenname. No icon is requested, no icon hash data are
     * provided, and an {@link MessageAck acknowledgement packet} <i>is</i>
     * requested. Also, the ICBM message ID is set to <code>0</code>. The IM
     * "features" block used is {@link #FEATURES_DEFAULT}.
     *
     * @param sn the screenname to whom to send the given message
     * @param message the message to send
     * @param autoresponse whether this message is an "auto-response" or not
     */
    public SendImIcbm(String sn, InstantMessage message, boolean autoresponse) {
        this(sn, message, autoresponse, 0, false, null, null, FEATURES_DEFAULT, true);
    }

    public SendImIcbm(String sn, String message, boolean autoResponse, long messageId, boolean wantsIcon, OldIconHashInfo iconInfo, Collection<ExtraInfoBlock> expInfoBlocks, boolean ackRequested) {
        this(sn, new InstantMessage(message), autoResponse, messageId, wantsIcon, iconInfo, expInfoBlocks, ackRequested);
    }

    /**
     * Creates a new outgoing IM with the given properties.
     *
     * @param sn the screenname to hom to send the given message
     * @param message the message to send
     * @param autoResponse whether this message is an "auto-response" or not
     * @param messageId an ICBM message ID to attach to this message
     * @param wantsIcon whether or not to request the receiver's buddy icon
     * @param iconInfo a block of buddy icon hash information to "advertise" to
     *        the receiver
     * @param expInfoBlocks a list of AIM Expression information blocks
     * @param ackRequested whether a {@link MessageAck} should be sent in
     *        response to this command
     */
    public SendImIcbm(String sn, InstantMessage message, boolean autoResponse, long messageId, boolean wantsIcon, OldIconHashInfo iconInfo, Collection<ExtraInfoBlock> expInfoBlocks, boolean ackRequested) {
        this(sn, message, autoResponse, messageId, wantsIcon, iconInfo, expInfoBlocks, FEATURES_DEFAULT, ackRequested);
    }

    /**
     * Creates a new outgoing IM with the given properties.
     *
     * @param sn the screenname to hom to send the given message
     * @param message the message to send
     * @param autoResponse whether this message is an "auto-response" or not
     * @param messageId an ICBM message ID to attach to this message
     * @param wantsIcon whether or not to request the receiver's buddy icon
     * @param iconInfo a block of buddy icon hash information to "advertise" to
     *        the receiver
     * @param expInfoBlocks a list of AIM Expression information blocks
     * @param featuresBlock an IM "features" block, like {@link
     *        #FEATURES_DEFAULT}
     * @param ackRequested whether a {@link MessageAck} should be sent in
     *        response to this command
     */
    public SendImIcbm(String sn, InstantMessage message, boolean autoResponse, long messageId, boolean wantsIcon, OldIconHashInfo iconInfo, Collection<ExtraInfoBlock> expInfoBlocks, ByteBlock featuresBlock, boolean ackRequested) {
        super(IcbmCommand.CMD_SEND_ICBM, messageId, message, autoResponse, wantsIcon, iconInfo, expInfoBlocks, featuresBlock);
        DefensiveTools.checkNull(sn, "sn");
        if (ackRequested && autoResponse) {
            throw new IllegalArgumentException("ackRequested and autoResponse " + "cannot both be true");
        }
        this.sn = sn;
        this.ackRequested = ackRequested;
    }

    public final String getScreenname() {
        return sn;
    }

    /**
     * Returns whether or not a {@link MessageAck} was requested.
     *
     * @return whether or not a message acknowledgement packet was requested
     */
    public final boolean isAckRequested() {
        return ackRequested;
    }

    protected void writeChannelData(OutputStream out) throws IOException {
        OscarTools.writeScreenname(out, sn);
        writeImTlvs(out);
        if (ackRequested) new Tlv(TYPE_ACK).write(out);
    }

    public String toString() {
        return "SendImIcbm to " + sn + " (id=" + getIcbmMessageId() + ", ackreq=" + ackRequested + "): " + getMessage();
    }
}
