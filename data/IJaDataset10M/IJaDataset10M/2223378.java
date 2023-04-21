package org.systemsbiology.apps.gui.client.rpc;

import org.systemsbiology.apps.gui.client.controller.request.LoginRequest;
import org.systemsbiology.apps.gui.client.controller.request.LogoutRequest;
import org.systemsbiology.apps.gui.client.controller.request.PasswordChangeRequest;
import org.systemsbiology.apps.gui.domain.User;
import org.systemsbiology.apps.gui.client.exception.AuthenticationException;
import org.systemsbiology.apps.gui.client.exception.SRMException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface for login information service  
 * 
 * @author Mark Christiansen
 *
 */
@RemoteServiceRelativePath("Login")
public interface LoginService extends RemoteService {

    public User loginUser(LoginRequest loginRequest);

    public void logoutUser(LogoutRequest logoutRequest);

    public static class Util {

        public static LoginServiceAsync getInstance() {
            LoginServiceAsync instance = (LoginServiceAsync) GWT.create(LoginService.class);
            return instance;
        }
    }
}
