package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * This packet is informing client that some AionObject is no longer visible.
 * 
 * @author -Nemesiss-
 * 
 */
public class SM_DELETE extends AionServerPacket {

    /**
	 * Object that is no longer visible.
	 */
    private final AionObject object;

    /**
	 * Constructor.
	 * 
	 * @param object
	 */
    public SM_DELETE(AionObject object) {
        this.object = object;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, object.getObjectId());
        writeC(buf, 0x00);
    }
}
