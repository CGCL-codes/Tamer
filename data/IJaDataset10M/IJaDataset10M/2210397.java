package quest.beluslan;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

public class _2611ConsultingtheLeaders extends QuestHandler {

    private static final int questId = 2611;

    public _2611ConsultingtheLeaders() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204763).addOnQuestStart(questId);
        qe.setNpcQuestData(204763).addOnTalkEvent(questId);
        qe.setNpcQuestData(204773).addOnTalkEvent(questId);
        qe.setNpcQuestData(204772).addOnTalkEvent(questId);
        qe.setNpcQuestData(204700).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 204763) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 1011); else return defaultQuestStartDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.START) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 2375); else if (env.getDialogId() == 1009) {
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else return defaultQuestEndDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                return defaultQuestEndDialog(env);
            }
        } else if (targetId == 204773) {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 1352); else if (env.getDialogId() == 10000) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else return defaultQuestStartDialog(env);
            }
        } else if (targetId == 204772) {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 1693); else if (env.getDialogId() == 10001) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else return defaultQuestStartDialog(env);
            }
        } else if (targetId == 204700) {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 2034); else if (env.getDialogId() == 10002) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else return defaultQuestStartDialog(env);
            }
        } else if (targetId == 204763) {
            if (qs != null) {
                if (env.getDialogId() == 26 && qs.getStatus() == QuestStatus.START) return sendQuestDialog(env, 2375); else if (env.getDialogId() == 1009) {
                    qs.setQuestVar(3);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return defaultQuestEndDialog(env);
                } else return defaultQuestEndDialog(env);
            }
        }
        return false;
    }
}
