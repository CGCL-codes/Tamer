package com.l2jserver.gameserver.network.serverpackets;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 *
 * @author  devScarlet
 */
public class NicknameChanged extends L2GameServerPacket {

    private static final String _S__CC_TITLE_UPDATE = "[S] cc NicknameChanged";

    private String _title;

    private int _objectId;

    public NicknameChanged(L2PcInstance cha) {
        _objectId = cha.getObjectId();
        _title = cha.getTitle();
    }

    /**
	 * @see com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket#writeImpl()
	 */
    @Override
    protected void writeImpl() {
        writeC(0xcc);
        writeD(_objectId);
        writeS(_title);
    }

    /**
	 * @see com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket#getType()
	 */
    @Override
    public String getType() {
        return _S__CC_TITLE_UPDATE;
    }
}
