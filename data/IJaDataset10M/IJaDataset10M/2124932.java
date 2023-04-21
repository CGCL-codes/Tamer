package quest.gelkmaros;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.model.templates.bonus.AbstractInventoryBonus;
import gameserver.model.templates.bonus.InventoryBonusType;
import gameserver.model.templates.bonus.MedalBonus;
import gameserver.questEngine.HandlerResult;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;

/**
 * @author Rolandas
 *
 */
public class _21050GelkmarosCoinFountain extends QuestHandler {

    private static final int questId = 21050;

    public _21050GelkmarosCoinFountain() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(730242).addOnQuestStart(questId);
        qe.setNpcQuestData(730242).addOnTalkEvent(questId);
        qe.setNpcQuestData(730242).addOnActionItemEvent(questId);
        qe.setQuestBonusType(InventoryBonusType.MEDAL).add(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        if (player.getLevel() < 50 || player.getWorldId() != 220070000) return false;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestTemplate template = DataManager.QUEST_DATA.getQuestById(env.getQuestId());
        if (qs == null || qs.getStatus() == QuestStatus.NONE || (qs.getCompleteCount() <= template.getMaxRepeatCount())) {
            if (env.getTargetId() == 730242) {
                switch(env.getDialogId()) {
                    case -1:
                        if (player.getCommonData().getLevel() >= 50) return sendQuestDialog(env, 1011); else return true;
                    case 10000:
                        if (player.getInventory().getItemCountByItemId(186000030) > 0) {
                            if (qs == null) {
                                qs = new QuestState(questId, QuestStatus.REWARD, 0, 0);
                                player.getQuestStateList().addQuest(questId, qs);
                            } else qs.setStatus(QuestStatus.REWARD);
                            return sendQuestDialog(env, 5);
                        } else return true;
                }
            }
        }
        if (qs == null) return false;
        if (qs.getStatus() == QuestStatus.REWARD && env.getTargetId() == 730242) {
            if (env.getDialogId() == 18) {
                if (player.getInventory().getItemCountByItemId(186000030) > 0 && QuestService.questFinish(env, 0)) {
                    sendQuestDialog(env, 1008);
                    return true;
                }
            }
            return sendQuestDialog(env, 5);
        }
        return false;
    }

    @Override
    public boolean onActionItemEvent(QuestCookie env) {
        return (env.getTargetId() == 730242);
    }

    @Override
    public HandlerResult onBonusApplyEvent(QuestCookie env, int index, AbstractInventoryBonus bonus) {
        if (!(bonus instanceof MedalBonus)) return HandlerResult.UNKNOWN;
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
            if (index == 0) return HandlerResult.SUCCESS;
        }
        return HandlerResult.FAILED;
    }
}
