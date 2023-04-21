package alexandria.gui;

import alexandria.core.Item;
import alexandria.core.Store;
import javax.swing.JTree;

/**
 * Extends JTree component to represent part of the hierhical tree model
 * stored in a specified Berkeley DB persistence store.
 *
 * @author neiko
 */
public class ItemsTree extends JTree {

    /**
     * Creates a new instance of ItemsTree based on some element in the store.
     * Element can't be null and must be existent in the store. This prevents
     * situations in which model can return empty tree path arrays. If null
     * parameter is passed as a root for the items tree, NullPointerException
     * will be thrown.
     */
    public ItemsTree(Store store) {
        super();
        setModel(new ItemsTreeModel(store));
    }

    /** Sets new store to be viewed by this tree */
    public void setStore(Store store) {
        ((ItemsTreeModel) getModel()).setStore(store);
    }
}
