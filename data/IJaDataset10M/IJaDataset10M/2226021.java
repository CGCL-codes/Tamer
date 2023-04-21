package org.systemsbiology.apps.corragui.client.rpc;

import org.systemsbiology.apps.corragui.client.controller.request.LoginRequest;
import org.systemsbiology.apps.corragui.client.controller.request.LogoutRequest;
import org.systemsbiology.apps.corragui.client.controller.request.PasswordChangeRequest;
import org.systemsbiology.apps.corragui.domain.User;
import org.systemsbiology.apps.corragui.domain.exception.AuthenticationException;
import org.systemsbiology.apps.corragui.domain.exception.CorraException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Login")
public interface LoginService extends RemoteService {

    public User loginUser(LoginRequest loginRequest);

    public void logoutUser(LogoutRequest logoutRequest);

    public void changeUserPassword(PasswordChangeRequest passChangeRequest) throws AuthenticationException, CorraException;

    public static class Util {

        public static LoginServiceAsync getInstance() {
            LoginServiceAsync instance = (LoginServiceAsync) GWT.create(LoginService.class);
            return instance;
        }
    }
}
