package org.jxul.tree;

import org.w3c.dom.Element;

/**
 * @author E20199
 *
 */
public interface IXULTreeBuilderObserver {

    int DROP_BEFORE = -1;

    int DROP_ON = 0;

    int DROP_AFTER = 1;

    /**
	 * Methods used by the drag feedback code to determine if a drag is allowable at the current location. To get the behavior where drops are only allowed on items, such as the mailNews folder pane, always return false whe the orientation is not DROP_ON. 
	 */
    boolean canDrop(int index, int orientation);

    /**
	 * Called when a cell in a non-selectable cycling column (e.g. unread/flag/etc.) is clicked.
	 * @param row
	 * @param colID
	 */
    void onCycleCell(int row, String colID);

    /**
	 * Called when a header is clicked. 
	 * @param colID
	 * @param elt
	 */
    void onCycleHeader(String colID, Element elt);

    /**
	 * Called when the user drops something on this view. The orientation param specifies before/on/after the given row. 
	 * @param row
	 * @param orientation
	 */
    void onDrop(int row, int orientation);

    /**
	 * A command API that can be used to invoke commands on the selection. The tree will automatically invoke this method when certain keys are pressed. For example, when the DEL key is pressed, performAction will be called with the "delete" string. 
	 * @param action
	 */
    void onPerformAction(String action);

    /**
	 * A command API that can be used to invoke commands on a specific cell. 
	 */
    void onPerformActionOnCell(String action, int row, String colID);

    /**
	 * A command API that can be used to invoke commands on a specific row. 
	 * @param action
	 * @param row
	 */
    void onPerformActionOnRow(String action, int row);

    /**
	 * Called when selection in the tree changes 
	 *
	 */
    void onSelectionChanged();

    /**
	 * Called when an item is opened or closed. 
	 * @param index
	 */
    void onToggleOpenState(int index);
}
