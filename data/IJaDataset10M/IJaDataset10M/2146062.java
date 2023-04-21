package com.skillworld.webapp.web.pages.rest.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.tapestry5.annotations.ContentType;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import com.skillworld.webapp.model.user.User;
import com.skillworld.webapp.model.userservice.AlreadyRegisteredException;
import com.skillworld.webapp.model.userservice.InvalidFacebookTokenException;
import com.skillworld.webapp.model.userservice.NicknameAlreadyInUseException;
import com.skillworld.webapp.model.userservice.UserService;
import com.skillworld.webapp.web.util.ErrorMessages;
import com.skillworld.webapp.web.util.UserSession;

@ContentType("text/xml")
public class FacebookSignUp {

    @SuppressWarnings("unused")
    @Property
    private String errorMessage = null;

    @Property
    private User user;

    @SuppressWarnings("unused")
    @Property
    private String jsessionId = "foo";

    @Inject
    private UserService userService;

    @SuppressWarnings("unused")
    @SessionState(create = false)
    private UserSession userSession;

    private boolean userSessionExists;

    @Inject
    private Request request;

    @Inject
    private HttpServletRequest httpServletRequest;

    void onPassivate() {
        return;
    }

    void onActivate() {
        if (userSessionExists) {
            errorMessage = ErrorMessages.ALREADY_LOGGED_IN;
            return;
        }
        String facebookId = request.getParameter("fb");
        String authorizationToken = request.getParameter("token");
        String nickname = request.getParameter("nick");
        String skilly = request.getParameter("skilly");
        String picture = request.getParameter("pic");
        if (facebookId == null || authorizationToken == null || nickname == null || skilly == null || picture == null) {
            errorMessage = ErrorMessages.INVALID_ARGUMENTS;
            return;
        }
        try {
            user = userService.registerFacebookUser(facebookId, authorizationToken, nickname, skilly, picture);
            HttpSession httpSession = httpServletRequest.getSession(true);
            jsessionId = httpSession.getId();
            httpSession.setMaxInactiveInterval(2 * 3600);
            userSession = new UserSession(user.getUserId(), facebookId, authorizationToken);
        } catch (AlreadyRegisteredException e) {
            errorMessage = ErrorMessages.ALREADY_REGISTERED;
        } catch (NicknameAlreadyInUseException e) {
            errorMessage = ErrorMessages.NICKNAME_ALREADY_IN_USE;
        } catch (InvalidFacebookTokenException e) {
            errorMessage = ErrorMessages.INVALID_AUTHORIZATION_TOKEN;
        }
    }
}
