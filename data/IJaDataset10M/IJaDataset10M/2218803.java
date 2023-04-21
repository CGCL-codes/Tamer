package org.rscdaemon.server.packethandler.loginserver;

import org.rscdaemon.server.packethandler.PacketHandler;
import org.rscdaemon.server.packetbuilder.loginserver.ReportInfoRequestPacketBuilder;
import org.rscdaemon.server.model.World;
import org.rscdaemon.server.model.Player;
import org.rscdaemon.server.net.Packet;
import org.rscdaemon.server.net.LSPacket;
import org.rscdaemon.server.util.Logger;
import org.apache.mina.common.IoSession;

public class ReportInfoRequestHandler implements PacketHandler {

    /**
	 * World instance
	 */
    public static final World world = World.getWorld();

    private ReportInfoRequestPacketBuilder builder = new ReportInfoRequestPacketBuilder();

    public void handlePacket(Packet p, IoSession session) throws Exception {
        long uID = ((LSPacket) p).getUID();
        Logger.event("LOGIN_SERVER requested report information (uID: " + uID + ")");
        Player player = world.getPlayer(p.readLong());
        if (player == null) {
            return;
        }
        builder.setUID(uID);
        builder.setPlayer(player);
        LSPacket temp = builder.getPacket();
        if (temp != null) {
            session.write(temp);
        }
    }
}
