package quest.daily;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;

/**
 * @author HellBoy
 *
 */
public class _36510StraightenOuttheTwister extends QuestHandler {

    private static final int questId = 36510;

    public _36510StraightenOuttheTwister() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(700761).addOnTalkEvent(questId);
        qe.setNpcQuestData(799837).addOnTalkEvent(questId);
        qe.setNpcQuestData(799838).addOnTalkEvent(questId);
        qe.setNpcQuestData(216614).addOnKillEvent(questId);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        return defaultQuestOnKillEvent(env, 216614, 0, true);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (defaultQuestStartDaily(env)) return true;
        if (qs == null) return false;
        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == 700761) return defaultQuestUseNpc(env, 0, 1, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true);
        }
        if (defaultQuestRewardDialog(env, 799837, 10002) || defaultQuestRewardDialog(env, 799838, 10002)) return true; else return false;
    }

    @Override
    public void QuestUseNpcInsideFunction(QuestCookie env) {
        Player player = env.getPlayer();
        VisibleObject vO = env.getVisibleObject();
        if (vO instanceof Npc) {
            Npc trap = (Npc) vO;
            if (trap.getNpcId() == 700761) QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 216614, trap.getX(), trap.getY(), trap.getZ(), (byte) 0, true);
        }
    }
}
