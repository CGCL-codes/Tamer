package net.sourceforge.nattable.widget;

public interface ISelectionListListener {

    void itemsSelected(SelectionList selectionList, Object[] addedItems);

    void itemsRemoved(SelectionList selectionList, Object[] removedItems);

    void itemsMoved(SelectionList selectionList, int[] oldIndexes, int[] newIndexes);
}
