package quest.verteron;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.QuestsData;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.QuestTemplate;
import org.openaion.gameserver.model.templates.bonus.AbstractInventoryBonus;
import org.openaion.gameserver.model.templates.bonus.CoinBonus;
import org.openaion.gameserver.model.templates.bonus.InventoryBonusType;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;

/**
 * @author Orpheo
 */
public class _1209IronWarriorAndScout extends QuestHandler {

    private static final int questId = 1209;

    static QuestsData questsData = DataManager.QUEST_DATA;

    public _1209IronWarriorAndScout() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203179).addOnQuestStart(questId);
        qe.setNpcQuestData(203179).addOnTalkEvent(questId);
        qe.addOnQuestFinish(questId);
        qe.setQuestBonusType(InventoryBonusType.COIN).add(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestTemplate template = questsData.getQuestById(questId);
        if (targetId == 203179) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 2) {
                    PlayerClass playerClass = player.getCommonData().getPlayerClass();
                    PlayerClass startPC = null;
                    try {
                        startPC = PlayerClass.getStartingClassFor(playerClass);
                    } catch (IllegalArgumentException e) {
                        startPC = playerClass;
                    }
                    if (startPC == PlayerClass.SCOUT || startPC == PlayerClass.WARRIOR) {
                        QuestService.startQuest(env, QuestStatus.START);
                        return sendQuestDialog(env, 1011);
                    } else {
                        return sendQuestDialog(env, 3739);
                    }
                }
            } else if (qs != null && qs.getStatus() == QuestStatus.START) {
                if (env.getDialogId() == 2) {
                    return sendQuestDialog(env, 1011);
                } else if (env.getDialogId() == 1011) {
                    if (player.getInventory().getItemCountByItemId(186000001) >= 2) {
                        qs.setQuestVarById(0, 0);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                    } else {
                        return sendQuestDialog(env, 1009);
                    }
                } else if (env.getDialogId() == 1352) {
                    if (player.getInventory().getItemCountByItemId(186000001) >= 4) {
                        qs.setQuestVarById(0, 1);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 6);
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
                return defaultQuestEndDialog(env, qs.getQuestVarById(0));
            }
        }
        return false;
    }

    @Override
    public HandlerResult onBonusApplyEvent(QuestCookie env, int index, AbstractInventoryBonus bonus) {
        if (!(bonus instanceof CoinBonus)) return HandlerResult.UNKNOWN;
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
            if (index == 0 && qs.getQuestVarById(0) == 0 || index == 1 && qs.getQuestVarById(0) == 1) return HandlerResult.SUCCESS;
        }
        return HandlerResult.FAILED;
    }
}
