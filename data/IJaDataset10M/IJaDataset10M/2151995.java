package com.archhuman.hart.event;

import com.archhuman.hart.model.Item;

/**
 * @author Sean
 */
public class ItemTimeEvent {

    private Item item;

    private int timeRemaining;

    /**
	 * @param item
	 * @param timeRemaining
	 */
    public ItemTimeEvent(Item item, int timeRemaining) {
        super();
        this.item = item;
        this.timeRemaining = timeRemaining;
    }

    /**
	 * @return Returns the item.
	 */
    public Item getItem() {
        return item;
    }

    /**
	 * @return Returns the timeRemaining.
	 */
    public int getTimeRemaining() {
        return timeRemaining;
    }
}
