package com.l2jserver.gameserver.network.serverpackets;

/**
 * This class ...
 *
 * @version $Revision: 1.2.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class PledgeShowMemberListDeleteAll extends L2GameServerPacket {

    private static final String _S__9B_PLEDGESHOWMEMBERLISTDELETEALL = "[S] 88 PledgeShowMemberListDeleteAll";

    public PledgeShowMemberListDeleteAll() {
    }

    @Override
    protected final void writeImpl() {
        writeC(0x88);
    }

    @Override
    public String getType() {
        return _S__9B_PLEDGESHOWMEMBERLISTDELETEALL;
    }
}
