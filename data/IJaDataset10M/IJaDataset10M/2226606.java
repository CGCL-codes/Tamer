package net.da.client.service;

import net.da.client.dto.LoginRequestData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthorizeServiceAsync {

    public void loginUser(LoginRequestData logingRequest, AsyncCallback callback);
}
