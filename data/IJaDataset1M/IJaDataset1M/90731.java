package quest.pandaemonium;

import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

public class _29000EssencetappersTest extends QuestHandler {

    private static final int questId = 29000;

    private static final int[] npc_ids = { 204096, 204097 };

    public _29000EssencetappersTest() {
        super(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        if (defaultQuestNoneDialog(env, 204096, 4762)) return true;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            switch(env.getTargetId()) {
                case 204097:
                    switch(env.getDialogId()) {
                        case 26:
                            if (var == 0) return sendQuestDialog(env, 1011);
                        case 10000:
                            return defaultCloseDialog(env, 0, 1, 122001250, 1, 0, 0);
                    }
                    break;
                case 204096:
                    switch(env.getDialogId()) {
                        case 26:
                            if (var == 1) return sendQuestDialog(env, 1352);
                        case 34:
                            return defaultQuestItemCheck(env, 1, 2, true, 5, 10001);
                    }
                    break;
            }
        }
        return defaultQuestRewardDialog(env, 204096, 0);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204096).addOnQuestStart(questId);
        for (int npc_id : npc_ids) qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }
}
