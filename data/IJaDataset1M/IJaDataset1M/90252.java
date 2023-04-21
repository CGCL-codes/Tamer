package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * This packet is used to teleport player
 * 
 * @author Luno , orz
 * 
 */
public class SM_TELEPORT_LOC extends AionServerPacket {

    private int mapId;

    private float x, y, z;

    public SM_TELEPORT_LOC(int mapId, float x, float y, float z) {
        this.mapId = mapId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, 3);
        writeC(buf, 0x90);
        writeC(buf, 0x9E);
        writeD(buf, mapId);
        writeF(buf, x);
        writeF(buf, y);
        writeF(buf, z);
        writeC(buf, 0);
    }
}
