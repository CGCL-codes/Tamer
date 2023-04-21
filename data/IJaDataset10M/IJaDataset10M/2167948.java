package org.rscdaemon.server.packethandler.loginserver;

import org.rscdaemon.server.packethandler.PacketHandler;
import org.rscdaemon.server.packetbuilder.loginserver.PlayerListRequestPacketBuilder;
import org.rscdaemon.server.model.World;
import org.rscdaemon.server.net.Packet;
import org.rscdaemon.server.net.LSPacket;
import org.rscdaemon.server.util.Logger;
import org.apache.mina.common.IoSession;

public class PlayerListRequestHandler implements PacketHandler {

    /**
	 * World instance
	 */
    public static final World world = World.getWorld();

    private PlayerListRequestPacketBuilder builder = new PlayerListRequestPacketBuilder();

    public void handlePacket(Packet p, IoSession session) throws Exception {
        long uID = ((LSPacket) p).getUID();
        Logger.event("LOGIN_SERVER requested player list (uID: " + uID + ")");
        builder.setUID(uID);
        LSPacket temp = builder.getPacket();
        if (temp != null) {
            session.write(temp);
        }
    }
}
