package org.qtitools.constructr.itembank.providers;

import java.io.Serializable;
import org.qtitools.constructr.itembank.Item;

public class SelectableItemWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    Item item;

    boolean selected;

    public SelectableItemWrapper(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
