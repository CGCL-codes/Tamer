package com.l2jserver.gameserver.network.serverpackets;

import com.l2jserver.gameserver.GameTimeController;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.4.2.5.2.6 $ $Date: 2005/03/27 15:29:39 $
 */
public class CharSelected extends L2GameServerPacket {

    private static final String _S__21_CHARSELECTED = "[S] 0b CharSelected";

    private L2PcInstance _activeChar;

    private int _sessionId;

    /**
	 * @param _characters
	 */
    public CharSelected(L2PcInstance cha, int sessionId) {
        _activeChar = cha;
        _sessionId = sessionId;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x0b);
        writeS(_activeChar.getName());
        writeD(_activeChar.getCharId());
        writeS(_activeChar.getTitle());
        writeD(_sessionId);
        writeD(_activeChar.getClanId());
        writeD(0x00);
        writeD(_activeChar.getAppearance().getSex() ? 1 : 0);
        writeD(_activeChar.getRace().ordinal());
        writeD(_activeChar.getClassId().getId());
        writeD(0x01);
        writeD(_activeChar.getX());
        writeD(_activeChar.getY());
        writeD(_activeChar.getZ());
        writeF(_activeChar.getCurrentHp());
        writeF(_activeChar.getCurrentMp());
        writeD(_activeChar.getSp());
        writeQ(_activeChar.getExp());
        writeD(_activeChar.getLevel());
        writeD(_activeChar.getKarma());
        writeD(_activeChar.getPkKills());
        writeD(_activeChar.getINT());
        writeD(_activeChar.getSTR());
        writeD(_activeChar.getCON());
        writeD(_activeChar.getMEN());
        writeD(_activeChar.getDEX());
        writeD(_activeChar.getWIT());
        writeD(GameTimeController.getInstance().getGameTime() % (24 * 60));
        writeD(0x00);
        writeD(_activeChar.getClassId().getId());
        writeD(0x00);
        writeD(0x00);
        writeD(0x00);
        writeD(0x00);
        writeB(new byte[64]);
        writeD(0x00);
    }

    @Override
    public String getType() {
        return _S__21_CHARSELECTED;
    }
}
