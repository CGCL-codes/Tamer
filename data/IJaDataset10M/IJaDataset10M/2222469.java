package quest.theobomos;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.QuestsData;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.QuestTemplate;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;

/**
 * @author Orpheo
 */
public class _3068PlatinumWarrior48 extends QuestHandler {

    static QuestsData questsData = DataManager.QUEST_DATA;

    private static final int questId = 3068;

    public _3068PlatinumWarrior48() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(798172).addOnQuestStart(questId);
        qe.setNpcQuestData(798172).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        if (player.getLevel() <= 46) return false;
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestTemplate template = questsData.getQuestById(questId);
        if (targetId == 798172) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 2) {
                    PlayerClass playerClass = player.getCommonData().getPlayerClass();
                    if (playerClass == PlayerClass.GLADIATOR || playerClass == PlayerClass.TEMPLAR || playerClass == PlayerClass.WARRIOR) {
                        QuestService.startQuest(env, QuestStatus.START);
                        return sendQuestDialog(env, 1011);
                    } else {
                        return sendQuestDialog(env, 3739);
                    }
                }
            } else if (qs != null && qs.getStatus() == QuestStatus.START) {
                if (env.getDialogId() == 2) {
                    return sendQuestDialog(env, 1011);
                } else if (env.getDialogId() == 10000) {
                    if (player.getInventory().getItemCountByItemId(186000005) >= 2000) {
                        qs.setQuestVarById(1, 0);
                        player.getInventory().removeFromBagByItemId(186000005, 2000);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                    } else {
                        return sendQuestDialog(env, 1009);
                    }
                } else if (env.getDialogId() == 10001) {
                    if (player.getInventory().getItemCountByItemId(186000005) >= 400) {
                        qs.setQuestVarById(1, 1);
                        player.getInventory().removeFromBagByItemId(186000005, 400);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 6);
                    } else {
                        return sendQuestDialog(env, 1009);
                    }
                } else if (env.getDialogId() == 10002) {
                    if (player.getInventory().getItemCountByItemId(186000005) >= 1000) {
                        qs.setQuestVarById(1, 2);
                        player.getInventory().removeFromBagByItemId(186000005, 1000);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 7);
                    } else {
                        return sendQuestDialog(env, 1009);
                    }
                } else if (env.getDialogId() == 10003) {
                    if (player.getInventory().getItemCountByItemId(186000005) >= 200) {
                        qs.setQuestVarById(1, 3);
                        player.getInventory().removeFromBagByItemId(186000005, 200);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 8);
                    } else {
                        return sendQuestDialog(env, 1009);
                    }
                }
            } else if (qs.getStatus() == QuestStatus.COMPLETE) {
                if (env.getDialogId() == 2) {
                    if ((qs.getCompleteCount() <= template.getMaxRepeatCount())) {
                        QuestService.startQuest(env, QuestStatus.START);
                        return sendQuestDialog(env, 1011);
                    } else return sendQuestDialog(env, 1008);
                }
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                return defaultQuestEndDialog(env, qs.getQuestVarById(1));
            }
        }
        return false;
    }
}
