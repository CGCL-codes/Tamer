package net.sf.l2j.gameserver.network.serverpackets;

/**
 * This class ...
 *
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class PartySmallWindowDeleteAll extends L2GameServerPacket {

    private static final String _S__65_PARTYSMALLWINDOWDELETEALL = "[S] 50 PartySmallWindowDeleteAll";

    @Override
    protected final void writeImpl() {
        writeC(0x50);
    }

    @Override
    public String getType() {
        return _S__65_PARTYSMALLWINDOWDELETEALL;
    }
}
