package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer
 *
 */
public class SM_RIFT_STATUS extends AionServerPacket {

    private int usedEntries;

    private int maxEntries;

    private int maxLevel;

    private int targetObjectId;

    public SM_RIFT_STATUS(int targetObjId, int usedEntries, int maxEntries, int maxLevel) {
        this.targetObjectId = targetObjId;
        this.usedEntries = usedEntries;
        this.maxEntries = maxEntries;
        this.maxLevel = maxLevel;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, targetObjectId);
        writeD(buf, usedEntries);
        writeD(buf, maxEntries);
        writeD(buf, 6793);
        writeD(buf, 25);
        writeD(buf, maxLevel);
    }
}
