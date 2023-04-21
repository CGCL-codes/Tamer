package com.gwt.tutorial.LoginManager.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LoginManager implements EntryPoint {

    public void onModuleLoad() {
        RootPanel rootPanel = RootPanel.get();
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        rootPanel.add(horizontalPanel, 10, 10);
        horizontalPanel.setSize("430px", "280px");
        VerticalPanel verticalPanel = new VerticalPanel();
        horizontalPanel.add(verticalPanel);
        Label lblWelcome = new Label("Welcome to my login page");
        lblWelcome.setStyleName("gwt-Label-Login");
        verticalPanel.add(lblWelcome);
        Login login = new Login();
        horizontalPanel.add(login);
    }
}
