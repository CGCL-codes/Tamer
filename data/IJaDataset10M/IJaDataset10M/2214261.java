package gameserver.model.templates.bonus;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.items.ItemBonus;
import gameserver.model.templates.item.ItemQuality;
import gameserver.model.templates.item.ItemRace;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import commons.utils.Rnd;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MasterRecipeBonus")
public class MasterRecipeBonus extends SimpleCheckItemBonus {

    static final InventoryBonusType type = InventoryBonusType.MASTER_RECIPE;

    @XmlAttribute()
    protected int skillId;

    public int getSkillId() {
        return skillId;
    }

    @Override
    public boolean canApply(Player player, int itemId, int questId) {
        if (!super.canApply(player, itemId, questId)) return false;
        Storage storage = player.getInventory();
        if (storage.getItemCountByItemId(checkItem) < count) return false; else if (storage.isFull()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_FULL_INVENTORY);
            return false;
        }
        return player.getSkillList().isSkillPresent(skillId);
    }

    @Override
    public boolean apply(Player player, Item item) {
        if (item != null) {
            ItemBonus bonusInfo = item.getItemTemplate().getBonusInfo();
            if (bonusInfo == null) return true;
            List<QuestItems> qi = Collections.singletonList(new QuestItems(item.getItemId(), 1));
            return ItemService.addItems(player, qi);
        }
        int heartLevel = DataManager.ITEM_DATA.getItemTemplate(checkItem).getLevel();
        ItemQuality quality = ItemQuality.RARE;
        if (Rnd.get() * 100 < 30) {
            quality = ItemQuality.LEGEND;
            if (heartLevel == 60 && Rnd.get() * 100 < 30) {
                quality = ItemQuality.UNIQUE;
            }
        }
        int bonusLevel = (skillId & 0xF) << 10 | ((quality.ordinal() << 7) | heartLevel);
        List<Integer> designIds = DataManager.ITEM_DATA.getBonusItems(InventoryBonusType.MASTER_RECIPE, bonusLevel, bonusLevel + 1);
        if (designIds.size() == 0) return true;
        List<Integer> finalIds = new ArrayList<Integer>();
        for (Integer id : designIds) {
            ItemTemplate template = ItemService.getItemTemplate(id);
            ItemRace itemRace = template.getRace();
            if (String.valueOf(itemRace) != String.valueOf(player.getCommonData().getRace())) continue;
            finalIds.add(id);
        }
        if (finalIds.size() == 0) return true;
        int itemId = finalIds.get(Rnd.get(finalIds.size()));
        return ItemService.addItems(player, Collections.singletonList(new QuestItems(itemId, 1)));
    }

    @Override
    public InventoryBonusType getType() {
        return type;
    }
}
