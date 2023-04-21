package alexandria.gui;

import alexandria.core.Item;
import alexandria.core.Store;

/**
 * Object to be used as a root for the ItemsTreeModel
 *
 * @author Neyko Neykov, 2007
 */
public class ItemsTreeRoot {

    /** Store item on which this items root is based */
    private Store store;

    /** Creates a new instance of ItemsTreeRoot */
    public ItemsTreeRoot(Store store) {
        this.store = store;
    }

    /** Retrieves the collection of items within this root */
    public Item[] getItems() {
        return store.getRootItems();
    }

    /** Retrieves the size of the root */
    public int size() {
        return store.getRootItems().length;
    }

    /** Retrieves the name of the store */
    @Override
    public String toString() {
        return store.getName();
    }
}
