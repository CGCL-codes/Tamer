package net.sf.l2j.gameserver.network.clientpackets;

/**
 * format (ch) d
 *
 * @author -Wooden-
 */
public final class RequestOustFromPartyRoom extends L2GameClientPacket {

    private static final String _C__D0_01_REQUESTOUSTFROMPARTYROOM = "[C] D0:01 RequestOustFromPartyRoom";

    @SuppressWarnings("unused")
    private int _id;

    @Override
    protected void readImpl() {
        _id = readD();
    }

    @Override
    protected void runImpl() {
    }

    @Override
    public String getType() {
        return _C__D0_01_REQUESTOUSTFROMPARTYROOM;
    }
}
