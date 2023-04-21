package com.eventbusapp.client.view;

import com.eventbusapp.client.event.ClearEvent;
import com.eventbusapp.client.event.ClearEventHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.TextBox;

public class TextBoxView extends TextBox {

    public TextBoxView(SimpleEventBus eventBus) {
        eventBus.addHandler(ClearEvent.TYPE, new ClearEventHandler() {

            public void clear(ClearEvent event) {
                setText("");
            }
        });
    }
}
