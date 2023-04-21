package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;

/**
 * 
 * @author Sweetkr
 * 
 */
public class SM_DP_INFO extends AionServerPacket {

    private int playerObjectId;

    private int currentDp;

    public SM_DP_INFO(int playerObjectId, int currentDp) {
        this.playerObjectId = playerObjectId;
        this.currentDp = currentDp;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, playerObjectId);
        writeH(buf, currentDp);
    }
}
