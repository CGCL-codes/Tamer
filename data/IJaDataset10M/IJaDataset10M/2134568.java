package quest.fenris_fangs_quest;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _4941PandaemoniumHonors extends QuestHandler {

    private static final int questId = 4941;

    public _4941PandaemoniumHonors() {
        super(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        if (defaultQuestNoneDialog(env, 204060, 4762)) return true;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == 204060) {
                switch(env.getDialogId()) {
                    case 26:
                        if (var == 0) return sendQuestDialog(env, 1011); else if (var == 1) return sendQuestDialog(env, 1352);
                    case 10000:
                        return defaultCloseDialog(env, 0, 1);
                    case 10001:
                        return defaultCloseDialog(env, 1, 0);
                    case 34:
                        return defaultQuestItemCheck(env, 1, 0, true, 5, 10001);
                }
            }
        }
        return defaultQuestRewardDialog(env, 204060, 2375);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204060).addOnQuestStart(questId);
        qe.setNpcQuestData(204060).addOnTalkEvent(questId);
    }
}
