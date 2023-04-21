package gnu.ojama.ui;

import gnu.ojama.model.*;
import javax.swing.*;

/**
 * A generic adapter in the framework for JList components.
 * @author Markku Vuorenmaa
 */
public class ListAdapter extends AbstractListModel {

    private ModelObject myRoot;

    /**
     * Default constructor
     */
    public ListAdapter() {
        super();
        myRoot = null;
    }

    /**
     * Free the resources used by this class.
     */
    public void finalize() throws Throwable {
        myRoot = null;
        super.finalize();
    }

    /**
     * Returns a the current root object from this adapter.
     * @return root object stored in the adapter
     */
    public ModelObject getRoot() {
        return myRoot;
    }

    /**
     * Sets a new root object for this adapter.
     * @param root new root object
     */
    public void setRoot(ModelObject root) {
        int oldChildCount = getSize();
        int newChildCount = 0;
        myRoot = root;
        if (myRoot != null) {
            myRoot.expand();
            newChildCount = myRoot.getChildCount();
        }
        if (newChildCount < oldChildCount) {
            fireIntervalRemoved(this, newChildCount, oldChildCount - 1);
        }
        fireContentsChanged(this, 0, oldChildCount);
        if (newChildCount > oldChildCount) {
            fireIntervalAdded(this, oldChildCount, newChildCount - 1);
        }
    }

    /**
     * Returns the child object under the root at specified index.
     * @param list object index
     * @return Object at specified index
     */
    public Object getElementAt(int index) {
        return myRoot.getChild(index);
    }

    /**
     * Returns the list model size based on the child count of the root object.
     * @return list model size
     */
    public int getSize() {
        int result = 0;
        if (myRoot != null) {
            myRoot.expand();
            result = myRoot.getChildCount();
        }
        return result;
    }
}
