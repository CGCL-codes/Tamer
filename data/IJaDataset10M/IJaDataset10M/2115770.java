package com.skillworld.webapp.web.pages.rest.user;

import org.apache.tapestry5.annotations.ContentType;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import com.skillworld.webapp.model.userservice.NicknameAlreadyInUseException;
import com.skillworld.webapp.model.userservice.UserService;
import com.skillworld.webapp.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@ContentType("text/xml")
public class EditMyProfile {

    @SuppressWarnings("unused")
    @Property
    private String errorMessage = null;

    @SessionState(create = false)
    private UserSession userSession;

    private boolean userSessionExists;

    @Inject
    private UserService userService;

    @Inject
    private Request request;

    void onPassivate() {
        return;
    }

    void onActivate() {
        if (!userSessionExists) {
            errorMessage = "not-logged-in";
            return;
        }
        String nickname = request.getParameter("nick");
        String skilly = request.getParameter("skilly");
        String picture = request.getParameter("pic");
        if (nickname == null || skilly == null || picture == null) {
            errorMessage = "invalid-arguments";
            return;
        }
        try {
            userService.updateUserProfile(userSession.getUserId(), nickname, skilly, picture);
        } catch (NicknameAlreadyInUseException e) {
            errorMessage = "nickname-already-in-use";
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
