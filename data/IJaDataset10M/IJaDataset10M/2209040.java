package quest.gelkmaros;

import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

/**
 * @author HellBoy
 */
public class _21033ExorcisingInfisto extends QuestHandler {

    private static final int questId = 21033;

    public _21033ExorcisingInfisto() {
        super(questId);
    }

    @Override
    public void register() {
        int[] npcs = { 799256, 204734 };
        for (int npc : npcs) qe.setNpcQuestData(npc).addOnTalkEvent(questId);
        qe.setNpcQuestData(799256).addOnQuestStart(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        if (defaultQuestNoneDialog(env, 799256, 182207829, 1)) return true;
        QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == 204734) {
                switch(env.getDialogId()) {
                    case 26:
                        if (var == 0) return sendQuestDialog(env, 1352);
                    case 10000:
                        return defaultCloseDialog(env, 0, 1, true, false, 182207830, 1, 182207829, 1);
                }
            }
        }
        return defaultQuestRewardDialog(env, 799256, 2375);
    }
}
