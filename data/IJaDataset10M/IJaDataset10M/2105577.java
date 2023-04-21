package br.com.unitri.blog.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

class AsyncCall<T> implements AsyncCallback<T> {

    @Override
    public void onFailure(Throwable caught) {
        Window.alert("Ooops");
        GWT.log("Ooops", caught);
    }

    @Override
    public void onSuccess(T result) {
    }
}
