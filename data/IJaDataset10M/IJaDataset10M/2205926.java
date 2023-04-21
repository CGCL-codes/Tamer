package net.kano.joscar.snaccmd.mailcheck;

import net.kano.joscar.DefensiveTools;
import net.kano.joscar.flapcmd.SnacCommand;
import net.kano.joscar.flapcmd.SnacPacket;
import net.kano.joscar.snac.CmdType;
import net.kano.joscar.snac.SnacCmdFactory;
import java.util.List;

/**
 * A SNAC command factory for the client-bound SNAC commands provided in this
 * package, appropriate for use in an AIM client.
 */
public class ClientMailCheckCmdFactory implements SnacCmdFactory {

    /** The supported SNAC command types. */
    protected static final List<CmdType> SUPPORTED_TYPES = DefensiveTools.asUnmodifiableList(new CmdType(MailCheckCmd.FAMILY_MAILCHECK, MailCheckCmd.CMD_UPDATE));

    public List<CmdType> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    public SnacCommand genSnacCommand(SnacPacket packet) {
        if (packet.getFamily() != MailCheckCmd.FAMILY_MAILCHECK) return null;
        int command = packet.getCommand();
        if (command == MailCheckCmd.CMD_UPDATE) {
            return new MailUpdate(packet);
        } else {
            return null;
        }
    }
}
