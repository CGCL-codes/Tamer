package org.webguitoolkit.ui.controls.table;

import java.io.Serializable;
import org.webguitoolkit.ui.controls.event.ClientEvent;

/**
 * interface for classes which want to react on the events a table event. All table events are being passed to the event listener
 * before any action from the table control is taken. This means that the listener is responsible to implement the default
 * behavior.
 * 
 * @see org.webguitoolkit.ui.controls.table.AbstractTableListener
 * 
 * @author arno
 */
public interface ITableListener extends Serializable {

    /**
	 * Button Top of table is pressed.
	 * 
	 * @param event event to be raised
	 */
    void onGotoTop(ClientEvent event);

    /**
	 * Button PageUp is pressed
	 * 
	 * @param event event to be raised
	 */
    void onPageUp(ClientEvent event);

    /**
	 * Button one line up is pressed
	 * 
	 * @param event event to be raised
	 */
    void onClickUp(ClientEvent event);

    /**
	 * Button one Line down is pressed.
	 * 
	 * @param event event to be raised
	 */
    void onClickDown(ClientEvent event);

    /**
	 * Button page down is pressed
	 * 
	 * @param event event to be raised
	 */
    void onPageDown(ClientEvent event);

    /**
	 * Button to last lise is being pressed
	 * 
	 * @param event event to be raised
	 */
    void onGotoBottom(ClientEvent event);

    /**
	 * the user has pressed enter in the input box for jumping to a specific row.
	 * 
	 * @param event event to be raised
	 */
    void onGotoRow(ClientEvent event);

    /**
	 * User has clicked on a row
	 * 
	 * @param event event to be raised
	 */
    void onRowSelected(ClientEvent event);

    /**
	 * implicit filter is executed (click on image or return pressed)
	 * 
	 * @param event event to be raised
	 */
    void onImplicitFilter(ClientEvent event);

    /**
	 * The DropDown Filter are activated
	 * 
	 * @param event event to be raised
	 */
    void onDDFilter(ClientEvent event);

    /**
	 * user wants to sort for a column
	 * 
	 * @param event event to be raised
	 */
    void onSort(ClientEvent event);

    /**
	 * 
	 * @param event event to be raised
	 */
    void onScrollTo(ClientEvent event);

    /**
	 * 
	 * @param event event to be raised
	 * @param rowCount number of rows
	 * @param tableSetting t.b.d.
	 */
    void onEditTableLayout(ClientEvent event, int rowCount, String tableSetting);

    /**
	 * 
	 * @param event event to be raised
	 * @param draggabelId the id
	 * @param droppableId the id
	 */
    void onDropped(ClientEvent event, String draggabelId, String droppableId);

    /**
	 * 
	 * @param event event to be raised
	 * @param draggableId the id
	 */
    void onDragStart(ClientEvent event, String draggableId);

    /**
	 * 
	 * @param event event to be raised
	 */
    void onCheckAll(ClientEvent event);
}
