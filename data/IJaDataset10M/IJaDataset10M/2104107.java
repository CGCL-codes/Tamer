package net.sf.l2j.gameserver.network.serverpackets;

/**
 * sample
 * <p>
 * 7d c1 b2 e0 4a 00 00 00 00
 * <p>
 * format cdd
 *
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class AskJoinAlly extends L2GameServerPacket {

    private static final String _S__A8_ASKJOINALLY_0XA8 = "[S] a8 AskJoinAlly 0xa8";

    private String _requestorName;

    private int _requestorObjId;

    /**
     *
     */
    public AskJoinAlly(int requestorObjId, String requestorName) {
        _requestorName = requestorName;
        _requestorObjId = requestorObjId;
    }

    @Override
    protected final void writeImpl() {
        writeC(0xa8);
        writeD(_requestorObjId);
        writeS(_requestorName);
    }

    @Override
    public String getType() {
        return _S__A8_ASKJOINALLY_0XA8;
    }
}
