package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.AionObject;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_DELETE extends AionServerPacket {

    /**
	* Object that is no longer visible.
	*/
    private final int objectId;

    private final int time;

    /**
	* Constructor.
	* 
	* @param object
	*/
    public SM_DELETE(AionObject object, int time) {
        this.objectId = object.getObjectId();
        this.time = time;
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        int action = 0;
        if (action != 1) {
            writeD(buf, objectId);
            writeC(buf, time);
        }
    }
}
