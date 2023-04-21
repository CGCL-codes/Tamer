package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 *
 * @version $Revision: 1.4.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 * @author godson
 */
public class ExOlympiadUserInfo extends L2GameServerPacket {

    private static final String _S__FE_29_OLYMPIADUSERINFO = "[S] FE:2C OlympiadUserInfo";

    @SuppressWarnings("unused")
    private static L2PcInstance _activeChar;

    /**
	 * @param _player
	 * @param _side
	 *            (1 = right, 2 = left)
	 */
    public ExOlympiadUserInfo(L2PcInstance player) {
        _activeChar = player;
    }

    @Override
    protected final void writeImpl() {
    }

    @Override
    public String getType() {
        return _S__FE_29_OLYMPIADUSERINFO;
    }
}
