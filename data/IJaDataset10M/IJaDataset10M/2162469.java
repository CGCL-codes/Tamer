package net.lukemurphey.nsia.web.views;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.lukemurphey.nsia.Application;
import net.lukemurphey.nsia.DisallowedOperationException;
import net.lukemurphey.nsia.GeneralizedException;
import net.lukemurphey.nsia.InputValidationException;
import net.lukemurphey.nsia.NoDatabaseConnectionException;
import net.lukemurphey.nsia.UserManagement;
import net.lukemurphey.nsia.UserManagement.UserDescriptor;
import net.lukemurphey.nsia.eventlog.EventLogField;
import net.lukemurphey.nsia.eventlog.EventLogMessage;
import net.lukemurphey.nsia.eventlog.EventLogField.FieldName;
import net.lukemurphey.nsia.web.RequestContext;
import net.lukemurphey.nsia.web.Shortcuts;
import net.lukemurphey.nsia.web.URLInvalidException;
import net.lukemurphey.nsia.web.View;
import net.lukemurphey.nsia.web.ViewFailedException;
import net.lukemurphey.nsia.web.ViewNotFoundException;
import net.lukemurphey.nsia.web.SessionMessages.MessageSeverity;
import net.lukemurphey.nsia.web.views.Dialog.DialogType;

public class UserDisableView extends View {

    public UserDisableView() {
        super("User/Disable", "user_disable", Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE));
    }

    public static String getURL(UserDescriptor user) throws URLInvalidException {
        UserDisableView view = new UserDisableView();
        return view.createURL(user.getUserID());
    }

    public boolean disableUser(RequestContext context, int userID) throws ViewFailedException, DisallowedOperationException {
        Application app = Application.getApplication();
        try {
            UserManagement userManagement = new UserManagement(Application.getApplication());
            if (context.getUser().getUserID() == userID) {
                throw new DisallowedOperationException("Users are not allowed to disable their own account");
            }
            if (userManagement.disableAccount(userID)) {
                app.logEvent(EventLogMessage.EventType.USER_DISABLED, new EventLogField(FieldName.TARGET_USER_ID, userID), new EventLogField(FieldName.SOURCE_USER_NAME, context.getUser().getUserName()), new EventLogField(FieldName.SOURCE_USER_ID, context.getUser().getUserID()));
                return true;
            } else {
                app.logEvent(EventLogMessage.EventType.USER_ID_INVALID, new EventLogField(FieldName.TARGET_USER_ID, userID), new EventLogField(FieldName.SOURCE_USER_NAME, context.getUser().getUserName()), new EventLogField(FieldName.SOURCE_USER_ID, context.getUser().getUserID()));
                return true;
            }
        } catch (SQLException e) {
            app.logExceptionEvent(EventLogMessage.EventType.SQL_EXCEPTION, e);
            throw new ViewFailedException(e);
        } catch (NoDatabaseConnectionException e) {
            app.logExceptionEvent(EventLogMessage.EventType.DATABASE_FAILURE, e);
            throw new ViewFailedException(e);
        } catch (InputValidationException e) {
            app.logExceptionEvent(EventLogMessage.EventType.INTERNAL_ERROR, e);
            throw new ViewFailedException(e);
        }
    }

    @Override
    protected boolean process(HttpServletRequest request, HttpServletResponse response, RequestContext context, String[] args, Map<String, Object> data) throws ViewFailedException, URLInvalidException, IOException, ViewNotFoundException {
        int userID = -1;
        if (args.length <= 0) {
            Dialog.getDialog(response, context, data, "The User ID was not provided.", "User ID Invalid", DialogType.WARNING);
            return true;
        } else {
            try {
                userID = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                Dialog.getDialog(response, context, data, "The User ID provided is invalid.", "User ID Invalid", DialogType.WARNING);
                return true;
            }
        }
        try {
            if (Shortcuts.hasRight(context.getSessionInfo(), "Users.Edit", "Disable user ID " + userID) == false) {
                context.addMessage("You do not have permission to disable user accounts", MessageSeverity.WARNING);
                response.sendRedirect(UserView.getURL(userID));
                return true;
            }
        } catch (GeneralizedException e) {
            throw new ViewFailedException(e);
        }
        try {
            disableUser(context, userID);
            context.addMessage("User successfully disabled", MessageSeverity.SUCCESS);
        } catch (DisallowedOperationException e) {
            context.addMessage("You cannot disable your own account", MessageSeverity.WARNING);
        }
        response.sendRedirect(UserView.getURL(userID));
        return true;
    }
}
