package com.l2jserver.gameserver.network.serverpackets;

public final class StopPledgeWar extends L2GameServerPacket {

    private static final String _S__7f_STOPPLEDGEWAR = "[S] 65 StopPledgeWar";

    private String _pledgeName;

    private String _playerName;

    public StopPledgeWar(String pledge, String charName) {
        _pledgeName = pledge;
        _playerName = charName;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x65);
        writeS(_pledgeName);
        writeS(_playerName);
    }

    @Override
    public String getType() {
        return _S__7f_STOPPLEDGEWAR;
    }
}
