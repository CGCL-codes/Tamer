package quest.reshanta;

import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

/**
 * @author HellBoy
 * 
 */
public class _1800JaiorunerksTombstone extends QuestHandler {

    private static final int questId = 1800;

    public _1800JaiorunerksTombstone() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(279016).addOnQuestStart(questId);
        qe.setNpcQuestData(279016).addOnTalkEvent(questId);
        qe.setNpcQuestData(730141).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        if (defaultQuestNoneDialog(env, 279016, 182202163, 1)) return true;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == 730141) {
                switch(env.getDialogId()) {
                    case -1:
                        if (var == 0) return sendQuestDialog(env, 1352);
                    case 10000:
                        return defaultCloseDialog(env, 0, 1, true, false, 0, 0, 182202163, 1);
                }
            }
        }
        return defaultQuestRewardDialog(env, 279016, 2375);
    }
}
