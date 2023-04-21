package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;

/**
 * 
 * @author Simple
 * 
 */
public class SM_LEGION_LEAVE_MEMBER extends AionServerPacket {

    private String name;

    private String name1;

    private int playerObjId;

    private int msgId;

    public SM_LEGION_LEAVE_MEMBER(int msgId, int playerObjId, String name) {
        this.msgId = msgId;
        this.playerObjId = playerObjId;
        this.name = name;
    }

    public SM_LEGION_LEAVE_MEMBER(int msgId, int playerObjId, String name, String name1) {
        this.msgId = msgId;
        this.playerObjId = playerObjId;
        this.name = name;
        this.name1 = name1;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, playerObjId);
        writeC(buf, 0x00);
        writeD(buf, 0x00);
        writeD(buf, msgId);
        writeS(buf, name);
        writeS(buf, name1);
    }
}
