package de.tum.team26.eistpoll.client.UI;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.tum.team26.eistpoll.client.EIST_POLL;

/**
 * Class to show the Inactive Panel
 */
public class Inactive extends UIElement {

    Label lblHeadding = new Label("Sorry, there are currently no polls active. Please come back later!");

    Label lblHeaddingh1 = new Label("Poll Inactive");

    VerticalPanel inactivePanel = new VerticalPanel();

    Label lblErrMsg;

    HorizontalPanel headder = new HorizontalPanel();

    Label btnLogin = new Label("Login");

    Image tum = new Image();

    Image info = new Image();

    EIST_POLL controller;

    /**
	 * Initialise Panel
	 * @param controller
	 */
    public Inactive(EIST_POLL controller) {
        this.controller = controller;
        btnLogin.addStyleName("btlogin");
        tum.setStyleName("tumlogo");
        info.setStyleName("infologo");
        tum.setUrl("tum.gif");
        info.setUrl("info.gif");
        headder.add(btnLogin);
        headder.add(tum);
        headder.add(info);
        headder.setStyleName("headder");
        inactivePanel.add(headder);
        lblHeaddingh1.setStyleName("h1");
        inactivePanel.add(lblHeaddingh1);
        inactivePanel.add(lblHeadding);
        inactivePanel.addStyleName("container");
        btnLogin.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                triggerLogin();
            }
        });
    }

    private void triggerLogin() {
        controller.triggerLogin();
    }

    @Override
    public VerticalPanel getContainer() {
        return inactivePanel;
    }

    @Override
    public void displayErrorMessage(String message) {
        if (lblErrMsg == null) {
            lblErrMsg = new Label(message);
            lblErrMsg.setStyleName("error");
            inactivePanel.add(lblErrMsg);
        } else {
            lblErrMsg.setText(message);
        }
    }
}
