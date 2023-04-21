package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_PING_RESPONSE extends AionServerPacket {

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, 0x04);
    }
}
