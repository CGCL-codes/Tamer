package odrop.client.handlers;

import java.util.Set;
import odrop.client.enumerators.ColumnVisibilityEventType;
import odrop.client.handlers.ColumnVisibilityChangeEvent.ColumnVisibilityChangeEventHandler;
import odrop.client.widgets.HidableColumn;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * ColumnVisibilityChangeEvent is a sub-class of GwtEvent.
 * This event is triggered when you select a column from a dropdown-menu and have it checked or unchecked.
 * It results in the column being hidden or shown.
 * Currently the Belongings-page implements this handler and its onColumnVisibilityChanged-method in order
 * to show or hide the columns. This lets the user customize the view on the item-CellTable.
 * 
 * @author divStar
 */
public class ColumnVisibilityChangeEvent extends GwtEvent<ColumnVisibilityChangeEventHandler> {

    /**
	 * ColumnVisibilityChangeEventHandler is the interface that pages use to react to a
	 * ColumnVisibilityChangeEvent.
	 * 
	 * @author divStar
	 */
    public static interface ColumnVisibilityChangeEventHandler extends EventHandler {

        public void onColumnVisibilityChanged(ColumnVisibilityChangeEvent event);
    }

    public static Type<ColumnVisibilityChangeEventHandler> TYPE = new Type<ColumnVisibilityChangeEventHandler>();

    /**
	 * This method registers this event with a given eventbus, attaching it to a listener that implements
	 * the appropriate handler to react to the change (the listener must implement the handler-interface).
	 * NOTE: in ODrop these events are attached using
	 * 		 ODrop.eventBus.addHandler(Event.TYPE, EventHandler);
	 * instead of using this register-method.
	 * 
	 * @param eventBus	EventBus to register this event on (ODrop has a central eventbus in odrop.client.ODrop)
	 * @param handler	potential listener, that implements the ColumnVisibilityChangeEventHandler-interface
	 * @return			HandlerRegistration-object that contains further information.
	 */
    public static HandlerRegistration register(final EventBus eventBus, final ColumnVisibilityChangeEventHandler handler) {
        return eventBus.addHandler(ColumnVisibilityChangeEvent.TYPE, handler);
    }

    private HidableColumn<?, ?> column;

    private Set<HidableColumn<?, ?>> selectedColumns;

    private ColumnVisibilityEventType columnVisibilityEventType;

    public HidableColumn<?, ?> getColumn() {
        return column;
    }

    public void setColumn(HidableColumn<?, ?> column) {
        this.column = column;
    }

    public Set<HidableColumn<?, ?>> getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(Set<HidableColumn<?, ?>> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public ColumnVisibilityEventType getColumnVisibilityEventType() {
        return columnVisibilityEventType;
    }

    public void setColumnVisibilityEventType(ColumnVisibilityEventType columnVisibilityEventType) {
        this.columnVisibilityEventType = columnVisibilityEventType;
    }

    /**
	 * Constructor without any arguments
	 */
    public ColumnVisibilityChangeEvent() {
    }

    /**
	 * Constructor will be used with COLUMN_VISIBILITY_CHANGE_ON_SAME_PAGE-type in order to initiate this event.
	 * 
	 * @param ColumnVisibilityEventType		this parameter specifies the event sub-type as defined in the corresponding enumeration.
	 */
    public ColumnVisibilityChangeEvent(ColumnVisibilityEventType columnVisibilityEventType) {
        this();
        this.setColumnVisibilityEventType(columnVisibilityEventType);
    }

    /**
	 * Constructor with a HidableColumn.
	 * 
	 * @param ColumnVisibilityEventType		this parameter specifies the event sub-type as defined in the corresponding enumeration.
	 * @param HidableColumn<?,?>			Column that is supposed to be hidden
	 */
    public ColumnVisibilityChangeEvent(ColumnVisibilityEventType columnVisibilityEventType, HidableColumn<?, ?> column) {
        this(columnVisibilityEventType);
        this.setColumn(column);
    }

    /**
	 * Constructor will be used with COLUMN_VISIBILITY_CHANGE_ON_PAGE_SWITCH-type in order to perform the event using the retrieved selectedColumns-list.
	 * 
	 * @param ColumnVisibilityEventType		this parameter specifies the event sub-type as defined in the corresponding enumeration.
	 * @param Set<HidableColumn<?,?>>		a set of selected columns
	 */
    public ColumnVisibilityChangeEvent(ColumnVisibilityEventType columnVisibilityEventType, Set<HidableColumn<?, ?>> selectedColumns) {
        this(columnVisibilityEventType);
        this.setSelectedColumns(selectedColumns);
    }

    @Override
    protected void dispatch(ColumnVisibilityChangeEventHandler handler) {
        handler.onColumnVisibilityChanged(this);
    }

    @Override
    public Type<ColumnVisibilityChangeEventHandler> getAssociatedType() {
        return ColumnVisibilityChangeEvent.TYPE;
    }
}
