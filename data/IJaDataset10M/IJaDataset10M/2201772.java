package quest.morheim;

import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

/**
 * @author XRONOS
 *
 */
public class _2488PretorsInBeluslan extends QuestHandler {

    private static final int questId = 2488;

    private static final int[] npcs = { 204053, 204208, 204702 };

    public _2488PretorsInBeluslan() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204053).addOnQuestStart(questId);
        for (int npc : npcs) qe.setNpcQuestData(npc).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (defaultQuestNoneDialog(env, 204053)) return true;
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == 204208) {
                switch(env.getDialogId()) {
                    case 26:
                        if (var == 0) return sendQuestDialog(env, 1352);
                    case 10000:
                        return defaultCloseDialog(env, 0, 1, true, false);
                }
            }
        }
        return defaultQuestRewardDialog(env, 204702, 2375);
    }
}
