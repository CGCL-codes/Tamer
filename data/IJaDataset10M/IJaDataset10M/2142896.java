package com.google.solarchallenge.client;

import com.google.gwt.user.client.History;
import com.google.solarchallenge.client.event.HistoryHandler;

/**
 * This is the client side controller for the GWT app.
 *
 * @author Arjun Satyapal
 */
public class AppController {

    public AppController() {
        History.addValueChangeHandler(HistoryHandler.getInstance());
    }

    public void go() {
        String historyToken = History.getToken();
        HistoryHandler.handleNewToken(historyToken);
    }
}
