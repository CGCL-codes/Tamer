package net.sf.l2j.gameserver.serverpackets;

/**
 * Fromat: (ch)
 * (just a trigger)
 * @author -Wooden-
 *
 */
public class ExMailArrived extends L2GameServerPacket {

    private static final String _S__FE_2D_EXMAILARRIVED = "[S] FE:2e ExMailArrived";

    @Override
    protected void writeImpl() {
        writeC(0xfe);
        writeH(0x2e);
    }

    @Override
    public String getType() {
        return _S__FE_2D_EXMAILARRIVED;
    }
}
