package ch.iserver.ace.application;

import java.util.EventObject;

public class ItemSelectionChangeEvent extends EventObject {

    private Item item;

    public ItemSelectionChangeEvent(Object source, Item item) {
        super(source);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
