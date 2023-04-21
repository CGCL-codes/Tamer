package com.manning.gwtip.user.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {

    public void createUser(User user, AsyncCallback callback);
}
