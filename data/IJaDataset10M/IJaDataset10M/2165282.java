package quest.heiron;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

/**
 * @author Atomics
 */
public class _1548KlawControl extends QuestHandler {

    private static final int questId = 1548;

    public _1548KlawControl() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204583).addOnQuestStart(questId);
        qe.setNpcQuestData(204583).addOnTalkEvent(questId);
        qe.setNpcQuestData(700209).addOnKillEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 204583) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 1011); else return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.REWARD) {
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 700209, 0, 4)) return true;
        if (defaultQuestOnKillEvent(env, 700209, 4, 5)) {
            Player player = env.getPlayer();
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            qs.setStatus(QuestStatus.REWARD);
            updateQuestStatus(env);
            return true;
        } else return false;
    }
}
