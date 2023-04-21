package com.wle.client.core.handlers;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wle.client.core.api.ServerExceptions;
import com.wle.client.core.events.GUIEventPublisherHandlerInterface;

public class AuthenticationHandler implements AsyncCallback<Object> {

    private GUIEventPublisherHandlerInterface publisher;

    public AuthenticationHandler(GUIEventPublisherHandlerInterface publisher) {
        this.publisher = publisher;
    }

    public void onFailure(Throwable error) {
        ServerExceptions se;
        try {
            throw error;
        } catch (ServerExceptions e) {
            se = e;
        } catch (Throwable t) {
            se = new ServerExceptions(t.getMessage());
        }
        publisher.authenticationFailed(se);
    }

    public void onSuccess(Object result) {
        publisher.authenticationPassed();
    }
}
