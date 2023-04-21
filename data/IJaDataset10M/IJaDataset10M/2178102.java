package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 *
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class RecipeShopMsg extends L2GameServerPacket {

    private static final String _S__DB_RecipeShopMsg = "[S] db RecipeShopMsg";

    private L2PcInstance _activeChar;

    public RecipeShopMsg(L2PcInstance player) {
        _activeChar = player;
    }

    @Override
    protected final void writeImpl() {
        writeC(0xdb);
        writeD(_activeChar.getObjectId());
        writeS(_activeChar.getCreateList().getStoreName());
    }

    @Override
    public String getType() {
        return _S__DB_RecipeShopMsg;
    }
}
