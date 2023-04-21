package com.l2jserver.gameserver.network.serverpackets;

import com.l2jserver.gameserver.GameTimeController;

public class ClientSetTime extends L2GameServerPacket {

    private static final String _S__EC_CLIENTSETTIME = "[S] f2 ClientSetTime [dd]";

    public static final ClientSetTime STATIC_PACKET = new ClientSetTime();

    private ClientSetTime() {
    }

    @Override
    protected final void writeImpl() {
        writeC(0xf2);
        writeD(GameTimeController.getInstance().getGameTime());
        writeD(6);
    }

    @Override
    public String getType() {
        return _S__EC_CLIENTSETTIME;
    }
}
