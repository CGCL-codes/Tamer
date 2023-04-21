package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.utils.gametime.GameTimeManager;

/**
 * In this packet Server is sending player stats values
 * 
 * @author -Nemesiss-
 * @author Luno
 */
public class SM_STATS_INFO extends AionServerPacket {

    /**
	 * Player that stats info will be send
	 */
    private Player player;

    /**
	 * Constructs new <tt>SM_UI</tt> packet
	 * 
	 * @param player
	 */
    public SM_STATS_INFO(Player player) {
        this.player = player;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        PlayerCommonData pcd = player.getCommonData();
        writeD(buf, player.getObjectId());
        writeD(buf, GameTimeManager.getGameTime().getTime());
        writeH(buf, 91);
        writeH(buf, 90);
        writeH(buf, 96);
        writeH(buf, 50);
        writeH(buf, 115);
        writeH(buf, 114);
        writeH(buf, 1);
        writeH(buf, 2);
        writeH(buf, 3);
        writeH(buf, 4);
        writeD(buf, 0);
        writeH(buf, pcd.getLevel());
        writeH(buf, 6392);
        writeD(buf, 1);
        writeQ(buf, 10L * Integer.MAX_VALUE);
        writeQ(buf, 2L * Integer.MAX_VALUE);
        writeQ(buf, 3L * Integer.MAX_VALUE);
        writeD(buf, 0);
        writeD(buf, 40);
        writeD(buf, 1);
        writeD(buf, 5000000);
        writeD(buf, 3000000);
        writeH(buf, 8000);
        writeH(buf, 7000);
        writeD(buf, 60);
        writeD(buf, 60);
        writeH(buf, 22016);
        writeH(buf, 18457);
        writeH(buf, 0xFEDC);
        writeH(buf, 0xEFAA);
        writeH(buf, 0);
        writeH(buf, 0xFAAC);
        writeH(buf, 0);
        writeH(buf, 0);
        writeH(buf, 0);
        writeH(buf, 1758);
        writeH(buf, 1987);
        writeH(buf, 2457);
        writeH(buf, 21852);
        writeH(buf, 23823);
        writeH(buf, 1);
        writeH(buf, 2);
        writeH(buf, 3);
        writeH(buf, 4);
        writeH(buf, 0);
        writeH(buf, 5);
        writeH(buf, 0);
        writeH(buf, 6);
        writeH(buf, 0);
        writeH(buf, 0);
        writeD(buf, 2);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, pcd.getPlayerClass().getClassId());
        writeD(buf, 0);
        writeB(buf, new byte[16]);
        writeH(buf, 91);
        writeH(buf, 90);
        writeH(buf, 96);
        writeH(buf, 50);
        writeH(buf, 115);
        writeH(buf, 114);
        writeH(buf, 0);
        writeH(buf, 0);
        writeH(buf, 0);
        writeH(buf, 0);
        writeD(buf, 0);
        writeD(buf, 100);
        writeD(buf, 150);
        writeD(buf, 0);
        writeD(buf, 60);
        writeH(buf, 0);
        writeH(buf, 0);
        writeH(buf, 0);
        writeH(buf, 5);
        writeH(buf, 4);
        writeH(buf, 0);
        writeF(buf, 0);
        writeH(buf, 0);
        writeH(buf, 0);
        writeH(buf, 0);
        writeH(buf, 1);
        writeH(buf, 2);
        writeH(buf, 3);
        writeH(buf, 4);
        writeH(buf, 0);
        writeH(buf, 5);
        writeH(buf, 0);
        writeH(buf, 6);
        writeH(buf, 0);
    }
}
