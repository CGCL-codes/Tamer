package portal.presentation.prefs;

import java.util.*;
import hambo.jndi.UserBO;
import hambo.app.util.Link;
import hambo.user.*;
import hambo.util.OID;
import hambo.*;
import hambo.util.Crypto;
import hambo.app.base.RedirectorBase;

/**
 * Validates and executes a password change. If the change is denied, the user
 * is redirected back where he came from with an error message. Else he is
 * redirected to some other page.
 */
public class ModifyPasswordValidator extends RedirectorBase {

    private Properties userData = new Properties();

    public void processPage() throws Exception {
        if (getParameter("cancel") != null) throwRedirect("prefsmain");
        String current = getParameter("oldpassword");
        Crypto crypto = Crypto.getInstance();
        String encrypted = crypto.encrypt(current);
        User user = null;
        try {
            UserManager userManager = UserManager.getUserManager();
            user = userManager.findUser(user_id, encrypted);
            if (user == null) formError("(@err_authfailed2@)");
        } catch (Exception ex) {
            logError("LoginValidator: Failed to lookup user in database.", ex);
            formError("(@err_authfailed2@)");
        }
        userData.setProperty(HamboUser.USER_ID, user_id);
        String password1 = getParameter(HamboUser.PASSWORD);
        userData.setProperty(HamboUser.PASSWORD, password1);
        String password2 = getParameter(HamboUser.CONFIRM_PASSWORD);
        userData.setProperty(HamboUser.CONFIRM_PASSWORD, password2);
        Map validData = null;
        try {
            validData = UserDataValidator.validate(userData, user, UserDataValidator.PASSWORD_UPDATE_MODE);
        } catch (MissingDataException ex) {
            logDebug3("ModifyPasswordValidator: " + ex.getMessage());
            formError(ex.getMessage());
        } catch (UserDataValidationException ex) {
            logDebug3("ModifyPasswordValidator: " + ex.getMessage() + "\n" + ex.getTranslatedMessage());
            formError(ex.getTranslatedMessage());
        } catch (Exception ex) {
            logError("ModifyPasswordValidator: When validating user data.", ex);
            formError("(@err_failed@)");
        }
        user.getAccountInfo().setPassword((String) validData.get(HamboUser.PASSWORD));
        try {
            UserManager userManager = UserManager.getUserManager();
            userManager.updateUser(user);
        } catch (Exception ex) {
            logError("Modify Password: Couldn't update user in database.", ex);
            formError("(@err_failed@)");
        }
        try {
            if (UserBO.useLdap()) {
                UserBO bo = new UserBO(user_id);
                bo.updateLDAP();
            }
        } catch (Exception ex) {
            logError("Failed to update LDAP at login.", ex);
        }
        fireUserEvent("prPasswordChanged");
        okPass("(@err_passwdok@)");
    }

    public void formError(String errorMsg) {
        Link errTarget = new Link("prefsuser2");
        throwRedirect(errTarget, errorMsg);
    }

    public void okPass(String msg) {
        Link errTarget = new Link("prefsmain");
        throwRedirect(errTarget, msg);
    }
}
