package jp.seraph.same.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jp.seraph.same.message.MessageUtil;

/**
 * ListedHierarchicalViewModelの抽象実装
 * このクラスは、子の名前が重複しないことを求めます。
 */
public abstract class AbstractListedHierarchicalViewModel<ChildType extends ViewModel> extends AbstractHierarchicalViewModel<ChildType> implements ListedHierarchicalViewModel<ChildType> {

    /**
     * @param aName
     */
    public AbstractListedHierarchicalViewModel(String aName) {
        super(aName);
        mChildren = new ArrayList<ChildType>();
        mAddedListeners = new ArrayList<ListChildAddedListener<ChildType>>();
        mRemovedListeners = new ArrayList<ListChildRemovedListener<ChildType>>();
    }

    private List<ChildType> mChildren;

    private List<ListChildAddedListener<ChildType>> mAddedListeners;

    private List<ListChildRemovedListener<ChildType>> mRemovedListeners;

    /**
     * @see jp.seraph.same.model.ListedHierarchicalViewModel#add(int, jp.seraph.same.model.ViewModel)
     */
    public void add(int aIndex, ChildType aChild) {
        if (this.contains(aChild)) {
            throw new IllegalArgumentException(MessageUtil.duplicateChildName(aChild.getName()));
        }
        mChildren.add(aIndex, aChild);
        fireChangedEvent();
        fireAddedEvent(aChild);
        fireAddedEvent(aIndex, aChild);
    }

    /**
     * @see jp.seraph.same.model.ListedHierarchicalViewModel#remove(int)
     */
    public ChildType remove(int aIndex) {
        ChildType tRemoved = mChildren.remove(aIndex);
        fireChangedEvent();
        fireRemovedEvent(tRemoved);
        fireRemovedEvent(aIndex, tRemoved);
        return tRemoved;
    }

    /**
     * @see jp.seraph.same.model.HierarchicalViewModel#add(jp.seraph.same.model.ViewModel)
     */
    public void add(ChildType aChild) {
        this.add(this.getChildCount(), aChild);
    }

    /**
     * @see jp.seraph.same.model.ListedHierarchicalViewModel#get(int)
     */
    public ChildType get(int aIndex) {
        return mChildren.get(aIndex);
    }

    /**
     * @see jp.seraph.same.model.HierarchicalViewModel#contains(jp.seraph.same.model.ViewModel)
     */
    public boolean contains(ChildType aChild) {
        return contains(aChild.getName());
    }

    /**
     * @see jp.seraph.same.model.HierarchicalViewModel#contains(java.lang.String)
     */
    public boolean contains(String aChildName) {
        List<ChildType> tChildren = mChildren;
        for (ChildType tChild : tChildren) {
            if (aChildName.equals(tChild.getName())) return true;
        }
        return false;
    }

    /**
     * @see jp.seraph.same.model.ListedHierarchicalViewModel#getIndex(jp.seraph.same.model.ViewModel)
     */
    public int getIndex(ChildType aChild) {
        return this.getIndex(aChild.getName());
    }

    /**
     * @see jp.seraph.same.model.ListedHierarchicalViewModel#getIndex(java.lang.String)
     */
    public int getIndex(String aChildName) {
        List<ChildType> tChildren = mChildren;
        for (int i = 0; i < tChildren.size(); i++) {
            ChildType tChild = tChildren.get(i);
            if (aChildName.equals(tChild.getName())) return i;
        }
        return -1;
    }

    /**
     * @see jp.seraph.same.model.HierarchicalViewModel#get(java.lang.String)
     */
    public ChildType get(String aChildName) {
        List<ChildType> tChildren = mChildren;
        for (ChildType tChild : tChildren) {
            if (aChildName.equals(tChild.getName())) return tChild;
        }
        return null;
    }

    /**
     * @see jp.seraph.same.model.HierarchicalViewModel#getChildCount()
     */
    public int getChildCount() {
        return mChildren.size();
    }

    /**
     * @see jp.seraph.same.model.HierarchicalViewModel#remove(jp.seraph.same.model.ViewModel)
     */
    public ChildType remove(ChildType aChild) {
        return this.remove(aChild.getName());
    }

    /**
     * @see jp.seraph.same.model.HierarchicalViewModel#remove(java.lang.String)
     */
    public ChildType remove(String aChildName) {
        int tIndex = this.getIndex(aChildName);
        if (tIndex == -1) return null; else return this.remove(tIndex);
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<ChildType> iterator() {
        return mChildren.iterator();
    }

    /**
     * @see jp.seraph.same.model.ListedHierarchicalViewModel#addAddedListener(jp.seraph.same.model.ListChildAddedListener)
     */
    public void addAddedListener(ListChildAddedListener<ChildType> aListener) {
        mAddedListeners.add(aListener);
    }

    /**
     * @see jp.seraph.same.model.ListedHierarchicalViewModel#removeAddedListener(jp.seraph.same.model.ListChildAddedListener)
     */
    public void removeAddedListener(ListChildAddedListener<ChildType> aListener) {
        mAddedListeners.remove(aListener);
    }

    protected void fireAddedEvent(int aIndex, ChildType aNewChild) {
        for (ListChildAddedListener<ChildType> tListener : mAddedListeners) {
            tListener.handle(this, aIndex, aNewChild);
        }
    }

    /**
     * @see jp.seraph.same.model.ListedHierarchicalViewModel#addRemovedListener(jp.seraph.same.model.ListChildRemovedListener)
     */
    public void addRemovedListener(ListChildRemovedListener<ChildType> aListener) {
        mRemovedListeners.add(aListener);
    }

    /**
     * @see jp.seraph.same.model.ListedHierarchicalViewModel#removeRemovedListener(jp.seraph.same.model.ListChildRemovedListener)
     */
    public void removeRemovedListener(ListChildRemovedListener<ChildType> aListener) {
        mRemovedListeners.add(aListener);
    }

    protected void fireRemovedEvent(int aIndex, ChildType aRemovedChild) {
        for (ListChildRemovedListener<ChildType> tListener : mRemovedListeners) {
            tListener.handle(this, aIndex, aRemovedChild);
        }
    }
}
