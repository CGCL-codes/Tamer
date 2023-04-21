package kellinwood.meshi.autocomplete;

import java.util.*;
import javax.swing.*;

/**
 *
 * @author  ken
 */
public class SimpleListModelProvider implements ListModelProvider {

    private java.util.Collection collection = null;

    private ListModel listModel = new javax.swing.DefaultComboBoxModel();

    /** Creates a new instance of SimpleListModelProvider */
    public SimpleListModelProvider() {
    }

    /** Creates a new instance of SimpleListModelProvider */
    public SimpleListModelProvider(java.util.Collection collection) {
        setCollection(collection);
    }

    public java.util.Collection getCollection() {
        return collection;
    }

    /**
     * Setter for property list.
     * @param list New value of property list.
     */
    public void setCollection(java.util.Collection collection) {
        this.collection = collection;
        listModel = new DefaultComboBoxModel(collection.toArray());
    }

    public javax.swing.ListModel getListModel() {
        return listModel;
    }
}
