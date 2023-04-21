package com.softaspects.jsf.component.table.event;

import com.softaspects.jsf.component.table.listener.TableValueChangedListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.component.UIComponent;

public class TableValueChangedEvent extends FacesEvent {

    private int row;

    private int column;

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    /**
     * Construct a new event object from the specified
     * source component
     *
     * @param uiComponent component
     */
    public TableValueChangedEvent(UIComponent uiComponent, int row, int column) {
        super(uiComponent);
        this.row = row;
        this.column = column;
    }

    /**
     * Return true if this FacesListener is an instance of a
     * listener class that this event supports
     *
     * @param listener listener
     * @return true/false
     */
    public boolean isAppropriateListener(FacesListener listener) {
        return listener instanceof TableValueChangedListener;
    }

    /**
     * Broadcast this TableValueChangedEvent to the specified FacesListener
     * This will be accomplished by calling an event processing method,
     * and passing this TableValueChangedEvent as a paramter.
     *
     * @param listener listener
     */
    public void processListener(FacesListener listener) {
        ((TableValueChangedListener) listener).processTableValueChanged(this);
    }
}
