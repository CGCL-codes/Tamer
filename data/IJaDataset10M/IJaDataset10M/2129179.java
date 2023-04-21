package nakayo.gameserver.model.templates.bonus;

import com.aionemu.commons.utils.Rnd;
import nakayo.gameserver.dataholders.DataManager;
import nakayo.gameserver.model.gameobjects.Item;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.model.templates.quest.QuestItems;
import nakayo.gameserver.services.ItemService;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.Collections;
import java.util.List;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnchantBonus")
public class EnchantBonus extends SimpleCheckItemBonus {

    static final InventoryBonusType type = InventoryBonusType.ENCHANT;

    @Override
    public boolean apply(Player player, Item item) {
        List<Integer> itemIds = DataManager.ITEM_DATA.getBonusItems(type, bonusLevel, bonusLevel + 10);
        if (itemIds.size() == 0) return true;
        int itemId = itemIds.get(Rnd.get(itemIds.size()));
        return ItemService.addItems(player, Collections.singletonList(new QuestItems(itemId, 1)));
    }

    @Override
    public InventoryBonusType getType() {
        return type;
    }
}
