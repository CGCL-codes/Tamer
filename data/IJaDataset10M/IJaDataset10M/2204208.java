package quest.verteron;

import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

public class _1192VerteronReinforcements extends QuestHandler {

    private static final int questId = 1192;

    public _1192VerteronReinforcements() {
        super(questId);
    }

    @Override
    public void register() {
        int npcs[] = { 203098, 203701, 203833 };
        qe.setNpcQuestData(203098).addOnQuestStart(questId);
        for (int id : npcs) qe.setNpcQuestData(id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        if (defaultQuestNoneDialog(env, 203098)) return true;
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            switch(env.getTargetId()) {
                case 203701:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 0) return sendQuestDialog(env, 1352);
                            case 10000:
                                return defaultCloseDialog(env, 0, 1);
                        }
                    }
                case 203833:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 1) return sendQuestDialog(env, 1693);
                            case 10001:
                                return defaultCloseDialog(env, 1, 2);
                        }
                    }
                case 203098:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 2) return sendQuestDialog(env, 2375);
                            case 1009:
                                return defaultCloseDialog(env, 2, 3, true, true);
                        }
                    }
            }
        }
        return defaultQuestRewardDialog(env, 203098, 0);
    }
}
