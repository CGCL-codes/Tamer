package com.ganzhavitaliy.copslocator.gae.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GeoServiceAsync {

    void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
}
