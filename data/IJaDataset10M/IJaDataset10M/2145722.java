package org.middleheaven.ui.models;

public interface UISelectionModel extends UIInputModel {

    public Object getElementAt(int index);

    /**
	 * 
	 * @return The number of elements in the model
	 */
    public int getSize();

    public void clearSelection();

    public boolean isSelectedIndex(int index);

    public boolean isSelectionEmpty();

    public int getMaxSelectionIndex();

    public int getMinSelectionIndex();

    public void setSelectionInterval(int start, int end);

    public void removeSelectionInterval(int start, int end);

    /**
	 * 
	 * @param anItem the index of the item in the model or -1 if the item is not in the model
	 * @return
	 */
    public int indexOf(Object anItem);
}
