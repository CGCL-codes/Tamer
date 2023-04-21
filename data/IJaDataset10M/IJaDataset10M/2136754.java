package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_LOGIN_QUEUE extends AionServerPacket {

    private int waitingPosition;

    private int waitingTime;

    private int waitingCount;

    private SM_LOGIN_QUEUE() {
        this.waitingPosition = 5;
        this.waitingTime = 60;
        this.waitingCount = 50;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, waitingPosition);
        writeD(buf, waitingTime);
        writeD(buf, waitingCount);
    }
}
