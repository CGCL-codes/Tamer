package it.newinstance.spikes.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HelloServiceAsync {

    void sayHello(AsyncCallback<HelloObject> callback);
}
