package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;

/**
 * @author LokiReborn
 * 
 */
public class SM_WINDSTREAM_LOCATIONS extends AionServerPacket {

    private int bidirectional;

    private int mapId;

    private int streamId;

    private int boost;

    public SM_WINDSTREAM_LOCATIONS(int bidirectional, int mapId, int streamId, int boost) {
        this.bidirectional = bidirectional;
        this.mapId = mapId;
        this.streamId = streamId;
        this.boost = boost;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, bidirectional);
        writeD(buf, mapId);
        writeD(buf, streamId);
        writeC(buf, boost);
    }
}
