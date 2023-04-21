package com.peterhi.web.client;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author YUN TAO
 */
public class View extends DockPanel {

    private Label titleLabel = new Label();

    private Widget body;

    public View(String title, Widget body) {
        setStyleName("gwt-View");
        titleLabel.setStyleName("gwt-View-title");
        titleLabel.setText(title);
        body.setWidth("100%");
        body.setHeight("100%");
        add(titleLabel, NORTH);
        add(body, CENTER);
    }
}
