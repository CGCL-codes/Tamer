package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;

/**
 * @author Sweetkr
 */
public class SM_MANTRA_EFFECT extends AionServerPacket {

    private Player player;

    private int subEffectId;

    public SM_MANTRA_EFFECT(Player player, int subEffectId) {
        this.player = player;
        this.subEffectId = subEffectId;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, 0x00);
        writeD(buf, player.getObjectId());
        writeH(buf, subEffectId);
    }
}
