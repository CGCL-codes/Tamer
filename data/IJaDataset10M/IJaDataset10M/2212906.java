package quest.sanctum;

import java.util.Collections;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Leunam
 *
 */
public class _1935TissueIDontEvenKnowYou extends QuestHandler {

    private static final int questId = 1935;

    private static final int[] npc_ids = { 203833, 203837, 203761 };

    public _1935TissueIDontEvenKnowYou() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203833).addOnQuestStart(questId);
        for (int npc_id : npc_ids) qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 203833) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 1011); else return defaultQuestStartDialog(env);
            }
        }
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203833) {
                if (env.getDialogId() == -1) return sendQuestDialog(env, 2375); else if (env.getDialogId() == 1009) return sendQuestDialog(env, 5); else return defaultQuestEndDialog(env);
            }
        } else if (qs.getStatus() != QuestStatus.START) {
            return false;
        }
        if (targetId == 203837) {
            switch(env.getDialogId()) {
                case 26:
                    if (var == 0) return sendQuestDialog(env, 1352);
                case 10000:
                    if (var == 0) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
            }
        } else if (targetId == 203761) {
            switch(env.getDialogId()) {
                case 26:
                    if (var == 1) return sendQuestDialog(env, 1693);
                case 10001:
                    if (var == 1) {
                        qs.setQuestVarById(0, var + 1);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
            }
        }
        return false;
    }
}
