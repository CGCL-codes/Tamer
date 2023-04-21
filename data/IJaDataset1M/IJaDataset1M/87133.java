package com.thyante.thelibrarian.view;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import com.thyante.thelibrarian.model.specification.IField;
import com.thyante.thelibrarian.model.specification.IItem;

public interface ICollectionView extends IView, IItemFilterListener {

    /**
	 * This method is called when this view becomes the active view.
	 */
    public void onViewShown();

    /**
	 * This method is called when the view is hidden (i.e. another view
	 * is made visible).
	 */
    public void onViewHidden();

    /**
	 * Notifies the view that the value of the field <code>field</code> in the item <code>item</code>
	 * has changed.
	 * @param item The item that has changed
	 * @param field The field whose value has changed
	 */
    public void onItemValueChanged(IItem item, IField field);

    /**
	 * Notifies the view that media files have been added or removed.
	 * @param item The item whose media files have been changed
	 */
    public void onItemMediaChanged(IItem item);

    /**
	 * Returns the currently set item filter.
	 * @return The current item filter
	 */
    public ItemFilterChain getFilterChain();

    /**
	 * Displays the UI for editing the item <code>item</code>.
	 * @param item The item to edit
	 */
    public void editItem(IItem item);

    /**
	 * Returns the currently selected items.
	 * @return The currently selected items
	 */
    public IStructuredSelection getSelection();

    /**
	 * Returns the item that is currently focused (or <code>null</code> if there is no such item).
	 * @return The currently focused item
	 */
    public IItem getFocusedItem();

    /**
	 * Selects the items that are referenced in the <code>selection</code> object.
	 * @param selection The new selection
	 */
    public void setSelection(IStructuredSelection selection);

    /**
	 * Shows the editing dialog for the selected item.
	 */
    public void editSelectedItem();

    /**
	 * Removes the selected items from the collection.
	 * @param bAskFeedback Flag indicating whether the user should be prompted whether she wants
	 * 	to delete the selected items
	 */
    public void removeSelectedItems(boolean bAskFeedback);

    /**
	 * Adds the selection change listener <code>listener</code> to the view.
	 * @param listener The listener to add
	 */
    public void addSelectionChangedListener(ISelectionChangedListener listener);

    /**
	 * Removes the selection change listener <code>listener</code> from the view.
	 * @param listener The listener to remove
	 */
    public void removeSelectionChangedListener(ISelectionChangedListener listener);

    /**
	 * Adds the item detail listener <code>listener</code> to the view.
	 * @param listener The listener to add
	 */
    public void addItemDetailListener(IItemDetailListener listener);

    /**
	 * Removes the item detail listener <code>listener</code> from the view.
	 * @param listener The listener to remove
	 */
    public void removeItemDetailListener(IItemDetailListener listener);
}
