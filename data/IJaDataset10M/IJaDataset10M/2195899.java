package com.vinay.innos.jideos.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthenticationAsync {

    public void login(String user, String password, AsyncCallback callback);
}
