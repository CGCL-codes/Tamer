package org.itracker.web.actions.admin.user;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.itracker.model.NameValuePair;
import org.itracker.model.Permission;
import org.itracker.model.Project;
import org.itracker.model.User;
import org.itracker.services.ProjectService;
import org.itracker.services.UserService;
import org.itracker.services.util.UserUtilities;
import org.itracker.web.actions.base.ItrackerBaseAction;
import org.itracker.web.forms.UserForm;
import org.itracker.web.util.Constants;

public class EditUserFormAction extends ItrackerBaseAction {

    private static final Logger log = Logger.getLogger(EditUserFormAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionMessages errors = new ActionMessages();
        if (!hasPermission(UserUtilities.PERMISSION_USER_ADMIN, request, response)) {
            return mapping.findForward("unauthorized");
        }
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute(Constants.USER_KEY);
        String action = (String) request.getParameter("action");
        String pageTitleKey = "";
        String pageTitleArg = "";
        boolean isUpdate = false;
        if (action != null && action.equals("update")) {
            isUpdate = true;
            pageTitleKey = "itracker.web.admin.edituser.title.update";
            pageTitleArg = user.getLogin();
        } else {
            pageTitleKey = "itracker.web.admin.edituser.title.create";
        }
        request.setAttribute("isUpdate", isUpdate);
        request.setAttribute("pageTitleKey", pageTitleKey);
        request.setAttribute("pageTitleArg", pageTitleArg);
        try {
            UserService userService = getITrackerServices().getUserService();
            ProjectService projectService = getITrackerServices().getProjectService();
            List<Project> projects = null;
            User editUser = null;
            HashMap<Integer, HashMap<String, Permission>> userPermissions = new HashMap<Integer, HashMap<String, Permission>>();
            List<NameValuePair> permissionNames = UserUtilities.getPermissionNames(getLocale(request));
            UserForm userForm = (UserForm) form;
            if (userForm == null) {
                userForm = new UserForm();
            }
            if ("create".equals(action)) {
                if (!userService.allowProfileCreation(null, null, UserUtilities.AUTH_TYPE_UNKNOWN, UserUtilities.REQ_SOURCE_WEB)) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.noprofilecreates"));
                    saveErrors(request, errors);
                    return mapping.findForward("error");
                }
                editUser = new User();
                editUser.setId(-1);
                editUser.setStatus(UserUtilities.STATUS_ACTIVE);
                userForm.setAction("create");
                userForm.setId(editUser.getId());
            } else if ("update".equals(action)) {
                Integer userId = userForm.getId();
                if (userId == null) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.invaliduser"));
                } else {
                    editUser = userService.getUser(userId);
                    if (editUser == null) {
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.invaliduser"));
                    } else {
                        userForm.setAction("update");
                        userForm.setId(editUser.getId());
                        userForm.setLogin(editUser.getLogin());
                        userForm.setFirstName(editUser.getFirstName());
                        userForm.setLastName(editUser.getLastName());
                        userForm.setEmail(editUser.getEmail());
                        userForm.setSuperUser(editUser.isSuperUser());
                        List<Permission> permissionList = userService.getPermissionsByUserId(editUser.getId());
                        HashMap<String, String> formPermissions = new HashMap<String, String>();
                        boolean allowProfileUpdate = userService.allowProfileUpdates(editUser, null, UserUtilities.AUTH_TYPE_UNKNOWN, UserUtilities.REQ_SOURCE_WEB);
                        request.setAttribute("allowProfileUpdate", allowProfileUpdate);
                        boolean allowPasswordUpdate = userService.allowPasswordUpdates(editUser, null, UserUtilities.AUTH_TYPE_UNKNOWN, UserUtilities.REQ_SOURCE_WEB);
                        request.setAttribute("allowPasswordUpdate", allowPasswordUpdate);
                        boolean allowPermissionUpdate = userService.allowPermissionUpdates(editUser, null, UserUtilities.AUTH_TYPE_UNKNOWN, UserUtilities.REQ_SOURCE_WEB);
                        request.setAttribute("allowPermissionUpdate", allowPermissionUpdate);
                        if (editUser.getId() > 0) {
                            request.setAttribute("isUpdate", true);
                        }
                        for (int i = 0; i < permissionList.size(); i++) {
                            log.debug("Processing permission type: " + permissionList.get(i).getPermissionType());
                            if (permissionList.size() > 0 && permissionList.get(0).getPermissionType() == -1) {
                                if (permissionList.size() > 1 && i != 0) {
                                    Integer projectId = permissionList.get(i).getProject().getId();
                                    if (userPermissions.get(projectId) == null) {
                                        HashMap<String, Permission> projectPermissions = new HashMap<String, Permission>();
                                        userPermissions.put(permissionList.get(i).getProject().getId(), projectPermissions);
                                    }
                                    formPermissions.put("Perm" + permissionList.get(i).getPermissionType() + "Proj" + permissionList.get(i).getProject().getId(), "on");
                                    Integer permissionType = permissionList.get(i).getPermissionType();
                                    Permission thisPermission = permissionList.get(i);
                                    HashMap<String, Permission> permissionHashMap = ((HashMap<String, Permission>) userPermissions.get(projectId));
                                    permissionHashMap.put(String.valueOf(permissionType), thisPermission);
                                }
                            } else {
                                Integer projectId = permissionList.get(i).getProject().getId();
                                if (userPermissions.get(projectId) == null) {
                                    HashMap<String, Permission> projectPermissions = new HashMap<String, Permission>();
                                    userPermissions.put(permissionList.get(i).getProject().getId(), projectPermissions);
                                }
                                formPermissions.put("Perm" + permissionList.get(i).getPermissionType() + "Proj" + permissionList.get(i).getProject().getId(), "on");
                                Integer permissionType = permissionList.get(i).getPermissionType();
                                Permission thisPermission = permissionList.get(i);
                                HashMap<String, Permission> permissionHashMap = ((HashMap<String, Permission>) userPermissions.get(projectId));
                                permissionHashMap.put(String.valueOf(permissionType), thisPermission);
                            }
                        }
                        userForm.setPermissions(formPermissions);
                    }
                }
            } else {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.invalidaction"));
            }
            if (editUser == null) {
                return mapping.findForward("unauthorized");
            }
            if (errors.isEmpty()) {
                String userStatus = UserUtilities.getStatusName(editUser.getStatus());
                request.setAttribute("userStatus", userStatus);
                projects = projectService.getAllAvailableProjects();
                Collections.sort(projects, Project.PROJECT_COMPARATOR);
                request.setAttribute(Constants.PROJECTS_KEY, projects);
                request.setAttribute("userForm", userForm);
                session.setAttribute(Constants.EDIT_USER_KEY, editUser);
                session.setAttribute(Constants.EDIT_USER_PERMS_KEY, userPermissions);
                request.setAttribute("permissionNames", permissionNames);
                request.setAttribute("permissionRowColIdxes", new Integer[] { 0, 1 });
                saveToken(request);
                return mapping.findForward("edituserform");
            }
        } catch (Exception e) {
            log.error("Exception while creating edit user form.", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.system"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return mapping.findForward("error");
    }
}
