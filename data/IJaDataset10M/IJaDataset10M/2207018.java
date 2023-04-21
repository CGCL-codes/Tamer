package net.sf.l2j.gameserver.serverpackets;

/**
 * Format: ch ddd [ddd]
 * @author  KenM
 */
public class ExGetBossRecord extends L2GameServerPacket {

    private static final String _S__FE_33_EXGETBOSSRECORD = "[S] FE:34 ExGetBossRecord";

    private int _unk1, _unk2;

    public ExGetBossRecord(int val1, int val2) {
        _unk1 = val1;
        _unk2 = val2;
    }

    /**
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#writeImpl()
	 */
    @Override
    protected void writeImpl() {
        writeC(0xfe);
        writeH(0x34);
        writeD(_unk1);
        writeD(_unk2);
        writeD(0x00);
    }

    /**
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
    @Override
    public String getType() {
        return _S__FE_33_EXGETBOSSRECORD;
    }
}
