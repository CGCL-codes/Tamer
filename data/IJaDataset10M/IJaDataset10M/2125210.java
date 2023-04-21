package com.l2jserver.gameserver.network.communityserver.writepackets;

import org.netcon.BaseWritePacket;

/**
 * @authors  Forsaiken, Gigiikun
 */
public final class RequestShowCommunityBoard extends BaseWritePacket {

    public RequestShowCommunityBoard(final int playerObjId, final String cmd) {
        super.writeC(0x02);
        super.writeC(0x00);
        super.writeD(playerObjId);
        super.writeS(cmd);
    }
}
