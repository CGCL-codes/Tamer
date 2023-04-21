package net.sf.l2j.gameserver.serverpackets;

/**
 * This class ...
 *
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class CharDeleteSuccess extends L2GameServerPacket {

    private static final String _S__33_CHARDELETEOK = "[S] 1d CharDeleteOk";

    @Override
    protected final void writeImpl() {
        writeC(0x1d);
    }

    @Override
    public String getType() {
        return _S__33_CHARDELETEOK;
    }
}
