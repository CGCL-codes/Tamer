package com.habitsoft.kiyaa.views;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.habitsoft.kiyaa.util.AsyncCallbackFactory;
import com.habitsoft.kiyaa.util.AsyncCallbackShared;
import com.habitsoft.kiyaa.util.AsyncCallbackWithTimeout;

/**
 * View which returns immediately from load() without waiting for its
 * subview(s) to load.
 */
public class NoWaitView implements View {

    View view;

    AsyncCallbackShared<Void> loadInProgress;

    public void clearFields() {
        view.clearFields();
    }

    public Widget getViewWidget() {
        return view.getViewWidget();
    }

    public void load(AsyncCallback<Void> callback) {
        callback.onSuccess(null);
        loadInProgress = new AsyncCallbackShared<Void>(AsyncCallbackFactory.<Void>defaultNewInstance()) {

            @Override
            public void onSuccess(Void result) {
                loadInProgress = null;
                super.onSuccess(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                loadInProgress = null;
                super.onFailure(caught);
            }
        };
        view.load(new AsyncCallbackWithTimeout<Void>(loadInProgress, view));
    }

    public void save(final AsyncCallback<Void> callback) {
        if (loadInProgress != null) {
            loadInProgress.addCallback(new AsyncCallback<Void>() {

                public void onSuccess(Void result) {
                    view.save(callback);
                }

                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }
            });
        } else {
            view.save(callback);
        }
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
