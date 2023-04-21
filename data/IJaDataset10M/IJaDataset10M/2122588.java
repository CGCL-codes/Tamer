package com.l2jserver.gameserver.network.serverpackets;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Format: (chd) ddd
 * d: Always -1
 * d: Origin Team
 * d: Destination Team
 * 
 * @author mrTJO
 */
public class ExCubeGameChangeTeam extends L2GameServerPacket {

    private static final String _S__FE_97_05_EXCUBEGAMECHANGETEAM = "[S] FE:97:05 ExCubeGameChangeTeam";

    L2PcInstance _player;

    boolean _fromRedTeam;

    /**
	 * Move Player from Team x to Team y
	 * 
	 * @param player Player Instance
	 * @param fromRedTeam Is Player from Red Team?
	 */
    public ExCubeGameChangeTeam(L2PcInstance player, boolean fromRedTeam) {
        _player = player;
        _fromRedTeam = fromRedTeam;
    }

    @Override
    protected void writeImpl() {
        writeC(0xfe);
        writeH(0x97);
        writeD(0x05);
        writeD(_player.getObjectId());
        writeD(_fromRedTeam ? 0x01 : 0x00);
        writeD(_fromRedTeam ? 0x00 : 0x01);
    }

    @Override
    public String getType() {
        return _S__FE_97_05_EXCUBEGAMECHANGETEAM;
    }
}
