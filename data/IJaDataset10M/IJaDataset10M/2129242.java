package quest.greater_stigma_quest;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.model.gameobjects.Item;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Fennek
 * 
 */
public class _30217StigmaAndScars extends QuestHandler {

    private static final int questId = 30217;

    public _30217StigmaAndScars() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(798909).addOnQuestStart(questId);
        qe.setNpcQuestData(798941).addOnTalkEvent(questId);
        qe.setNpcQuestData(799506).addOnTalkEvent(questId);
        qe.setNpcQuestData(798909).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 798909) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 4762); else return defaultQuestStartDialog(env);
            }
        }
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            switch(targetId) {
                case 798941:
                    if (var == 0) {
                        switch(env.getDialogId()) {
                            case 26:
                                return sendQuestDialog(env, 1011);
                            case 10000:
                                qs.setQuestVar(1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                case 799506:
                    if (var == 1) {
                        switch(env.getDialogId()) {
                            case 26:
                                return sendQuestDialog(env, 1352);
                            case 10001:
                                qs.setQuestVar(2);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                case 798909:
                    if (var == 2) {
                        switch(env.getDialogId()) {
                            case 26:
                                return sendQuestDialog(env, 1693);
                            case 34:
                                if (player.getInventory().getItemCountByItemId(182209618) < 1) {
                                    return sendQuestDialog(env, 10001);
                                } else if (player.getInventory().getItemCountByItemId(182209619) < 1) {
                                    return sendQuestDialog(env, 10001);
                                }
                                player.getInventory().removeFromBagByItemId(182209618, 1);
                                player.getInventory().removeFromBagByItemId(182209619, 1);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                        }
                    }
                    return false;
            }
        }
        return false;
    }
}
