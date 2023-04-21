package eu.future.earth.gwt.client.date.week;

import java.util.Date;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import eu.future.earth.gwt.client.FtrGwtDateCss;
import eu.future.earth.gwt.client.date.DateEvent;
import eu.future.earth.gwt.client.date.DateEventListener;
import eu.future.earth.gwt.client.date.DateRenderer;
import eu.future.earth.gwt.client.date.HasDateEventHandlers;
import eu.future.earth.gwt.client.date.DateEvent.DateEventActions;

public class DayHeader<T> extends SimplePanel implements ClickHandler, HasDateEventHandlers<T> {

    final Label label = new Label();

    private Date date = null;

    private DateRenderer<T> renderer = null;

    public DayHeader(Date newDate, DateRenderer<T> newRenderer) {
        super();
        renderer = newRenderer;
        setDate(newDate);
        label.setWidth("100%");
        label.setHorizontalAlignment(Label.ALIGN_CENTER);
        if (renderer.supportDayView() || renderer.headerClickable()) {
            label.setStyleName(FtrGwtDateCss.DAY_HEADER_LABEL_CLICKABLE);
            label.addClickHandler(this);
        } else {
            label.setStyleName(FtrGwtDateCss.DAY_HEADER_LABEL);
        }
        super.setWidget(label);
    }

    public void setDate(Date newDate) {
        date = newDate;
        if (!renderer.gotoDayViewOnClick()) {
            label.setText(renderer.getDayHeaderDateTimeFormatter().format(newDate) + "*");
        } else {
            label.setText(renderer.getDayHeaderDateTimeFormatter().format(newDate));
        }
    }

    public void onClick(ClickEvent newEvent) {
        if (!renderer.gotoDayViewOnClick()) {
            DateEvent.fire(this, date, DateEventActions.DAY_SELECTED);
        } else {
            DateEvent.fire(this, date, DateEventActions.GO_TO_DAY_VIEW);
        }
    }

    public HandlerRegistration addDateEventHandler(DateEventListener<? extends T> handler) {
        return addHandler(handler, DateEvent.getType());
    }
}
