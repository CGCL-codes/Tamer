package nakayo.gameserver.controllers;

import nakayo.gameserver.ai.events.Event;
import nakayo.gameserver.dataholders.DataManager;
import nakayo.gameserver.model.gameobjects.Npc;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.model.templates.teleport.TelelocationTemplate;
import nakayo.gameserver.model.templates.teleport.TeleportLocation;
import nakayo.gameserver.model.templates.teleport.TeleporterTemplate;
import nakayo.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import nakayo.gameserver.questEngine.model.QuestState;
import nakayo.gameserver.questEngine.model.QuestStatus;
import nakayo.gameserver.services.TeleportService;
import nakayo.gameserver.utils.PacketSendUtility;
import nakayo.gameserver.utils.exceptionhandlers.exception_enums;

/**
 * @author Dns
 * 
 */
public class RestrictedPortalController extends NpcController {

    @Override
    public void onDialogRequest(final Player player) {
        getOwner().getAi().handleEvent(Event.TALK);
        Npc npc = getOwner();
        if (npc.getNpcId() == exception_enums.NPC_TELEPORT_BALAUREA_ASMO || npc.getNpcId() == exception_enums.NPC_TELEPORT_BALAUREA_ELYOS) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
            return;
        }
    }

    @Override
    public void onDialogSelect(int dialogId, final Player player, int questId) {
        Npc npc = getOwner();
        int targetObjectId = npc.getObjectId();
        if (dialogId == 10000 && (npc.getNpcId() == exception_enums.NPC_TELEPORT_BALAUREA_ASMO || npc.getNpcId() == exception_enums.NPC_TELEPORT_BALAUREA_ELYOS)) {
            int completedquestid = 0;
            switch(player.getCommonData().getRace()) {
                case ASMODIANS:
                    completedquestid = 20001;
                    break;
                case ELYOS:
                    completedquestid = 10001;
                    break;
            }
            QuestState qstel = player.getQuestStateList().getQuestState(completedquestid);
            if (qstel == null || qstel.getStatus() != QuestStatus.COMPLETE) {
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
                return;
            }
            TeleporterTemplate template = DataManager.TELEPORTER_DATA.getTeleporterTemplate(npc.getNpcId());
            if (template != null) {
                TeleportLocation loc = template.getTeleLocIdData().getTelelocations().get(0);
                if (loc != null) {
                    if (!player.getInventory().decreaseKinah(loc.getPrice())) return;
                    TelelocationTemplate tlt = DataManager.TELELOCATION_DATA.getTelelocationTemplate(loc.getLocId());
                    TeleportService.teleportTo(player, tlt.getMapId(), tlt.getX(), tlt.getY(), tlt.getZ(), 1000);
                }
            }
        }
    }
}
