package net.sf.l2j.gameserver.network.serverpackets;

/**
 * This class ...
 *
 * @version $Revision: $ $Date: $
 * @author Luca Baldi
 */
public class ExQuestInfo extends L2GameServerPacket {

    private static final String _S__FE_19_EXQUESTINFO = "[S] FE:19 EXQUESTINFO";

    @Override
    protected void writeImpl() {
        writeC(0xfe);
        writeH(0x19);
    }

    @Override
    public String getType() {
        return _S__FE_19_EXQUESTINFO;
    }
}
