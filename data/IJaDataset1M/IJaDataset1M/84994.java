package com.l2jserver.gameserver.model.actor.instance;

import java.util.StringTokenizer;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.templates.chars.L2NpcTemplate;

public class L2FortEnvoyInstance extends L2Npc {

    public L2FortEnvoyInstance(int objectID, L2NpcTemplate template) {
        super(objectID, template);
        setInstanceType(InstanceType.L2FortEnvoyInstance);
    }

    @Override
    public void showChatWindow(L2PcInstance player) {
        player.sendPacket(ActionFailed.STATIC_PACKET);
        String filename;
        if (!player.isClanLeader() || player.getClan() == null || getFort().getFortId() != player.getClan().getHasFort()) filename = "data/html/fortress/envoy-noclan.htm"; else if (getFort().getFortState() == 0) filename = "data/html/fortress/envoy.htm"; else filename = "data/html/fortress/envoy-no.htm";
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile(player.getHtmlPrefix(), filename);
        html.replace("%objectId%", String.valueOf(getObjectId()));
        html.replace("%castleName%", String.valueOf(CastleManager.getInstance().getCastleById(getFort().getCastleIdFromEnvoy(getNpcId())).getName()));
        player.sendPacket(html);
    }

    @Override
    public void onBypassFeedback(L2PcInstance player, String command) {
        StringTokenizer st = new StringTokenizer(command, " ");
        String actualCommand = st.nextToken();
        String par = "";
        if (st.countTokens() >= 1) par = st.nextToken();
        if (actualCommand.equalsIgnoreCase("select")) {
            int val = 0;
            try {
                val = Integer.parseInt(par);
            } catch (IndexOutOfBoundsException ioobe) {
            } catch (NumberFormatException nfe) {
            }
            int castleId = 0;
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            if (val == 2) {
                castleId = getFort().getCastleIdFromEnvoy(getNpcId());
                if (CastleManager.getInstance().getCastleById(castleId).getOwnerId() < 1) {
                    html.setHtml("<html><body>Contact is currently not possible, " + CastleManager.getInstance().getCastleById(castleId).getName() + " Castle isn't currently owned by clan.</body></html>");
                    player.sendPacket(html);
                    return;
                }
            }
            getFort().setFortState(val, castleId);
            html.setFile(player.getHtmlPrefix(), "data/html/fortress/envoy-ok.htm");
            html.replace("%castleName%", String.valueOf(CastleManager.getInstance().getCastleById(getFort().getCastleIdFromEnvoy(getNpcId())).getName()));
            player.sendPacket(html);
        } else super.onBypassFeedback(player, command);
    }
}
