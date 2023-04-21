package net.sf.l2j.gameserver.clientpackets;

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.serverpackets.PackageSendableList;

/**
 * Format: (c)d
 * d: char object id (?)
 * @author  -Wooden-
 */
public final class RequestPackageSendableItemList extends L2GameClientPacket {

    private static final String _C_9E_REQUESTPACKAGESENDABLEITEMLIST = "[C] 9E RequestPackageSendableItemList";

    private int _objectID;

    @Override
    protected void readImpl() {
        _objectID = readD();
    }

    /**
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#runImpl()
	 */
    @Override
    public void runImpl() {
        L2ItemInstance[] items = getClient().getActiveChar().getInventory().getAvailableItems(true);
        sendPacket(new PackageSendableList(items, _objectID));
    }

    /**
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
    @Override
    public String getType() {
        return _C_9E_REQUESTPACKAGESENDABLEITEMLIST;
    }
}
