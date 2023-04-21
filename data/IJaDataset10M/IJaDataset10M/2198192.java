package com.l2jserver.gameserver.network.serverpackets;

public final class SurrenderPledgeWar extends L2GameServerPacket {

    private static final String _S__81_SURRENDERPLEDGEWAR = "[S] 67 SurrenderPledgeWar";

    private String _pledgeName;

    private String _playerName;

    public SurrenderPledgeWar(String pledge, String charName) {
        _pledgeName = pledge;
        _playerName = charName;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x67);
        writeS(_pledgeName);
        writeS(_playerName);
    }

    @Override
    public String getType() {
        return _S__81_SURRENDERPLEDGEWAR;
    }
}
