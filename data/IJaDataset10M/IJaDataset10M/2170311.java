package quest.inggison;

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
public class _30206DragonboundDiaries extends QuestHandler {

    private static final int questId = 30206;

    public _30206DragonboundDiaries() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(798941).addOnQuestStart(questId);
        qe.setNpcQuestData(798941).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 798941) {
                switch(env.getDialogId()) {
                    case 26:
                        return sendQuestDialog(env, 1011);
                    case 1008:
                        return sendQuestDialog(env, 2375);
                    case 34:
                        if (player.getInventory().getItemCountByItemId(182209608) < 1) {
                            return sendQuestDialog(env, 2716);
                        }
                        player.getInventory().removeFromBagByItemId(182209608, 1);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                }
            }
            return false;
        }
        return false;
    }
}
