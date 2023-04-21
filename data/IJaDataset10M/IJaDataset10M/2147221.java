package com.hellomvp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.hellomvp.client.place.GoodbyePlace;
import com.hellomvp.client.ui.PresenterNavigator;

public class HelloLeftPanel extends Composite {

    private static LeftPanelUiBinder uiBinder = GWT.create(LeftPanelUiBinder.class);

    @UiField
    Button butGoodBye;

    private PresenterNavigator navigator;

    interface LeftPanelUiBinder extends UiBinder<Widget, HelloLeftPanel> {
    }

    @UiHandler("butGoodBye")
    void onClickGoodbye(ClickEvent e) {
        navigator.goTo(new GoodbyePlace());
    }

    public HelloLeftPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setName(String name) {
        butGoodBye.setText("Goodbye " + name);
    }

    public void setNavigator(PresenterNavigator navigator) {
        this.navigator = navigator;
    }
}
