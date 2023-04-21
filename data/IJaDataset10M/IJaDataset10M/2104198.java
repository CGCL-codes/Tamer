package gameserver.network.aion.serverpackets;

import gameserver.configs.GeneralConfig;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_CHARACTER_SELECT extends AionServerPacket {

    private int type;

    private int messageType;

    private int wrongCount;

    public SM_CHARACTER_SELECT(int type) {
        this.type = type;
    }

    public SM_CHARACTER_SELECT(int type, int messageType, int wrongCount) {
        this.type = type;
        this.messageType = messageType;
        this.wrongCount = wrongCount;
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, type);
        switch(type) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                writeH(buf, messageType);
                writeC(buf, wrongCount > 0 ? 1 : 0);
                writeD(buf, wrongCount);
                writeD(buf, GeneralConfig.PASSKEY_WRONG_MAXCOUNT);
                break;
        }
    }
}
