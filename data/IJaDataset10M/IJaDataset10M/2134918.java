package com.mauricerogers.icontact.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBCheckerServiceAsync {

    void isConnectionSuccessful(AsyncCallback<Boolean> callback);
}
