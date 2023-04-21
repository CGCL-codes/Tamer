package quest.inggison;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import java.util.Collections;

public class _11267Sword_Predilection extends QuestHandler {

    private static final int questId = 11267;

    public _11267Sword_Predilection() {
        super(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 799038) {
                switch(env.getDialogId()) {
                    case 26:
                        return sendQuestDialog(env, 1011);
                    case 1007:
                        return sendQuestDialog(env, 4);
                    case 1002:
                        if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182206886, 1)))) ;
                        if (QuestService.startQuest(env, QuestStatus.START)) return sendQuestDialog(env, 1003); else return false;
                    case 1003:
                        return sendQuestDialog(env, 1004);
                }
            }
        }
        if (qs == null || qs.getStatus() == QuestStatus.COMPLETE) {
            if (targetId == 798316) {
                if (env.getDialogId() == 26) {
                    return sendQuestDialog(env, 1352);
                } else return defaultQuestStartDialog(env);
            }
        }
        if (qs.getStatus() == QuestStatus.START) {
            switch(targetId) {
                case 798316:
                    switch(env.getDialogId()) {
                        case 26:
                            return sendQuestDialog(env, 2375);
                        case 2034:
                            return sendQuestDialog(env, 2034);
                        case 34:
                            if (QuestService.collectItemCheck(env, true)) {
                                player.getInventory().removeFromBagByItemId(182206886, 1);
                                player.getInventory().removeFromBagByItemId(100000940, 1);
                                player.getInventory().removeFromBagByItemId(182400001, 8000000);
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
            if (targetId == 798316) return defaultQuestEndDialog(env);
        }
        return false;
    }

    @Override
    public void register() {
        qe.setNpcQuestData(799038).addOnQuestStart(questId);
        qe.setNpcQuestData(798316).addOnTalkEvent(questId);
    }
}
