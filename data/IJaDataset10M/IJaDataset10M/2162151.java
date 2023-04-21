package net.sf.jmoney.oda.ui.wizards;

import net.sf.jmoney.model2.EntryInfo;
import net.sf.jmoney.model2.ExtendablePropertySet;
import net.sf.jmoney.model2.ListPropertyAccessor;
import org.eclipse.ui.IMemento;

/**
 * This class represents nodes in the item list selection tree.
 * There is one instance of this class for each node in the tree,
 * being the data set for each node.
 */
class ObjectList_EntriesInAccount implements IObjectList {

    IObjectList parentItemList;

    public ObjectList_EntriesInAccount(IObjectList parentItemList) {
        this.parentItemList = parentItemList;
    }

    public void save(IMemento memento) {
        IMemento childMemento = memento.createChild("entriesInAccount");
        parentItemList.save(childMemento);
    }

    public boolean isUsed(ListPropertyAccessor listProperty) {
        return parentItemList.isUsed(listProperty);
    }

    public ExtendablePropertySet getPropertySet() {
        return EntryInfo.getPropertySet();
    }
}
