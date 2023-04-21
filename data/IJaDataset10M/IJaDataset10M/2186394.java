package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Lyahim
 */
public class SM_LEAVE_GROUP_MEMBER extends AionServerPacket {

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, 0x00);
        writeD(buf, 0x00);
        writeH(buf, 0x00);
        writeC(buf, 0x00);
    }
}
