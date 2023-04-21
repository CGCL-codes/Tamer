package quest.gelkmaros;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

public class _30301TrackingSupplies extends QuestHandler {

    private static final int questId = 30301;

    public _30301TrackingSupplies() {
        super(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = 0;
        if (player.getCommonData().getLevel() < 55) return false;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (targetId == 799225) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 1011); else return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.START) {
                long itemCount;
                if (env.getDialogId() == 26 && qs.getQuestVarById(0) == 0) {
                    return sendQuestDialog(env, 2375);
                } else if (env.getDialogId() == 33 && qs.getQuestVarById(0) == 0) {
                    itemCount = player.getInventory().getItemCountByItemId(182209701);
                    if (itemCount > 0) {
                        player.getInventory().removeFromBagByItemId(182209701, 1);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                    } else {
                        return sendQuestDialog(env, 2716);
                    }
                } else return defaultQuestEndDialog(env);
            } else {
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public void register() {
        qe.setNpcQuestData(799225).addOnQuestStart(questId);
        qe.setNpcQuestData(799225).addOnTalkEvent(questId);
    }
}
