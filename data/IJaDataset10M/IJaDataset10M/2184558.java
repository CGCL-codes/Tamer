package jp.seraph.same.model;

/**
 * 子に順序を持つHierarchicalViewModelです。
 *
 * @param <ChildType>
 */
public interface ListedHierarchicalViewModel<ChildType extends ViewModel> extends HierarchicalViewModel<ChildType> {

    public void add(int aIndex, ChildType aChild);

    public ChildType get(int aIndex);

    public ChildType remove(int aIndex);

    /**
     *
     * @param aChild
     * @return 対象の子のインデックス 見つからなければ-1
     */
    public int getIndex(ChildType aChild);

    /**
     *
     * @param aChildName
     * @return 対象の子のインデックス 見つからなければ-1
     */
    public int getIndex(String aChildName);

    public void addAddedListener(ListChildAddedListener<ChildType> aListener);

    public void removeAddedListener(ListChildAddedListener<ChildType> aListener);

    public void addRemovedListener(ListChildRemovedListener<ChildType> aListener);

    public void removeRemovedListener(ListChildRemovedListener<ChildType> aListener);
}
