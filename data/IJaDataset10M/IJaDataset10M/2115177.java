package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.gameserver.model.PartyMatchRoom;
import com.l2jserver.gameserver.model.PartyMatchRoomList;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExClosePartyRoom;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * 
 * @author Gnacik
 *
 */
public final class RequestWithdrawPartyRoom extends L2GameClientPacket {

    private int _roomid;

    @SuppressWarnings("unused")
    private int _unk1;

    @Override
    protected void readImpl() {
        _roomid = readD();
        _unk1 = readD();
    }

    @Override
    protected void runImpl() {
        final L2PcInstance _activeChar = getClient().getActiveChar();
        if (_activeChar == null) return;
        PartyMatchRoom _room = PartyMatchRoomList.getInstance().getRoom(_roomid);
        if (_room == null) return;
        if ((_activeChar.isInParty() && _room.getOwner().isInParty()) && (_activeChar.getParty().getPartyLeaderOID() == _room.getOwner().getParty().getPartyLeaderOID())) {
            _activeChar.broadcastUserInfo();
        } else {
            _room.deleteMember(_activeChar);
            _activeChar.setPartyRoom(0);
            _activeChar.sendPacket(new ExClosePartyRoom());
            _activeChar.sendPacket(new SystemMessage(SystemMessageId.PARTY_ROOM_EXITED));
        }
    }

    @Override
    public String getType() {
        return "[C] D0:0B RequestWithdrawPartyRoom";
    }
}
