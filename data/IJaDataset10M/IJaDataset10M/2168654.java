package quest.radiant_ops;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;

public class _35505OperationGelkmaros extends QuestHandler {

    private static final int questId = 35505;

    public _35505OperationGelkmaros() {
        super(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (defaultQuestStartDaily(env)) return true;
        if (qs == null) return false;
        if (defaultQuestRewardDialog(env, 799825, 10002) || defaultQuestRewardDialog(env, 799826, 10002)) return true; else return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        Player player = env.getPlayer();
        if (player.getWorldId() == 220070000) {
            if (defaultQuestOnKillPlayerEvent(env, 1, 0, 4, false) || defaultQuestOnKillPlayerEvent(env, 1, 4, 5, true)) return true;
        }
        return false;
    }

    @Override
    public void register() {
        qe.setNpcQuestData(799825).addOnTalkEvent(questId);
        qe.setNpcQuestData(799826).addOnTalkEvent(questId);
        qe.addOnKillPlayer(questId);
    }
}
