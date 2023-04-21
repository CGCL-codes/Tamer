package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Nemiroff
 * Date: 01.12.2009
 */
public class SM_TITLE_UPDATE extends AionServerPacket {

    private int objectId;

    private int titleId;

    /**
     * Constructs new <tt>SM_TITLE_UPDATE </tt> packet
     * @param player
     * @param titleId
     */
    public SM_TITLE_UPDATE(Player player, int titleId) {
        this.objectId = player.getObjectId();
        this.titleId = titleId;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, this.objectId);
        writeD(buf, this.titleId);
    }
}
