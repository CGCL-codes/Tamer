package org.azrul.epice.rest.dto;

import org.azrul.epice.domain.Item;

/**
 *
 * @author azrulm
 */
public class UpdateItemResponse extends Response {

    private Item updatedItem;

    /**
     * @return the updatedItem
     */
    public Item getUpdatedItem() {
        return updatedItem;
    }

    /**
     * @param updatedItem the updatedItem to set
     */
    public void setUpdatedItem(Item updatedItem) {
        this.updatedItem = updatedItem;
    }
}
