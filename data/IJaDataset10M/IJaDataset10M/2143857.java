package quest.beshmundir_temple;

import java.util.Collections;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.services.ZoneService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

/**
 * @author Ritsu
 * 
 */
public class _30236GroupNewGreatSword extends QuestHandler {

    private static final int questId = 30236;

    public _30236GroupNewGreatSword() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(799032).addOnQuestStart(questId);
        qe.setNpcQuestData(799032).addOnTalkEvent(questId);
        qe.setQuestItemIds(186000107).add(questId);
    }

    @Override
    public boolean onItemUseEvent(final QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();
        if (id == 186000107) {
            if (!ZoneService.getInstance().isInsideZone(player, ZoneName.DEBILKARIM_FORGE_300160000)) return false;
            if (qs == null) return true;
            if (qs.getQuestVarById(0) != 0) return false;
            if (player.getInventory().getItemCountByItemId(100900716) == 0 || player.getInventory().getItemCountByItemId(186000099) == 0 || player.getInventory().getItemCountByItemId(186000106) < 20 || player.getInventory().getItemCountByItemId(186000107) == 0) return false;
            PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
            ThreadPoolManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    ItemService.addItems(player, Collections.singletonList(new QuestItems(182209634, 1)));
                    PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                    player.getInventory().removeFromBagByItemId(186000099, 1);
                    player.getInventory().removeFromBagByItemId(186000106, 20);
                    player.getInventory().removeFromBagByItemId(186000107, 1);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                }
            }, 3000);
            return true;
        }
        return true;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 799032) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 4762); else return defaultQuestStartDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == -1) {
                    if (QuestService.collectItemCheck(env, false)) return sendQuestDialog(env, 2716);
                } else return defaultQuestStartDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                if (env.getDialogId() == -1) return sendQuestDialog(env, 10002); else if (env.getDialogId() == 1009) return sendQuestDialog(env, 5); else return defaultQuestEndDialog(env);
            }
        }
        return false;
    }
}
