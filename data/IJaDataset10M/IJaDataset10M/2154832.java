package net.sf.l2j.gameserver.network.clientpackets;

/**
 * Format: (ch) S
 *
 * @author -Wooden-
 */
public class RequestAskJoinPartyRoom extends L2GameClientPacket {

    private static final String _C__D0_14_REQUESTASKJOINPARTYROOM = "[C] D0:14 RequestAskJoinPartyRoom";

    private String _player;

    @Override
    protected void readImpl() {
        _player = readS();
    }

    @Override
    protected void runImpl() {
        System.out.println("C5:RequestAskJoinPartyRoom: S: " + _player);
    }

    @Override
    public String getType() {
        return _C__D0_14_REQUESTASKJOINPARTYROOM;
    }
}
