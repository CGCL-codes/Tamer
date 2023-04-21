package com.ao.model.worldobject;

import com.ao.model.character.Character;
import com.ao.model.worldobject.properties.ItemProperties;

/**
 * An item that is consumed when used.
 */
public abstract class ConsumableItem extends AbstractItem {

    /**
	 * Creates a new ConsumableItem instance.
	 * @param properties The item's properties.
	 * @param amount The item's amount.
	 */
    public ConsumableItem(ItemProperties properties, int amount) {
        super(properties, amount);
    }

    @Override
    public void use(Character character) {
        if (--this.amount == 0) {
            character.getInventory().cleanup();
        }
    }
}
