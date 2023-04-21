package quest.eltnen;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

/**
 * @author Rhys2002
 */
public class _1347RaidingKlaw extends QuestHandler {

    private static final int questId = 1347;

    private static final int[] npc_ids = { 203965, 203966 };

    private static final int[] mob_ids = { 210908, 210874, 212137, 212056, 210917 };

    public _1347RaidingKlaw() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203965).addOnQuestStart(questId);
        for (int npc_id : npc_ids) qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
        for (int mob_id : mob_ids) qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 203965) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 1011); else return defaultQuestStartDialog(env);
            }
        }
        if (qs == null) return false;
        if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 203966) {
                if (env.getDialogId() == 26) {
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return sendQuestDialog(env, 1352);
                }
            }
            return false;
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203966) {
                if (env.getDialogId() == 1009) return sendQuestDialog(env, 5); else return defaultQuestEndDialog(env);
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        int[] mobs1 = { 210908, 210874 };
        int[] mobs2 = { 212137, 212056, 210917 };
        if (defaultQuestOnKillEvent(env, mobs1, 0, 15, 0) || defaultQuestOnKillEvent(env, mobs2, 0, 7, 1)) return true; else return false;
    }
}
