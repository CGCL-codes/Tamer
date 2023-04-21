package evolaris.platform.um.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import evolaris.framework.sys.business.KeyValueContainer;
import evolaris.framework.sys.business.PermissionManager;
import evolaris.framework.sys.business.UserManagerBase;
import evolaris.framework.sys.business.exception.ConfigurationException;
import evolaris.framework.sys.business.exception.InputException;
import evolaris.framework.sys.datamodel.AccessControlledEntry;
import evolaris.framework.sys.datamodel.Permission;
import evolaris.framework.sys.web.action.LocalizedAction;
import evolaris.framework.um.business.UserManager;
import evolaris.framework.um.business.UserSetManager;
import evolaris.framework.um.datamodel.Role;
import evolaris.framework.um.datamodel.User;
import evolaris.framework.um.datamodel.UserSet;
import evolaris.platform.um.web.form.PermissionForm;

/**
 * @author robert.brandner
 *
 */
public class PermissionEnterOrEditAction extends LocalizedAction {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(PermissionEnterOrEditAction.class);

    protected ActionForward executeAccordingToMethod(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        if ("edit".equals(method)) {
            return edit(mapping, form, req, resp);
        }
        if ("modify".equals(method)) {
            return modify(mapping, form, req, resp);
        }
        return super.executeAccordingToMethod(mapping, form, req, resp, method);
    }

    /**
	 * show list of access permissions for specified object 
	 */
    protected ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        PermissionForm permissionForm = (PermissionForm) form;
        String idParam = req.getParameter("id");
        String nameParam = req.getParameter("name");
        String formActionPath = req.getParameter("formActionPath");
        req.setAttribute("formActionPath", formActionPath);
        PermissionManager permissionMgr = new PermissionManager(locale, session);
        List<String> permissionValues = new ArrayList<String>();
        AccessControlledEntry entry = permissionMgr.getAccessControlledEntry(Long.parseLong(idParam));
        if (entry == null) {
            throw new InputException(getResources(req).getMessage(locale, "um.AccessControlledEntryNotFound", idParam));
        }
        LOGGER.debug("preparing editing of permissions for AccessControlledEntry #`" + entry.getId() + "`");
        req.setAttribute("entry", entry);
        req.setAttribute("name", nameParam);
        LOGGER.debug("about to fetch list of permissions for AccessControlledClass `" + entry.getAccessControlledClass().getName() + "`...");
        List<Permission> permissions = permissionMgr.getPermissions(entry.getAccessControlledClass().getName());
        LOGGER.debug("successfully fetched list of permissions.");
        LOGGER.debug("about to prepare for display...");
        List<KeyValueContainer> displayablePermissions = new ArrayList<KeyValueContainer>();
        for (Permission p : permissions) {
            try {
                String s = this.getResources(req).getMessage(locale, "um.AccessPermission_" + p.getValue());
                displayablePermissions.add(new KeyValueContainer(p.getId(), s));
                LOGGER.debug("added displayable permission `" + s + "`");
            } catch (Exception e) {
                String msg = getResources(req).getMessage(locale, "um.missingAccessPermissionName", p.getValue());
                throw new ConfigurationException(msg);
            }
        }
        req.setAttribute("permissions", displayablePermissions);
        LOGGER.debug("about to fetch anonymous permissions...");
        Set<Long> anonymousPermissions = permissionMgr.getAnonymousPermissions(entry);
        for (Permission p : permissions) {
            if (anonymousPermissions.contains(p.getId())) {
                permissionValues.add("anonymous_" + p.getId());
            }
        }
        LOGGER.debug("successfully fetched anonymous permissions.");
        LOGGER.debug("about to fetch role permissions...");
        UserManager userMgr = new UserManager(locale, session);
        Role[] roles = null;
        if (req.isUserInRole(UserManagerBase.ADMINISTRATOR)) {
            roles = userMgr.getRoles();
        } else if (req.isUserInRole(UserManagerBase.GROUP_ADMINISTRATOR)) {
            roles = webUser.getRoles().toArray(new Role[0]);
        } else {
            LOGGER.error("insufficient rights - user must be GROUP_ADMINISTRATOR or ADMINISTRATOR to edit permissions.");
            throw new InputException(getResources(req).getMessage(locale, "um.insufficientRights"));
        }
        req.setAttribute("roles", roles);
        for (Role r : roles) {
            Set<Long> rolePermissions = permissionMgr.getRolePermissions(entry, r);
            for (Permission p : permissions) {
                if (rolePermissions.contains(p.getId())) {
                    permissionValues.add("role_" + r.getId() + "_" + p.getId());
                }
            }
        }
        LOGGER.debug("successfully fetched role permissions.");
        LOGGER.debug("about to fetch userset permissions...");
        UserSet[] userSets = entry.getGroup().getUserSets().toArray(new UserSet[0]);
        req.setAttribute("usersets", userSets);
        for (UserSet s : userSets) {
            Set<Long> userSetPermissions = permissionMgr.getUserSetPermissions(entry, s);
            for (Permission p : permissions) {
                if (userSetPermissions.contains(p.getId())) {
                    permissionValues.add("userset_" + s.getId() + "_" + p.getId());
                }
            }
        }
        LOGGER.debug("sucessfully fetched userset permissions.");
        LOGGER.debug("about to fetch user permissions.");
        User[] users = userMgr.getUsers(entry.getGroup(), false);
        if (users != null && users.length > 100) {
            req.setAttribute("tooManyUsers", getLocalizedMessage("application", "um.tooManyUsersForPermissions", 100));
        } else {
            req.setAttribute("users", users);
            for (User u : users) {
                Set<Long> userPermissions = permissionMgr.getUserPermissions(entry, u);
                for (Permission p : permissions) {
                    if (userPermissions.contains(p.getId())) {
                        permissionValues.add("user_" + u.getId() + "_" + p.getId());
                    }
                }
            }
        }
        permissionForm.setPermission(permissionValues.toArray(new String[0]));
        permissionForm.setId(Long.parseLong(idParam));
        permissionForm.setName(nameParam);
        return mapping.findForward("edit");
    }

    public ActionForward modify(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        if (!(req.isUserInRole(UserManagerBase.ADMINISTRATOR) || req.isUserInRole(UserManagerBase.GROUP_ADMINISTRATOR))) {
            throw new InputException(getResources(req).getMessage(locale, "um.insufficientRights"));
        }
        PermissionForm permissionForm = (PermissionForm) form;
        PermissionManager permissionMgr = new PermissionManager(locale, session);
        AccessControlledEntry entry = permissionMgr.getAccessControlledEntry(permissionForm.getId());
        if (entry == null) {
            throw new InputException(getResources(req).getMessage(locale, "um.AccessControlledEntryNotFound", permissionForm.getId()));
        }
        Set<Long> anonymousPermissions = new HashSet<Long>();
        Map<Long, Set<Long>> rolePermissions = new HashMap<Long, Set<Long>>();
        Map<Long, Set<Long>> userSetPermissions = new HashMap<Long, Set<Long>>();
        Map<Long, Set<Long>> userPermissions = new HashMap<Long, Set<Long>>();
        for (String p : permissionForm.getPermission()) {
            String[] parts = p.split("_");
            if ("anonymous".equals(parts[0])) {
                anonymousPermissions.add(Long.parseLong(parts[1]));
            } else if ("role".equals(parts[0])) {
                Long roleId = Long.parseLong(parts[1]);
                if (!rolePermissions.containsKey(roleId)) {
                    rolePermissions.put(roleId, new HashSet<Long>());
                }
                Long permission = Long.parseLong(parts[2]);
                if (permission > 0) {
                    rolePermissions.get(roleId).add(permission);
                }
            } else if ("userset".equals(parts[0])) {
                Long userSetId = Long.parseLong(parts[1]);
                if (!userSetPermissions.containsKey(userSetId)) {
                    userSetPermissions.put(userSetId, new HashSet<Long>());
                }
                Long permission = Long.parseLong(parts[2]);
                if (permission > 0) {
                    userSetPermissions.get(userSetId).add(permission);
                }
            } else if ("user".equals(parts[0])) {
                Long userId = Long.parseLong(parts[1]);
                if (!userPermissions.containsKey(userId)) {
                    userPermissions.put(userId, new HashSet<Long>());
                }
                Long permission = Long.parseLong(parts[2]);
                if (permission > 0) {
                    userPermissions.get(userId).add(permission);
                }
            }
        }
        UserManager userMgr = new UserManager(locale, session);
        UserSetManager userSetMgr = new UserSetManager(locale, session);
        permissionMgr.setAnonymousPermissions(entry, anonymousPermissions.toArray(new Long[0]));
        for (Iterator<Map.Entry<Long, Set<Long>>> iter = rolePermissions.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<Long, Set<Long>> element = iter.next();
            Role role = userMgr.getRole(element.getKey());
            permissionMgr.setRolePermissions(entry, role, element.getValue().toArray(new Long[0]));
        }
        for (Iterator<Map.Entry<Long, Set<Long>>> iter = userSetPermissions.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<Long, Set<Long>> element = iter.next();
            UserSet userSet = userSetMgr.getUserSet(element.getKey());
            permissionMgr.setUserSetPermissions(entry, userSet, element.getValue().toArray(new Long[0]));
        }
        for (Iterator<Map.Entry<Long, Set<Long>>> iter = userPermissions.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<Long, Set<Long>> element = iter.next();
            User user = userMgr.getUserDetails(element.getKey());
            permissionMgr.setUserPermissions(entry, user, element.getValue().toArray(new Long[0]));
        }
        return mapping.findForward("modified");
    }

    public ActionForward back(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        return mapping.findForward("cancelled");
    }

    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        return back(mapping, form, req, resp);
    }

    @Override
    protected ActionForward defaultMethod(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        return edit(mapping, form, req, resp);
    }

    @Override
    protected Map getKeyMethodMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("um.modify", "modify");
        map.put("um.back", "back");
        return map;
    }
}
