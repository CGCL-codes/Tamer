package quest.kromedes_trial;

import gameserver.dataholders.DataManager;
import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

public class _18622KaligasCollection extends QuestHandler {

    private static final int questId = 18622;

    public _18622KaligasCollection() {
        super(questId);
    }

    @Override
    public boolean onActionItemEvent(QuestCookie env) {
        if (env.getPlayer().getTribe().equals("PC")) {
            int targetId = 0;
            if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
            return (targetId == 730330);
        } else {
            return false;
        }
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        if (env.getPlayer().getTribe().equals("PC")) {
            final Player player = env.getPlayer();
            int targetId = 0;
            if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
            QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            if (targetId == 730330) {
                PlayerClass playerClass = player.getCommonData().getPlayerClass();
                if (playerClass == PlayerClass.RANGER || playerClass == PlayerClass.ASSASSIN || playerClass == PlayerClass.MAGE || playerClass == PlayerClass.PRIEST || playerClass == PlayerClass.GLADIATOR || playerClass == PlayerClass.WARRIOR || playerClass == PlayerClass.SCOUT) {
                    if (qs == null || qs.getStatus() == QuestStatus.NONE || (qs.getStatus() == QuestStatus.COMPLETE && (qs.getCompleteCount() <= template.getMaxRepeatCount()))) {
                        if (env.getDialogId() == -1) return sendQuestDialog(env, 1011); else return defaultQuestStartDialog(env);
                    } else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                        if (env.getDialogId() == -1) return sendQuestDialog(env, 2375); else if (env.getDialogId() == 34) {
                            if (player.getInventory().getItemCountByItemId(185000102) >= 1) {
                                player.getInventory().removeFromBagByItemId(185000102, 1);
                                qs.setStatus(QuestStatus.REWARD);
                                qs.setQuestVar(1);
                                qs.setCompliteCount(0);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else return sendQuestDialog(env, 2716);
                        }
                    } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                        int var = qs.getQuestVarById(0);
                        switch(env.getDialogId()) {
                            case -1:
                                if (var == 1) return sendQuestDialog(env, 5);
                            case 17:
                                QuestService.questFinish(env, qs.getQuestVars().getQuestVars() - 1);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                }
                return false;
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void register() {
        qe.setNpcQuestData(730330).addOnQuestStart(questId);
        qe.setNpcQuestData(730330).addOnActionItemEvent(questId);
        qe.setNpcQuestData(730330).addOnTalkEvent(questId);
    }
}
