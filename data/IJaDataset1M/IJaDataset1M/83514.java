package com.mycompany.mywebapp.server.manager;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mycompany.mywebapp.server.business.UserSession;
import com.mycompany.mywebapp.shared.JParameter;

public interface UserSessionServiceAsync {

    void login(JParameter params, AsyncCallback<UserSession> callback);

    void logout(AsyncCallback<Object> callback);

    void stepSendEmail(String email, AsyncCallback<Boolean> callback);

    void stepConfirmYourAccount(String username, String token, AsyncCallback<Boolean> callback);

    void stepResetPassword(String username, String token, String password, AsyncCallback<Boolean> callback);
}
