package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_EMOTION_LIST extends AionServerPacket {

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, 0x00);
        writeH(buf, 13);
        for (int i = 0; i < 13; i++) {
            writeD(buf, 64 + i);
            writeH(buf, 0x00);
        }
    }
}
