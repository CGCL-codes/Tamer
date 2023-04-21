package com.l2jserver.gameserver.network.serverpackets;

/**
 * This class ...
 *
 * @version $Revision: 1.2.2.1.2.3 $ $Date: 2005/03/27 15:29:40 $
 */
public class SendTradeRequest extends L2GameServerPacket {

    private static final String _S__73_SENDTRADEREQUEST = "[S] 70 SendTradeRequest";

    private int _senderID;

    public SendTradeRequest(int senderID) {
        _senderID = senderID;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x70);
        writeD(_senderID);
    }

    @Override
    public String getType() {
        return _S__73_SENDTRADEREQUEST;
    }
}
