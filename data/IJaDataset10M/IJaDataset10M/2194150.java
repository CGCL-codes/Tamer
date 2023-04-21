package quest.fenris_fangs_quest;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _4937RecognitionOfThePreceptors extends QuestHandler {

    private static final int questId = 4937;

    private static final int[] npcs = { 204053, 204059, 204058, 204056, 204057, 204075 };

    public _4937RecognitionOfThePreceptors() {
        super(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        if (defaultQuestNoneDialog(env, 204053, 4762, 182207112, 1)) return true;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            switch(env.getTargetId()) {
                case 204059:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 0) return sendQuestDialog(env, 1011);
                            case 10000:
                                return defaultCloseDialog(env, 0, 1);
                        }
                    }
                    break;
                case 204058:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 1) return sendQuestDialog(env, 1352);
                            case 10001:
                                return defaultCloseDialog(env, 1, 2);
                        }
                    }
                    break;
                case 204057:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 2) return sendQuestDialog(env, 1693);
                            case 10002:
                                return defaultCloseDialog(env, 2, 3);
                        }
                    }
                    break;
                case 204056:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 3) return sendQuestDialog(env, 2034);
                            case 10003:
                                return defaultCloseDialog(env, 3, 4, 182207113, 1, 182207112, 1);
                        }
                    }
                    break;
                case 204075:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 4) return sendQuestDialog(env, 2375);
                            case 10255:
                                if (player.getInventory().getItemCountByItemId(186000084) >= 1) return defaultCloseDialog(env, 4, 0, true, false, 0, 0, 186000084, 1); else return sendQuestDialog(env, 2461);
                        }
                    }
                    break;
            }
        }
        return defaultQuestRewardDialog(env, 204053, 10002);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204053).addOnQuestStart(questId);
        for (int npc : npcs) qe.setNpcQuestData(npc).addOnTalkEvent(questId);
    }
}
