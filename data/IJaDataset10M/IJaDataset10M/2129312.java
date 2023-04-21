package nakayo.gameserver.network.aion.serverpackets;

import nakayo.gameserver.network.aion.AionConnection;
import nakayo.gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * @author Nemiroff
 */
public class SM_FLY_TIME extends AionServerPacket {

    private int currentFp;

    private int maxFp;

    public SM_FLY_TIME(int currentFp, int maxFp) {
        this.currentFp = currentFp;
        this.maxFp = maxFp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, currentFp);
        writeD(buf, maxFp);
    }
}
