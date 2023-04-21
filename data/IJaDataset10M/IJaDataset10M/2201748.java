package quest.eltnen;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

/**
 * @author Atomics
 */
public class _1376AMountaineOfTrouble extends QuestHandler {

    private static final int questId = 1376;

    public _1376AMountaineOfTrouble() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203947).addOnQuestStart(questId);
        qe.setNpcQuestData(203947).addOnTalkEvent(questId);
        qe.setNpcQuestData(203964).addOnTalkEvent(questId);
        qe.setNpcQuestData(210976).addOnKillEvent(questId);
        qe.setNpcQuestData(210986).addOnKillEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203947) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 1011); else return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203964) {
                if (env.getDialogId() == -1) return sendQuestDialog(env, 1352);
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        int[] mobs = { 210976, 210986 };
        if (defaultQuestOnKillEvent(env, mobs, 0, 6) || defaultQuestOnKillEvent(env, mobs, 6, true)) return true; else return false;
    }
}
