package nakayo.gameserver.network.aion.serverpackets;

import nakayo.gameserver.network.aion.AionConnection;
import nakayo.gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * @author orz
 */
public class SM_GATHER_STATUS extends AionServerPacket {

    private int status;

    private int playerobjid;

    private int gatherableobjid;

    public SM_GATHER_STATUS(int playerobjid, int gatherableobjid, int status) {
        this.playerobjid = playerobjid;
        this.gatherableobjid = gatherableobjid;
        this.status = status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, playerobjid);
        writeD(buf, gatherableobjid);
        writeH(buf, 0);
        writeC(buf, status);
    }
}
