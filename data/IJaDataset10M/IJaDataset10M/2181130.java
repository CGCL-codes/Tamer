package com.googlecode.gwt_control.client;

import com.google.gwt.user.client.ui.Hyperlink;

/**
 *
 * @author Olafur Gauti Gudmundsson
 */
public class ViewLink extends Hyperlink {

    public ViewLink(String text, Controller destination) {
        super(text, destination.getViewLinkToken().toString());
    }

    public void setDestination(Controller destination) {
        setTargetHistoryToken(destination.getViewLinkToken().toString());
    }
}
