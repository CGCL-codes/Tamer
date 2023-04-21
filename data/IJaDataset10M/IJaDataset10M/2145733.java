package org.bejug.javacareers.project.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bejug.javacareers.jobs.model.Item;

/**
 * This class holds the list of ItemValueBindings.
 *
 * @author : Peter Symoens (Last modified by $Author: shally $)
 * @version $Revision: 1.7 $ - $Date: 2005/12/20 15:36:47 $:
 */
public class ItemValueBindingList {

    private List bindingList;

    /**
     * 
     */
    private ItemValueBinding[] itemList;

    /**
     *
     * @param itemList The itemListto set.
     */
    public void setItemList(ItemValueBinding[] itemList) {
        this.itemList = itemList;
    }

    /**
     *
     * @param item Item
     */
    public void add(Item item) {
        bindingList.add(item);
    }

    /**
     *
     * @param item Item
     */
    public void remove(Item item) {
        bindingList.remove(item);
    }

    /**
     *
     * @return the ValueBindingList
     */
    public List getBindingList() {
        if (bindingList == null) {
            bindingList = new ArrayList(Arrays.asList(itemList));
        }
        return bindingList;
    }
}
