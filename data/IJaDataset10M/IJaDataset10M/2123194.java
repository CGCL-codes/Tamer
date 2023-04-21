package net.kano.joscar.snaccmd.conn;

import net.kano.joscar.flapcmd.SnacPacket;
import net.kano.joscar.snaccmd.ExtraInfoBlock;
import java.util.Collection;

/**
 * A SNAC command used for setting some sort of security information. As of this
 * writing, the significance of this command is unknown. This command is
 * normally responded-to with a {@link EncryptionInfoAck} which contains the
 * same extra info blocks sent in this command.
 * <br>
 * <br>
 * When sent by the official AIM clients, this command normally contains two
 * extra info blocks, the first of type {@link
 * ExtraInfoBlock#TYPE_CERTINFO_HASHA} and the second of type {@link
 * ExtraInfoBlock#TYPE_CERTINFO_HASHB}. They normally contain {@link
 * net.kano.joscar.snaccmd.CertificateInfo#HASHA_DEFAULT} and {@link
 * net.kano.joscar.snaccmd.CertificateInfo#HASHB_DEFAULT}, respectively.
 *
 * @snac.src client
 * @snac.cmd 0x01 0x22
 *
 * @see EncryptionInfoAck
 * @see ExtraInfoBlock
 * @see net.kano.joscar.snaccmd.CertificateInfo
 */
public class SetEncryptionInfoCmd extends AbstractExtraInfoCmd {

    /**
     * Creates a new set-encryption-info command from the given incoming
     * set-encryption-info SNAC packet.
     *
     * @param packet an incoming set-encryption-info SNAC packet
     */
    protected SetEncryptionInfoCmd(SnacPacket packet) {
        super(CMD_SETENCINFO, packet);
    }

    /**
     * Creates a new outgoing set-encryption-info command with the given list of
     * extra info blocks. Note that neither <code>blocks</code> nor any of its
     * elements can be <code>null</code>.
     *
     * @param blocks the list of extra info blocks to send in this command
     */
    public SetEncryptionInfoCmd(Collection<ExtraInfoBlock> blocks) {
        super(CMD_SETENCINFO, blocks);
    }
}
