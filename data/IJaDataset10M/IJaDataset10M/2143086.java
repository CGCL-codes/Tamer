package net.sf.l2j.gameserver.serverpackets;

/**
 * Format: (ch)ddd
 *
 */
public class ExPutItemResultForVariationMake extends L2GameServerPacket {

    private static final String _S__FE_52_EXCONFIRMVARIATIONITEM = "[S] FE:53 ExPutItemResultForVariationMake";

    private int _itemObjId;

    private int _unk1;

    private int _unk2;

    public ExPutItemResultForVariationMake(int itemObjId) {
        _itemObjId = itemObjId;
        _unk1 = 1;
        _unk2 = 1;
    }

    /**
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#writeImpl()
	 */
    @Override
    protected void writeImpl() {
        writeC(0xfe);
        writeH(0x53);
        writeD(_itemObjId);
        writeD(_unk1);
        writeD(_unk2);
    }

    /**
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
    @Override
    public String getType() {
        return _S__FE_52_EXCONFIRMVARIATIONITEM;
    }
}
