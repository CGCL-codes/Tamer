package com.ufro.blackjack.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BlackJackGame implements EntryPoint {

    private Button clickMeButton;

    public void onModuleLoad() {
        RootPanel rootPanel = RootPanel.get();
        clickMeButton = new Button();
        rootPanel.add(clickMeButton);
        clickMeButton.setText("Click me!");
        clickMeButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                Window.alert("Hello, GWT World!");
            }
        });
    }
}
