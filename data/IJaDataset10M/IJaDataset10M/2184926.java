package quest.gelkmaros_armor;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;

public class _21055OrtizsPlan extends QuestHandler {

    private static final int questId = 21055;

    public _21055OrtizsPlan() {
        super(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 799295) {
                if (env.getDialogId() == 26) {
                    return sendQuestDialog(env, 1011);
                } else return defaultQuestStartDialog(env);
            }
        }
        if (qs == null) return false;
        if (qs == null || qs.getStatus() == QuestStatus.COMPLETE) {
            if (targetId == 799295) {
                if (env.getDialogId() == 26) {
                    return sendQuestDialog(env, 1011);
                } else return defaultQuestStartDialog(env);
            }
        }
        if (qs.getStatus() == QuestStatus.START) {
            switch(targetId) {
                case 799295:
                    switch(env.getDialogId()) {
                        case 26:
                            return sendQuestDialog(env, 2375);
                        case 2034:
                            return sendQuestDialog(env, 2034);
                        case 34:
                            if (QuestService.collectItemCheck(env, true)) {
                                player.getInventory().removeFromBagByItemId(182207843, 4);
                                player.getInventory().removeFromBagByItemId(182207844, 20);
                                player.getInventory().removeFromBagByItemId(182400001, 200000);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else {
                                return sendQuestDialog(env, 2716);
                            }
                    }
                    break;
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 799295) return defaultQuestEndDialog(env);
        }
        return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
        if (qs != null || !lvlCheck) return false;
        QuestService.startQuest(env, QuestStatus.START);
        return true;
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(799295).addOnQuestStart(questId);
        qe.setNpcQuestData(799295).addOnTalkEvent(questId);
    }
}
