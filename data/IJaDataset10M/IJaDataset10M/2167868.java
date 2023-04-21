package org.bejug.javacareers.jobs.dao;

import org.bejug.javacareers.jobs.model.Item;

/**
 * dao for the item-tables.
 *
 * @author Bart Meyers (Last modified by $Author: stephan_janssen $)
 * @version $Revision: 1.5 $ - $Date: 2005/10/11 08:33:33 $
 */
public interface ItemDao {

    /**
	 * get an item by id.
	 * @param id Integer. 
	 * @return the wanted item.
	 */
    Item getItem(Integer id);

    /**
	 * delete an item . 
	 * @param item the item to delete.
	 */
    void deleteItem(Item item);

    /**
	 * delete an item by id. 
	 * @param id the id of the item to delete.
	 */
    void deleteItem(Integer id);

    /**
	 * store an item
	 * @param item Item.
	 */
    public void storeItem(Item item);
}
