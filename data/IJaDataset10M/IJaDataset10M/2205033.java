package com.l2jserver.gameserver.network.serverpackets;

/**
 * Format: ch
 * Trigger packet
 * @author  KenM
 */
public class ExShowVariationCancelWindow extends L2GameServerPacket {

    private static final String _S__FE_51_EXSHOWVARIATIONCANCELWINDOW = "[S] FE:52 ExShowVariationCancelWindow";

    /**
	 * @see com.l2jserver.util.network.BaseSendablePacket.ServerBasePacket#writeImpl()
	 */
    @Override
    protected void writeImpl() {
        writeC(0xfe);
        writeH(0x52);
    }

    /**
	 * @see com.l2jserver.gameserver.BasePacket#getType()
	 */
    @Override
    public String getType() {
        return _S__FE_51_EXSHOWVARIATIONCANCELWINDOW;
    }
}
