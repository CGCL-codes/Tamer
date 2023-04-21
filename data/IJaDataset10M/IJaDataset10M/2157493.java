package com.l2jserver.gameserver.network.serverpackets;

/**
 * This class ...
 *
 * @version $Revision: 1.4.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 *
 * @author godson
 */
public class ExOlympiadMode extends L2GameServerPacket {

    private static final String _S__FE_2B_OLYMPIADMODE = "[S] FE:7c ExOlympiadMode";

    private int _mode;

    /**
	 * @param _mode (0 = return, 3 = spectate)
	 */
    public ExOlympiadMode(int mode) {
        _mode = mode;
    }

    @Override
    protected final void writeImpl() {
        writeC(0xfe);
        writeH(0x7c);
        writeC(_mode);
    }

    @Override
    public String getType() {
        return _S__FE_2B_OLYMPIADMODE;
    }
}
