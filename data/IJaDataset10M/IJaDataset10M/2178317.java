package com.gwtaf.core.client.util;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtaf.core.client.error.GlobalErrorHandler;
import com.gwtaf.core.shared.util.VOID;

public abstract class AsyncCallbackAdapter<T> implements AsyncCallback<T> {

    /**
   * Default callback for <i>void</i> services.<br>
   * Such a service only has to return a Boolean.
   */
    public static final AsyncCallback<VOID> voidCallback = new AsyncCallbackAdapter<VOID>() {

        public void onSuccess(VOID result) {
        }
    };

    public void onFailure(Throwable caught) {
        GlobalErrorHandler.get().onUncaughtException(caught);
    }
}
