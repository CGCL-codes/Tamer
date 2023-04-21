package net.kano.joscar.snaccmd.conn;

import net.kano.joscar.ByteBlock;
import net.kano.joscar.DefensiveTools;
import net.kano.joscar.flapcmd.SnacPacket;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A SNAC command sent to indicate that the client has finished initializing
 * the session and is ready to "go online." This also tells the server more
 * than the previously sent {@link ClientVersionsCmd} about your SNAC family
 * versions.
 *
 * @snac.src client
 * @snac.cmd 0x01 0x02
 */
public class ClientReadyCmd extends ConnCommand {

    /** A list of SNAC family information objects to be sent in this command. */
    private final List<SnacFamilyInfo> infos;

    /**
     * Creates a new client ready command from the given incoming SNAC packet.
     *
     * @param packet an incoming client-ready packet
     */
    protected ClientReadyCmd(SnacPacket packet) {
        super(CMD_CLIENT_READY);
        DefensiveTools.checkNull(packet, "packet");
        ByteBlock snacData = packet.getData();
        int num = snacData.getLength() / 8;
        List<SnacFamilyInfo> infos = new ArrayList<SnacFamilyInfo>(num);
        for (int i = 0; i < num; i++) {
            infos.add(SnacFamilyInfo.readSnacFamilyInfo(snacData));
            snacData = snacData.subBlock(8);
        }
        this.infos = DefensiveTools.getUnmodifiable(infos);
    }

    /**
     * Creates a new outgoing client ready command with the given SNAC family
     * information blocks.
     *
     * @param infos the SNAC family information blocks to send with this command
     */
    public ClientReadyCmd(Collection<SnacFamilyInfo> infos) {
        super(CMD_CLIENT_READY);
        this.infos = DefensiveTools.getSafeListCopy(infos, "infos");
    }

    /**
     * Returns the SNAC family information blocks sent with this command.
     *
     * @return this command's SNAC family information blocks
     */
    public final List<SnacFamilyInfo> getSnacFamilyInfos() {
        return infos;
    }

    public void writeData(OutputStream out) throws IOException {
        if (infos != null) {
            for (SnacFamilyInfo info : infos) info.write(out);
        }
    }

    public String toString() {
        return "ClientReadyCmd: " + infos;
    }
}
