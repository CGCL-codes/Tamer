package br.com.unitri.blog.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CounterServiceAsync {

    void getCurrentValue(AsyncCallback<Integer> callback);
}
