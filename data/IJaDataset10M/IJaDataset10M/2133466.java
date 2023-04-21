package edu.vt.eng.swat.workflow.app.global;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IEvaluationService;
import edu.vt.eng.swat.workflow.app.prop.Properties;
import edu.vt.eng.swat.workflow.db.base.DBFactory;
import edu.vt.eng.swat.workflow.db.base.entity.User;
import edu.vt.eng.swat.workflow.db.base.entity.UserType;
import edu.vt.eng.swat.workflow.preference.DefaultSettings;

/**
 * Manages actions with the application user.
 * 
 * @author Dmitry Churbanov (dmitry.churbanov@gmail.com)
 */
public class AppUser {

    private static User user;

    public static void login(User loginUser) {
        user = loginUser;
        updateProperties();
    }

    public static void logout() {
        user = null;
        updateProperties();
    }

    public static boolean isLoggedIn() {
        return user != null;
    }

    public static long getAccessType() {
        if (user == null) {
            return UserType.NONE;
        } else {
            return user.getType();
        }
    }

    public static Long getUserId() {
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    public static User copyUser() {
        if (user != null) {
            User userCopy = new User();
            userCopy.setName(user.getName());
            userCopy.setFirstName(user.getFirstName());
            userCopy.setLastName(user.getLastName());
            userCopy.setDescription(user.getDescription());
            userCopy.setEmail(user.getEmail());
            userCopy.setEnabled(user.getEnabled());
            userCopy.setPassword(user.getPassword());
            userCopy.setType(user.getType());
            return userCopy;
        }
        return null;
    }

    public static boolean changeUserPassword(String newPassword) {
        if (newPassword != null && newPassword.length() > 0) {
            user.setPassword(newPassword);
            User newUser = update(user);
            if (newUser != null) {
                user = newUser;
                return true;
            }
        }
        return false;
    }

    private static User update(User user) {
        try {
            return DBFactory.getFactory().getUserDao().update(user);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUserFirstLastNames() {
        if (user != null) {
            return user.getFirstName() + " " + user.getLastName();
        }
        return "";
    }

    /**
     * Notifies all listeners of the user properties that properties were updated.
     */
    public static void updateProperties() {
        IEvaluationService evaluationService = (IEvaluationService) PlatformUI.getWorkbench().getService(IEvaluationService.class);
        evaluationService.requestEvaluation(Properties.getVariable(Properties.USER_ACCESS_TYPE));
        try {
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            shell.setText(App.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
        DefaultSettings.processUserSettings();
    }
}
