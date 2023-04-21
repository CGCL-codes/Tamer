package quest.convent_of_marchutan;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _29501GainingTheGladiatorsStigma extends QuestHandler {

    private static final int questId = 29501;

    private final int skillId = 234;

    private final int mainNpcId = 204056;

    public _29501GainingTheGladiatorsStigma() {
        super(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (targetId == mainNpcId) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 4762); else return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.REWARD) {
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onSkillUseEvent(QuestCookie env, int skillUsedId) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() != QuestStatus.START) return false;
        if (skillUsedId == skillId) {
            if (var >= 0 && var < 9) {
                qs.setQuestVarById(0, var + 1);
                updateQuestStatus(env);
                return true;
            } else if (var == 9) {
                qs.setQuestVarById(0, var + 1);
                updateQuestStatus(env);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return true;
            }
        }
        return false;
    }

    @Override
    public void register() {
        qe.setNpcQuestData(mainNpcId).addOnQuestStart(questId);
        qe.setNpcQuestData(mainNpcId).addOnTalkEvent(questId);
        qe.setQuestSkillIds(skillId).add(questId);
    }
}
