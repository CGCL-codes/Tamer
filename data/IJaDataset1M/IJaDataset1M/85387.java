package org.gwanted.gwt.widget.grid.client.view.cells;

import org.gwanted.gwt.widget.grid.client.view.controls.Control;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Miguel A. Rager
 */
public class ControlCell extends TableCell {

    private final int events;

    private final Control control;

    private final int row;

    private final boolean isASourcesClickEvents;

    public ControlCell(final Control control, final int row) {
        super();
        this.control = control;
        this.row = row;
        this.events = control.getEvents();
        if (control.getStyle() != null) {
            this.addStyleName(control.getStyle());
        }
        Widget controlWidget = control.getWidget();
        this.add(controlWidget);
        isASourcesClickEvents = controlWidget instanceof SourcesClickEvents;
        if (isASourcesClickEvents) {
            ((SourcesClickEvents) controlWidget).addClickListener(new ClickListener() {

                public void onClick(Widget sender) {
                    fireEvent();
                }
            });
        } else {
            controlWidget.sinkEvents(events);
            DOM.setEventListener(controlWidget.getElement(), this);
        }
    }

    public final void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);
        if (!isASourcesClickEvents && ((DOM.eventGetType(event) & events) == 1)) {
            fireEvent();
        }
    }

    private void fireEvent() {
        control.getCommand(row).execute();
    }
}
