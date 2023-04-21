package org.dreamspeak.lib.protocol.packets.inbound.reliablecontent;

import java.nio.ByteBuffer;
import org.dreamspeak.lib.data.Player;
import org.dreamspeak.lib.data.PlayerList;
import org.dreamspeak.lib.data.PlayerChannelPrivilegeSet.ChannelPrivilege;
import org.dreamspeak.lib.data.PlayerServerPrivilegeSet.ServerPrivilege;
import org.dreamspeak.lib.protocol.ProtocolException;
import org.dreamspeak.lib.protocol.packets.ResponsibleForPacketTypes;

/**
 * TODO: Proper documentation
 * 
 * @author avithan
 */
@ResponsibleForPacketTypes({ 0x006A, 0x006B })
public class PlayerPrivilegeUpdate extends PlayerListUpdate {

    static final byte OPERATION_PERMIT = 0x00;

    static final byte OPERATION_REVOKE = 0x02;

    public PlayerPrivilegeUpdate(short packetType, ByteBuffer data) throws ProtocolException {
        super(packetType, data);
        playerId = data.getInt(0x00);
        operationType = data.get(0x04);
        updatingPlayerId = data.getInt(0x06);
    }

    final int updatingPlayerId;

    final int playerId;

    final byte operationType;

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public void processUpdate(PlayerList playerList) throws ProtocolException {
        Player p = playerList.get(getPlayerId());
        if (p == null) {
            throw new ProtocolException("Recieved PlayerPrivilegeUpdate for unknown Player with id " + getPlayerId());
        }
        boolean set;
        if (operationType == OPERATION_PERMIT) {
            set = true;
        } else if (operationType == OPERATION_REVOKE) {
            set = false;
        } else {
            throw new ProtocolException("Unknown OperationType " + operationType + " in PlayerPrivilegeUpdate");
        }
        switch(getPacketType()) {
            case 0x6A:
                if (set) {
                    p.getChannelPrivileges().add(ChannelPrivilege.ChannelAdministrator);
                } else {
                    p.getChannelPrivileges().remove(ChannelPrivilege.ChannelAdministrator);
                }
                break;
            case 0x6B:
                if (set) {
                    p.getServerPrivileges().add(ServerPrivilege.ServerAdministrator);
                } else {
                    p.getServerPrivileges().remove(ServerPrivilege.ServerAdministrator);
                }
                break;
        }
    }
}
