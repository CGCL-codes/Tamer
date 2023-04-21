package com.mobileares.midp.widgets.client.pop;

import com.allen_sauer.gwt.dnd.client.*;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mobileares.midp.widgets.client.panel.GlassPanel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 2011-4-1
 * Time: 15:43:26
 * To change this template use File | Settings | File Templates.
 */
public class PopPanel {

    private List<PopClosedListener> listener;

    private GlassPanel glass;

    private boolean isGlass = true;

    private PopWidget widget;

    private HorizontalPanel wrapper;

    public PopPanel() {
        this(true);
    }

    public PopPanel(boolean drag) {
        widget = new PopWidget();
        widget.setCloseHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                hidden();
            }
        });
        wrapper = new HorizontalPanel();
        wrapper.add(widget);
        DOM.setStyleAttribute(wrapper.getElement(), "zIndex", "1000");
        if (drag) initDrag();
    }

    public PopWidget getPopWidget() {
        return widget;
    }

    public void setGlass(boolean enable) {
        this.isGlass = enable;
    }

    public void addBodyWidget(Widget ui) {
        widget.getBodyWidget().add(ui);
    }

    public void addFootButton(Button button) {
        widget.getButtonWidget().add(button);
    }

    public void setWidth(String width) {
        widget.setWidth(width);
    }

    public void show() {
        if (isGlass) {
            glass = GlassPanel.getInstance();
            glass.show();
        }
        PopHelper.center(wrapper);
    }

    public void hidden() {
        fireCloseListener();
        PopHelper.hidden(wrapper);
        if (isGlass) {
            glass.hidden();
        }
    }

    public void setHeadText(String mess) {
        widget.setHeadText(mess);
    }

    public void addClosedListener(PopClosedListener cl) {
        if (listener == null) listener = new ArrayList<PopClosedListener>();
        listener.add(cl);
    }

    protected void fireCloseListener() {
        if (listener != null) {
            for (PopClosedListener li : listener) {
                li.fire(PopPanel.this);
            }
        }
    }

    private void initDrag() {
        PickupDragController pdc;
        pdc = new PickupDragController(RootPanel.get(), false);
        pdc.addDragHandler(new DragHandler() {

            public void onDragEnd(DragEndEvent event) {
            }

            public void onDragStart(DragStartEvent event) {
            }

            public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
                RootPanel.get().getElement().getStyle().clearProperty("position");
            }

            public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
                RootPanel.get().getElement().getStyle().setProperty("position", "relative");
            }
        });
        pdc.makeDraggable(wrapper, widget.main);
        AbsolutePositionDropController dropController = new AbsolutePositionDropController(RootPanel.get());
        pdc.registerDropController(dropController);
    }
}
