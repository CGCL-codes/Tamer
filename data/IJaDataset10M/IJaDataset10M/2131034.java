package org.jazzteam.shareideas.service.implementation;

import javax.servlet.http.HttpSession;
import org.jazzteam.shareideas.exceptions.GeneralServiceException;
import org.jazzteam.shareideas.model.User;
import org.jazzteam.shareideas.service.IAuthService;
import org.jazzteam.shareideas.service.IUserService;

/**
 * Implements users authendification process.
 * 
 * Roles: Access control, login, logout,
 * 
 * @author ADLeR
 * @version $Rev: $
 */
public class AuthServiceReal implements IAuthService {

    private static final String sesUserID = "user";

    private IUserService userService;

    private HttpSession httpSession;

    public AuthServiceReal() {
    }

    @Override
    public void setSession(HttpSession session) {
        this.httpSession = session;
    }

    @Override
    public boolean isUserLogged() throws GeneralServiceException {
        if (httpSession == null) {
            throw new GeneralServiceException("AuthService: http session wasn't initialised.");
        }
        if (httpSession.getAttribute(sesUserID) != null) {
            return true;
        }
        return false;
    }

    @Override
    public User getCurrentUser() throws GeneralServiceException {
        if (httpSession == null) {
            throw new GeneralServiceException("AuthService: http session wasn't initialised.");
        }
        Long userId = (Long) httpSession.getAttribute(sesUserID);
        if (userId != null) {
            return userService.get(userId.longValue());
        }
        return null;
    }

    @Override
    public boolean login(String login, String password) throws IllegalArgumentException, GeneralServiceException {
        if (login == null || password == null) {
            throw new IllegalArgumentException();
        }
        if (httpSession == null) {
            throw new GeneralServiceException("Authendification service: http session wasn't initialised.");
        }
        User user = userService.get(login);
        if (user != null) {
            httpSession.setAttribute(sesUserID, user.getId());
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        httpSession.setAttribute(sesUserID, null);
    }

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
}
