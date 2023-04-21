package quest.master_craft;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _29020TailorsPotential extends QuestHandler {

    private static final int questId = 29020;

    private static final int questStartNpc = 204110;

    private static final int secondNpc = 204111;

    private static final int[] recipesItemIds = { 152206964, 152206965 };

    private static final int[] recipesIds = { 155007002, 155007003 };

    public _29020TailorsPotential() {
        super(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        if (defaultQuestNoneDialog(env, questStartNpc, 4762)) return true;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            switch(env.getTargetId()) {
                case secondNpc:
                    switch(env.getDialogId()) {
                        case 26:
                            if (var == 0) return sendQuestDialog(env, 1011); else if (var == 2) return sendQuestDialog(env, 4080);
                        case 10009:
                            if (player.getInventory().decreaseKinah(84000)) return defaultCloseDialog(env, var, 1, recipesItemIds[0], 1, 0, 0); else return sendQuestDialog(env, 4400);
                        case 10019:
                            if (player.getInventory().decreaseKinah(111500)) return defaultCloseDialog(env, var, 1, recipesItemIds[1], 1, 0, 0); else return sendQuestDialog(env, 4400);
                    }
                    break;
                case questStartNpc:
                    switch(env.getDialogId()) {
                        case 26:
                            if (var == 1) return sendQuestDialog(env, 1352);
                        case 34:
                            if (var == 1) {
                                if (QuestService.collectItemCheck(env, true)) {
                                    qs.setStatus(QuestStatus.REWARD);
                                    updateQuestStatus(env);
                                    return sendQuestDialog(env, 5);
                                } else {
                                    int checkFailId = 3398;
                                    if (player.getRecipeList().isRecipePresent(recipesIds[0]) || player.getRecipeList().isRecipePresent(recipesIds[1])) checkFailId = 2716; else if (player.getInventory().getItemCountByItemId(recipesItemIds[0]) > 0 || player.getInventory().getItemCountByItemId(recipesItemIds[1]) > 0) checkFailId = 3057;
                                    if (checkFailId == 3398) {
                                        qs.setQuestVar(2);
                                        updateQuestStatus(env);
                                    }
                                    return sendQuestDialog(env, checkFailId);
                                }
                            }
                            break;
                    }
                    break;
            }
        }
        return defaultQuestRewardDialog(env, questStartNpc, 0);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(questStartNpc).addOnQuestStart(questId);
        qe.setNpcQuestData(questStartNpc).addOnTalkEvent(questId);
        qe.setNpcQuestData(secondNpc).addOnTalkEvent(questId);
    }
}
