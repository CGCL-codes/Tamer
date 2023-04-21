package net.sf.gilead.sample.client.login;

import net.sf.gilead.sample.domain.User;
import net.sf.gilead.sample.domain.exception.IdentificationException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * User update
 * @author bruno.marchesson
 *
 */
public interface LoginRemote extends RemoteService {

    /**
	 * Utility class for simplifing access to the instance of async service.
	 */
    public static class Util {

        private static LoginRemoteAsync instance;

        public static LoginRemoteAsync getInstance() {
            if (instance == null) {
                instance = (LoginRemoteAsync) GWT.create(LoginRemote.class);
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "/LoginRemote");
            }
            return instance;
        }
    }

    /**
	 * Authenticate a user
	 */
    public User authenticate(String login, String password) throws IdentificationException;
}
