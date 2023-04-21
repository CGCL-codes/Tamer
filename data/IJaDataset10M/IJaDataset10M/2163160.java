package edu.uis.csc478.spring09.threeoxen.persistence.store;

import edu.uis.csc478.spring09.threeoxen.persistence.ObjectRepository;
import edu.uis.csc478.spring09.threeoxen.persistence.pantry.Pantry;

public interface StoreRepository extends ObjectRepository {

    public GroceryItem getGroceryItem(String itemId);

    public void addGroceryItem(GroceryItem item);

    public void updateGroceryItem(GroceryItem item);

    public void removeGroceryItem(String upcCode);

    public boolean groceryItemExists(String upcCode);

    public String getGroceryItemContents();

    public Store getStoreByOwnerName(String storeOwnerName);

    public void addStore(Store store);

    public boolean storeExists(String storeOwnerName);
}
