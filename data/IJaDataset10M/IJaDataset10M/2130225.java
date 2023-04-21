package com.archhuman.hart.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sean
 */
public class Map {

    private String name;

    private List items = new ArrayList();

    /**
	 * @return Returns the items.
	 */
    public List getItems() {
        return items;
    }

    /**
	 * @param items The items to set.
	 */
    public void setItems(List items) {
        this.items = items;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public String toString() {
        return name;
    }
}
