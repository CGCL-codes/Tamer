package org.openaion.gameserver.model.templates.bonus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;

/**
 * @author Rolandas
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GoodsBonus")
public class GoodsBonus extends AbstractInventoryBonus {

    static final InventoryBonusType type = InventoryBonusType.GOODS;

    @XmlAttribute()
    protected int checkItem;

    @Override
    public boolean apply(Player player, Item item) {
        return true;
    }

    @Override
    public boolean canApply(Player player, int itemId, int questId) {
        return false;
    }

    @Override
    public InventoryBonusType getType() {
        return type;
    }
}
