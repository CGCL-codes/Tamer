package com.gwttest.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ImageServiceAsync {

    public void getImageToken(String base64image, AsyncCallback<String> callback);
}
