package com.l2jserver.gameserver.network.serverpackets;

/**
 * This class ...
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:39 $
 */
public final class LeaveWorld extends L2GameServerPacket {

    private static final String _S__96_LEAVEWORLD = "[S] 84 LeaveWorld";

    public static final LeaveWorld STATIC_PACKET = new LeaveWorld();

    @Override
    protected final void writeImpl() {
        writeC(0x84);
    }

    @Override
    public String getType() {
        return _S__96_LEAVEWORLD;
    }
}
