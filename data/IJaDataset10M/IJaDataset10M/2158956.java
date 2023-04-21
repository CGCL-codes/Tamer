package com.cofluent.web.client;

import com.cofluent.web.client.model.Build;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BuildInformationServiceAsync {

    public void refresh(AsyncCallback callback);

    public void getBuilds(AsyncCallback callback);

    public void delete(Build build, AsyncCallback callback);
}
