package org.elip.stewiemaze.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FacebookServiceAsync {

    void getUsername(String accessToken, AsyncCallback<String> callback);
}
