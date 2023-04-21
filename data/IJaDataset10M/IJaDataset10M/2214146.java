package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Sweetkr
 */
public class SM_TRANSFORM extends AionServerPacket {

    private Creature creature;

    private int state;

    public SM_TRANSFORM(Creature creature) {
        this.creature = creature;
        this.state = creature.getState();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, creature.getObjectId());
        writeD(buf, creature.getTransformedModelId());
        writeH(buf, state);
        writeF(buf, 0.55f);
        writeF(buf, 1.5f);
        writeC(buf, 0);
    }
}
