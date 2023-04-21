package com.mobileares.midp.widgets.client.pop;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import com.mobileares.midp.widgets.client.icon.Icon;
import com.mobileares.midp.widgets.client.panel.EventFlowPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 2011-7-15
 * Time: 16:35:03
 * To change this template use File | Settings | File Templates.
 */
public class PopWidget extends Composite {

    private FlowPanel container;

    private SimplePanel title;

    FlowPanel body;

    private FlowPanel buttonPanel;

    private EventFlowPanel head;

    private Icon close;

    FocusPanel main;

    private FlowPanel iconBar;

    private SimplePanel shadow;

    private ClickHandler closeHandler;

    public PopWidget() {
        container = new FlowPanel();
        container.setStyleName("pop");
        shadow = new SimplePanel();
        shadow.setStyleName("pop-shadow");
        shadow.add(container);
        initHead();
        initBody();
        initFooter();
        initWidget(shadow);
    }

    public void setCloseHandler(ClickHandler closeHandler) {
        this.closeHandler = closeHandler;
    }

    public void setHeadText(String mess) {
        DOM.setInnerHTML(title.getElement(), mess);
    }

    private void initHead() {
        head = new EventFlowPanel();
        head.setStyleName("head");
        title = new SimplePanel();
        title.setStyleName("title");
        SimplePanel left = new SimplePanel();
        left.setStyleName("left");
        SimplePanel right = new SimplePanel();
        right.setStyleName("right");
        main = new FocusPanel();
        main.setStyleName("main");
        main.add(title);
        head.add(left);
        head.add(right);
        head.add(getIconBar());
        head.add(main);
        container.add(head);
    }

    private Widget getIconBar() {
        iconBar = new FlowPanel();
        iconBar.setStyleName("icon");
        close = new Icon("fishimages/pop/b.png", "fishimages/pop/a.png");
        iconBar.add(close);
        close.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (closeHandler != null) closeHandler.onClick(event);
            }
        });
        return iconBar;
    }

    private void initBody() {
        body = new FlowPanel();
        body.setStyleName("body");
        container.add(body);
    }

    public FlowPanel getBodyWidget() {
        return body;
    }

    private void initFooter() {
        SimplePanel footer = new SimplePanel();
        footer.setStyleName("footer");
        buttonPanel = new FlowPanel();
        buttonPanel.setStyleName("bt");
        footer.add(buttonPanel);
        container.add(footer);
    }

    public FlowPanel getButtonWidget() {
        return buttonPanel;
    }
}
