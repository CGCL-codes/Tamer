package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import org.apache.log4j.Logger;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.teleport.TeleporterTemplate;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.World;

/**
 * 
 * @author alexa026 , orz
 * 
 */
public class SM_TELEPORT_MAP extends AionServerPacket {

    private int targetObjectId;

    private Player player;

    private TeleporterTemplate teleport;

    public Npc npc;

    private static final Logger log = Logger.getLogger(SM_TELEPORT_MAP.class);

    public SM_TELEPORT_MAP(Player player, int targetObjectId, TeleporterTemplate teleport) {
        this.player = player;
        this.targetObjectId = targetObjectId;
        this.npc = (Npc) World.getInstance().findAionObject(targetObjectId);
        this.teleport = teleport;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        if ((teleport != null) && (teleport.getNpcId() != 0) && (teleport.getTeleportId() != 0)) {
            writeD(buf, targetObjectId);
            writeH(buf, teleport.getTeleportId());
        } else {
            PacketSendUtility.sendMessage(player, "Missing info at npc_teleporter.xml with npcid: " + npc.getNpcId());
            log.info(String.format("Missing teleport info with npcid: %d", npc.getNpcId()));
        }
    }
}
