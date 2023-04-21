package net.sf.odinms.net.channel.handler;

import net.sf.odinms.client.MapleClient;
import net.sf.odinms.net.AbstractMaplePacketHandler;
import net.sf.odinms.scripting.npc.NPCScriptManager;
import net.sf.odinms.server.life.MapleNPC;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.data.input.SeekableLittleEndianAccessor;

public class NPCTalkHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int oid = slea.readInt();
        slea.readInt();
        MapleNPC npc = (MapleNPC) c.getPlayer().getMap().getMapObject(oid);
        if (npc.hasShop()) {
            npc.sendShop(c);
        } else {
            if (c.getCM() != null || c.getQM() != null) {
                c.getSession().write(MaplePacketCreator.enableActions());
                return;
            }
            NPCScriptManager.getInstance().start(c, npc.getId(), null, null);
        }
    }
}
