package quest.miragent_holy_templar_quest;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _3933ClassPreceptorsConsent extends QuestHandler {

    private static final int questId = 3933;

    private static final int[] npcs = { 203701, 203704, 203705, 203706, 203707, 203752 };

    public _3933ClassPreceptorsConsent() {
        super(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        if (defaultQuestNoneDialog(env, 203701, 4762, 182206086, 1)) return true;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            switch(env.getTargetId()) {
                case 203704:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 0) return sendQuestDialog(env, 1011);
                            case 10000:
                                return defaultCloseDialog(env, 0, 1);
                        }
                    }
                    break;
                case 203705:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 1) return sendQuestDialog(env, 1352);
                            case 10001:
                                return defaultCloseDialog(env, 1, 2);
                        }
                    }
                    break;
                case 203706:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 2) return sendQuestDialog(env, 1693);
                            case 10002:
                                return defaultCloseDialog(env, 2, 3);
                        }
                    }
                    break;
                case 203707:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 3) return sendQuestDialog(env, 2034);
                            case 10003:
                                return defaultCloseDialog(env, 3, 4, 182206087, 1, 182206086, 1);
                        }
                    }
                    break;
                case 203752:
                    {
                        switch(env.getDialogId()) {
                            case 26:
                                if (var == 4) return sendQuestDialog(env, 2375);
                            case 10255:
                                if (player.getInventory().getItemCountByItemId(186000080) >= 1) return defaultCloseDialog(env, 4, 0, true, false, 0, 0, 186000080, 1); else return sendQuestDialog(env, 2461);
                        }
                    }
                    break;
            }
        }
        return defaultQuestRewardDialog(env, 203701, 10002);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203701).addOnQuestStart(questId);
        for (int npc : npcs) qe.setNpcQuestData(npc).addOnTalkEvent(questId);
    }
}
