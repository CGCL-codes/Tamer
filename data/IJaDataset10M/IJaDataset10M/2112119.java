package com.l2jserver.gameserver.model.actor.instance;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.templates.chars.L2NpcTemplate;

/**
 * @author NightMarez
 * @version $Revision: 1.3.2.2.2.5 $ $Date: 2005/03/27 15:29:32 $
 */
public final class L2ObservationInstance extends L2Npc {

    public L2ObservationInstance(int objectId, L2NpcTemplate template) {
        super(objectId, template);
        setInstanceType(InstanceType.L2ObservationInstance);
    }

    @Override
    public void showChatWindow(L2PcInstance player, int val) {
        String filename = null;
        if (isInsideRadius(-79884, 86529, 50, true) || isInsideRadius(-78858, 111358, 50, true) || isInsideRadius(-76973, 87136, 50, true) || isInsideRadius(-75850, 111968, 50, true)) {
            if (val == 0) filename = "data/html/observation/" + getNpcId() + "-Oracle.htm"; else filename = "data/html/observation/" + getNpcId() + "-Oracle-" + val + ".htm";
        } else {
            if (val == 0) filename = "data/html/observation/" + getNpcId() + ".htm"; else filename = "data/html/observation/" + getNpcId() + "-" + val + ".htm";
        }
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile(player.getHtmlPrefix(), filename);
        html.replace("%objectId%", String.valueOf(getObjectId()));
        player.sendPacket(html);
    }
}
