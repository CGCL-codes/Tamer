package net.sf.l2j.gameserver.serverpackets;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 *
 *
 * sample
 * b0
 * d8 a8 10 48  objectId
 * 00 00 00 00
 * 00 00 00 00
 * 00 00
 *
 * format   ddddS
 *
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:39 $
 */
public class PartyMatchDetail extends L2GameServerPacket {

    private static final String _S__B0_PARTYMATCHDETAIL = "[S] 9d PartyMatchDetail";

    private L2PcInstance _activeChar;

    /**
	 * @param allPlayers
	 */
    public PartyMatchDetail(L2PcInstance player) {
        _activeChar = player;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x9d);
        writeD(_activeChar.getObjectId());
        if (_activeChar.isPartyMatchingShowLevel()) {
            writeD(1);
        } else {
            writeD(0);
        }
        if (_activeChar.isPartyMatchingShowClass()) {
            writeD(1);
        } else {
            writeD(0);
        }
        writeD(0);
        writeS("  " + _activeChar.getPartyMatchingMemo());
    }

    @Override
    public String getType() {
        return _S__B0_PARTYMATCHDETAIL;
    }
}
