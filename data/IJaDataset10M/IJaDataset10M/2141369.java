package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 *  @author ShanSoft
 *  Packets Structure: chdd
 */
public final class RequestTeleportBookMark extends L2GameClientPacket {

    private static final String _C__51_REQUESTTELEPORTBOOKMARK = "[C] 51 RequestTeleportBookMark";

    private int id;

    @Override
    protected void readImpl() {
        id = readD();
    }

    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        activeChar.teleportBookmarkGo(id);
    }

    @Override
    public String getType() {
        return _C__51_REQUESTTELEPORTBOOKMARK;
    }
}
