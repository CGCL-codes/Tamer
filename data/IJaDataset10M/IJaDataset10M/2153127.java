package com.foo.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BarServiceAsync {

    public void echo(String message, AsyncCallback callback);
}
