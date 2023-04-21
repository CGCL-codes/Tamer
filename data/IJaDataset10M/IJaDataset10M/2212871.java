package nakayo.gameserver.network.aion.clientpackets;

import nakayo.gameserver.itemengine.actions.AbstractItemAction;
import nakayo.gameserver.itemengine.actions.ItemActions;
import nakayo.gameserver.model.Race;
import nakayo.gameserver.model.gameobjects.Item;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.model.templates.item.ItemTemplate;
import nakayo.gameserver.network.aion.AionClientPacket;
import nakayo.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import nakayo.gameserver.questEngine.QuestEngine;
import nakayo.gameserver.questEngine.model.QuestCookie;
import nakayo.gameserver.restrictions.RestrictionsManager;
import nakayo.gameserver.utils.PacketSendUtility;
import nakayo.gameserver.world.zone.ZoneName;
import org.apache.log4j.Logger;
import java.util.ArrayList;

/**
 * @author Avol
 */
public class CM_USE_ITEM extends AionClientPacket {

    public int uniqueItemId;

    public int type, targetItemId;

    private static final Logger log = Logger.getLogger(CM_USE_ITEM.class);

    public CM_USE_ITEM(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        uniqueItemId = readD();
        type = readC();
        if (type == 2) {
            targetItemId = readD();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        Item item = player.getInventory().getItemByObjId(uniqueItemId);
        if (item == null) {
            log.warn(String.format("CHECKPOINT: null item use action: %d %d", player.getObjectId(), uniqueItemId));
            return;
        }
        if (!RestrictionsManager.canUseItem(player)) return;
        switch(item.getItemTemplate().getRace()) {
            case ASMODIANS:
                if (player.getCommonData().getRace() != Race.ASMODIANS) return;
                break;
            case ELYOS:
                if (player.getCommonData().getRace() != Race.ELYOS) return;
                break;
        }
        if (!item.getItemTemplate().isAllowedFor(player.getCommonData().getPlayerClass(), player.getLevel())) return;
        ItemTemplate template = item.getItemTemplate();
        if (QuestEngine.getInstance().onItemUseEvent(new QuestCookie(null, player, template.getItemQuestId(), 0), item)) return;
        if (player.isCasting()) {
            return;
        }
        Item targetItem = player.getInventory().getItemByObjId(targetItemId);
        if (targetItem == null) targetItem = player.getEquipment().getEquippedItemByObjId(targetItemId);
        ItemActions itemActions = item.getItemTemplate().getActions();
        ArrayList<AbstractItemAction> actions = new ArrayList<AbstractItemAction>();
        if (itemActions == null) return;
        for (AbstractItemAction itemAction : itemActions.getItemActions()) {
            if (itemAction.canAct(player, item, targetItem)) actions.add(itemAction);
        }
        if (actions.size() == 0) return;
        if (player.isItemUseDisabled(item.getItemTemplate().getDelayId())) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANT_USE_UNTIL_DELAY_TIME);
            return;
        }
        if (item.getItemTemplate().getUsedzone() != 0 && item.getItemTemplate().getUsedzone() != player.getWorldId()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CAN_NOT_USE_ITEM_IN_CURRENT_POSITION);
            return;
        }
        int useDelay = item.getItemTemplate().getDelayTime();
        player.addItemCoolDown(item.getItemTemplate().getDelayId(), System.currentTimeMillis() + useDelay, useDelay / 1000);
        for (AbstractItemAction itemAction : actions) {
            itemAction.act(player, item, targetItem);
        }
    }
}
