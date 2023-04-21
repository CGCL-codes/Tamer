package com.skillworld.webapp.web.pages.rest.user;

import org.apache.tapestry5.annotations.ContentType;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import com.skillworld.webapp.model.userservice.NotTeamMemberException;
import com.skillworld.webapp.model.userservice.UserService;
import com.skillworld.webapp.model.util.PermissionDeniedException;
import com.skillworld.webapp.web.util.ErrorMessages;
import com.skillworld.webapp.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@ContentType("text/xml")
public class LeaveTeam {

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
            errorMessage = ErrorMessages.NOT_LOGGED_IN;
            return;
        }
        String teamParam = request.getParameter("team");
        if (teamParam == null) {
            this.errorMessage = ErrorMessages.INVALID_ARGUMENTS;
            return;
        }
        try {
            long teamId = Long.parseLong(teamParam);
            this.userService.leaveTeam(teamId, userSession.getUserId());
        } catch (InstanceNotFoundException e) {
            errorMessage = ErrorMessages.INSTANCE_NOT_FOUND;
        } catch (PermissionDeniedException e) {
            errorMessage = ErrorMessages.PERMISSION_DENIED;
        } catch (NotTeamMemberException e) {
            errorMessage = ErrorMessages.NOT_A_TEAM_MEMBER;
        }
    }
}
