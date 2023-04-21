package com.google.gwt.examples;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class ButtonExample implements EntryPoint {

    public void onModuleLoad() {
        Button b = new Button("Jump!", new ClickHandler() {

            public void onClick(ClickEvent event) {
                Window.alert("How high?");
            }
        });
        RootPanel.get().add(b);
    }
}
