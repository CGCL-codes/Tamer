package com.mobileares.midp.widgets.client.tab;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 2011-7-21
 * Time: 10:13:19
 * To change this template use File | Settings | File Templates.
 */
public class VTabItem extends Composite implements HasClickHandlers {

    private FlowPanel container;

    private Label text;

    private SimplePanel top;

    private SimplePanel middle;

    private SimplePanel bottom;

    private Widget widget;

    private Object userObject;

    public VTabItem(String text, Widget widget) {
        container = new FlowPanel();
        container.setStyleName("u2-vtab-item");
        top = new SimplePanel();
        middle = new SimplePanel();
        bottom = new SimplePanel();
        this.text = new Label(text);
        this.text.setStyleName("mtext");
        container.add(top);
        container.add(middle);
        container.add(bottom);
        middle.add(this.text);
        this.widget = widget;
        initWidget(container);
        setStyle(false);
    }

    public Widget getWidget() {
        return widget;
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    /**
     * Adds a {@link com.google.gwt.event.dom.client.ClickEvent} handler.
     *
     * @param handler the click handler
     * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove this handler
     */
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    void setStyle(boolean selected) {
        if (selected) {
            top.setStyleName("top-s");
            middle.setStyleName("middle-s");
            bottom.setStyleName("bottom-s");
        } else {
            top.setStyleName("top");
            middle.setStyleName("middle");
            bottom.setStyleName("bottom");
        }
    }
}
