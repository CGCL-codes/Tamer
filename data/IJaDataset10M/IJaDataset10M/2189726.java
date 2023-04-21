package net.sf.l2j.gameserver.network.serverpackets;

public class EnchantResult extends L2GameServerPacket {

    private static final String _S__81_ENCHANTRESULT = "[S] 81 EnchantResult";

    private int _unknown;

    public EnchantResult(int unknown) {
        _unknown = unknown;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x81);
        writeD(_unknown);
    }

    @Override
    public String getType() {
        return _S__81_ENCHANTRESULT;
    }
}
