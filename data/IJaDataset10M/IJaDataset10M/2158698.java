package quest.radiant_ops;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;

public class _35508ZeroTolerancePolicy extends QuestHandler {

    private static final int questId = 35508;

    public _35508ZeroTolerancePolicy() {
        super(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (defaultQuestStartDaily(env)) return true;
        if (qs == null) return false;
        return defaultQuestRewardDialog(env, 799831, 10002);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillPlayerEvent(env, 1, 0, 4, false) || defaultQuestOnKillPlayerEvent(env, 1, 4, 5, true)) return true; else return false;
    }

    @Override
    public void register() {
        qe.setNpcQuestData(799831).addOnTalkEvent(questId);
        qe.addOnKillPlayer(questId);
    }
}
