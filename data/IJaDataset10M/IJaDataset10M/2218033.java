package com.kenstevens.stratinit.client.gwtservice;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTRegisterServiceAsync {

    void register(String username, String password, String email, AsyncCallback<GWTResult<GWTNone>> callback);
}
