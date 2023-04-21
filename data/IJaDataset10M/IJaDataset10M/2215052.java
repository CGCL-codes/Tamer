package de.schlund.pfixcore.editor2.core.spring;

import java.security.Principal;
import org.pustefixframework.editor.common.exception.EditorSecurityException;
import org.pustefixframework.editor.common.exception.EditorUserNotExistingException;
import de.schlund.pfixcore.editor2.core.vo.EditorUser;
import de.schlund.pfixcore.util.UnixCrypt;

public class UserPasswordAuthenticationServiceImpl implements UserPasswordAuthenticationService {

    private UserManagementService usermanagement;

    private boolean allowUserLogins = true;

    private SecurityManagerService securitymanager;

    public void setUserManagementService(UserManagementService usermanagement) {
        this.usermanagement = usermanagement;
    }

    public void setSecurityManagerService(SecurityManagerService securitymanager) {
        this.securitymanager = securitymanager;
    }

    public Principal getPrincipalForUser(String username, String password) {
        EditorUser user;
        try {
            user = this.usermanagement.getUser(username);
        } catch (EditorUserNotExistingException e) {
            return null;
        }
        if (!UnixCrypt.matches(user.getCryptedPassword(), password)) {
            return null;
        }
        if (!isAllowUserLogins() && !user.getGlobalPermissions().isAdmin()) {
            return null;
        }
        return new PrincipalImpl(username);
    }

    private class PrincipalImpl implements Principal {

        private String username;

        private PrincipalImpl(String username) {
            this.username = username;
        }

        public String getName() {
            return this.username;
        }
    }

    public void setAllowUserLogins(boolean flag) throws EditorSecurityException {
        this.securitymanager.checkAdmin();
        this.allowUserLogins = flag;
    }

    public boolean isAllowUserLogins() {
        return this.allowUserLogins;
    }
}
