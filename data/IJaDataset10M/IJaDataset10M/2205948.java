package quest.verteron;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import java.util.Collections;

public class _1198TheWritingontheWall extends QuestHandler {

    private static final int questId = 1198;

    public _1198TheWritingontheWall() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(700009).addOnTalkEvent(questId);
        qe.setNpcQuestData(203098).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (targetId == 0) {
            if (env.getDialogId() == 1002) {
                QuestService.startQuest(env, QuestStatus.START);
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
                return true;
            }
        } else if (targetId == 700009) {
            if ((qs == null || qs.getStatus() == QuestStatus.NONE)) {
                if (player.getInventory().getItemCountByItemId(182200559) == 0) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182200559, 1)))) ((Npc) player.getTarget()).getController().onDespawn(false);
                }
            }
            return true;
        } else if (targetId == 203098) {
            if (qs != null) {
                if (env.getDialogId() == 26 && qs.getStatus() == QuestStatus.START) {
                    return sendQuestDialog(env, 2375);
                } else if (env.getDialogId() == 1009) {
                    player.getInventory().removeFromBagByItemId(182200559, 1);
                    qs.setQuestVar(1);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return defaultQuestEndDialog(env);
                } else return defaultQuestEndDialog(env);
            }
        }
        return false;
    }
}
