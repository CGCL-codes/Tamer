package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer
 *
 */
public class SM_SUMMON_PANEL_REMOVE extends AionServerPacket {

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, 0);
    }
}
