package edu.columbia.hypercontent;

import org.jasig.portal.groups.IGroupMember;
import org.jasig.portal.security.IAuthorizationPrincipal;
import org.jasig.portal.security.IAuthorizationService;
import org.jasig.portal.security.IPermission;
import org.jasig.portal.security.IPermissionPolicy;
import org.jasig.portal.services.AuthorizationService;

/**
 * Part of the Columbia University Content Management Suite
 *
 * @author Alex Vigdor av317@columbia.edu
 * @version $Revision: 1.1.1.1 $
 */
public class CMSPermissionsPolicy implements IPermissionPolicy, ICMSConstants {

    private static CMSPermissionsPolicy _instance;

    private CMSPermissionsPolicy() {
    }

    public boolean doesPrincipalHavePermission(IAuthorizationService service, IAuthorizationPrincipal principal, String owner, String activity, String target) throws org.jasig.portal.AuthorizationException {
        if (target == null) {
            return false;
        } else {
            if (target.indexOf("*") >= 0) {
                target = target.substring(0, target.indexOf("*"));
            }
            IPermission permission = null;
            IPermission[] perms = service.getAllPermissionsForPrincipal(principal, owner, activity, null);
            for (int i = 0; i < perms.length; i++) {
                if (target.startsWith(perms[i].getTarget())) {
                    if (permission == null) {
                        permission = perms[i];
                    } else {
                        if (perms[i].getTarget().length() > permission.getTarget().length()) {
                            permission = perms[i];
                        } else if (perms[i].getTarget().length() == permission.getTarget().length()) {
                            try {
                                IGroupMember person = AuthorizationService.instance().getGroupMember(principal);
                                String np = perms[i].getPrincipal();
                                IGroupMember ng = AuthorizationService.instance().getGroupMember(AuthorizationService.instance().newPrincipal(perms[i]));
                                IGroupMember og = AuthorizationService.instance().getGroupMember(AuthorizationService.instance().newPrincipal(permission));
                                if (np.equals(principal.toString()) || (ng.contains(person) && !og.contains(person))) {
                                    permission = perms[i];
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
            if ((permission == null) || (permission.getType().equals(permission.PERMISSION_TYPE_DENY))) {
                return false;
            }
            return true;
        }
    }

    public static synchronized CMSPermissionsPolicy instance() {
        if (_instance == null) {
            _instance = new CMSPermissionsPolicy();
        }
        return _instance;
    }
}
