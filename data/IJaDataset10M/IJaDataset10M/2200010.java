package net.kansun.bjss.domain.util;

import net.kansun.bjss.domain.goods.Item;

public interface Basket {

    /**
	 * Gets the number of items currently in the basket
	 * 
	 * @param itemName
	 * @return
	 */
    Integer getAmount(Item.Type itemName);

    /**
	 * Adds a new item into the basket. This will result the amount of the
	 * corresponding item in the basket to be incremented by 1
	 * 
	 * @param item
	 */
    void putIn(Item item);

    void putIn(Item... items);

    boolean isEmpty();
}
