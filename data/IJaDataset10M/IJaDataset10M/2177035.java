package org.jdna.bmt.web.client.ui.util;

import org.jdna.bmt.web.client.Application;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AsyncServiceReply<T> implements AsyncCallback<ServiceReply<T>> {

    public AsyncServiceReply() {
    }

    @Override
    public void onFailure(Throwable caught) {
        Dialogs.hideWaiting();
        Application.fireErrorEvent("Service Error", caught);
    }

    @Override
    public void onSuccess(ServiceReply<T> result) {
        Dialogs.hideWaiting();
        if (result.getCode() != 0) {
            onError(result);
        } else {
            onOK(result.getData());
        }
    }

    public void onError(ServiceReply<T> result) {
        Application.fireErrorEvent(result.getMessage());
    }

    public abstract void onOK(T result);
}
