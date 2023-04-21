package com.dexter.fridgeManagement.server.dataaccess;

import java.util.Collection;
import com.dexter.fridgeManagement.client.ListItem;
import com.dexter.fridgeManagement.client.Product;
import com.dexter.fridgeManagement.client.Unit;
import com.dexter.fridgeManagement.shared.ShoppingListEntry;

public interface IDataAccessHandler {

    /**
	 * Stores the given item. If the item already exists in the data store,
	 * nothing will be done, i.e. the new values will be dropped and the existing
	 * item will be kept.
	 * 
	 * @param listItem_in
	 *            The list item to be stored. May not be null. Note that the
	 *            javax.persistence.Entity flag has to be set.
	 */
    public abstract void storeItem(ListItem listItem_in);

    /**
	 * Looks up all available list items and returns a list.
	 * 
	 * @return the list of items. If no items were found, an empty list will be
	 *         returned.
	 */
    public abstract Collection<ListItem> getAllItems();

    /**
	 * Deletes the item of type with the name specified by the parameter
	 * 
	 * @param nameOfItemToBeDeleted_in
	 *            name of the item to be deleted
	 */
    public abstract void deleteItem(String nameOfItemToBeDeleted_in);

    /**
	 * Merges the given item with the items in the data store.
	 * If there is no existing item, an ordinary store operation will
	 * be performed
	 * @param item_in the item to be merged
	 */
    public abstract void merge(ListItem item_in);

    /**
	 * Stores a new unit item
	 * @param unitString_in the unit to be added
	 */
    public abstract void storeUnit(String unitString_in);

    /**
	 * Looks up all available units and returns a list.
	 * 
	 * @return the list of units. If no items were found, an empty list will be
	 *         returned.
	 */
    public abstract Collection<Unit> getAllUnits();

    /**
	 * Retrieves the unit with the given ID
	 * @param unitString_in the unit's ID
	 * @return the retrieved unit or null, if not object could be found
	 */
    public abstract Unit getUnit(String unitString_in);

    /**
	 * Updates a product. If the product does not exist in the data storage,
	 * it will be added. If it exists, its contents will be updated.
	 * 
	 * @param newProduct_in the product to be updated. Must not be null.
	 * @exception IllegalArgumentException thrown if the input argument was invalid
	 */
    public abstract void updateProduct(Product newProduct_in);

    /**
	 * Retrieves all products from the server
	 * @return the list of available products or null, if no product was available
	 */
    public abstract Collection<Product> getAllProducts();

    /**
	 * Returns the ListItem with the given ID
	 * @param name_in the ID of the item 
	 * @return the actual ListItem or null, if the desired item could not be found
	 */
    public abstract ListItem getItem(String name_in);

    /**
	 * Retrieves all product details for the product with the given name
	 *  
	 * @param productName_in the name of the product. Must not be null or empty.
	 * @return the product details or null, if no product with this name could be found
	 * @exception IllegalArgumentException thrown if the name of the product was null or empty
	 */
    public abstract Product getProductDetails(String productName_in);

    /**
	 * Returns a list of all items that are currently out of stock.
	 * This means that all items whose actual amount is less than the 
	 * minimum quantity defined by the related product.
	 * @return a list of items whose actual count is less than the defined minimum quantity
	 */
    public abstract Collection<ListItem> getAllItemsOutOfStock();

    /**
	 * Stores the given shopping list entry
	 * 
	 * @param entry_in
	 *            The item to be stored. May not be null. Note that the
	 *            javax.persistence.Entity flag has to be set.
	 */
    public abstract void storeShoppingListEntry(ShoppingListEntry entry_in);

    /**
	 * Looks up all available items of the shopping list and returns a list.
	 * 
	 * @return the list of items. If no items were found, an empty list will be
	 *         returned.
	 */
    public abstract Collection<ShoppingListEntry> getAllShoppingListEntries();

    /**
	 * Deletes the item of type with the name specified by the parameter
	 * 
	 * @param nameOfItemToBeDeleted_in
	 *            name of the item to be deleted
	 */
    public abstract void deleteShoppingListEntry(String nameOfItemToBeDeleted_in);

    /**
	 * Searches for a shopping list entry with a specified name
	 * @param name_in the name to be searched for
	 * @return the found item or null if not item with the given name could be found
	 */
    public abstract ShoppingListEntry getShoppingListEntryByName(String name_in);
}
