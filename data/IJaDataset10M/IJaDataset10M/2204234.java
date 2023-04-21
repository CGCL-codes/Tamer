package org.itracker.web.actions.admin.project;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.itracker.model.PermissionType;
import org.itracker.model.Project;
import org.itracker.model.User;
import org.itracker.services.ProjectService;
import org.itracker.services.UserService;
import org.itracker.services.util.UserUtilities;
import org.itracker.web.actions.base.ItrackerBaseAction;
import org.itracker.web.util.Constants;
import org.itracker.web.util.LoginUtilities;

public class EditProjectAction extends ItrackerBaseAction {

    private static final Logger log = Logger.getLogger(EditProjectAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionMessages errors = new ActionMessages();
        if (!isTokenValid(request)) {
            log.debug("Invalid request token while editing project.");
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.transaction"));
            saveErrors(request, errors);
            saveToken(request);
            return mapping.getInputForward();
        }
        resetToken(request);
        Project project = null;
        try {
            ProjectService projectService = getITrackerServices().getProjectService();
            UserService userService = getITrackerServices().getUserService();
            HttpSession session = request.getSession(true);
            User user = LoginUtilities.getCurrentUser(request);
            String action = (String) request.getParameter("action");
            if ("update".equals(action)) {
                Map<Integer, Set<PermissionType>> userPermissions = getUserPermissions(session);
                project = projectService.getProject((Integer) PropertyUtils.getSimpleProperty(form, "id"));
                if (!UserUtilities.hasPermission(userPermissions, project.getId(), UserUtilities.PERMISSION_PRODUCT_ADMIN)) {
                    return mapping.findForward("unauthorized");
                }
                AdminProjectUtilities.setFormProperties(project, projectService, form, errors);
                if (!errors.isEmpty()) {
                    saveErrors(request, errors);
                    return mapping.getInputForward();
                } else {
                    Integer[] ownersArray = (Integer[]) PropertyUtils.getSimpleProperty(form, "owners");
                    Set<Integer> ownerIds = null == ownersArray ? new HashSet<Integer>() : new HashSet<Integer>(Arrays.asList(ownersArray));
                    AdminProjectUtilities.updateProjectOwners(project, ownerIds, projectService, userService);
                    if (log.isDebugEnabled()) {
                        log.debug("execute: updating existing project: " + project);
                    }
                    project = projectService.updateProject(project, user.getId());
                }
            } else if ("create".equals(action)) {
                if (!user.isSuperUser()) {
                    return mapping.findForward("unauthorized");
                }
                project = new Project();
                AdminProjectUtilities.setFormProperties(project, projectService, form, errors);
                if (!errors.isEmpty()) {
                    saveErrors(request, errors);
                    return mapping.getInputForward();
                }
                project = projectService.createProject(project, user.getId());
                if (log.isDebugEnabled()) {
                    log.debug("execute: created new project: " + project);
                }
                Integer[] users = (Integer[]) PropertyUtils.getSimpleProperty(form, "users");
                if (users != null) {
                    Set<Integer> userIds = new HashSet<Integer>(Arrays.asList(users));
                    Integer[] permissionArray = (Integer[]) PropertyUtils.getSimpleProperty(form, "permissions");
                    Set<Integer> permissions = null == permissionArray ? new HashSet<Integer>(0) : new HashSet<Integer>(Arrays.asList(permissionArray));
                    Integer[] ownersArray = (Integer[]) PropertyUtils.getSimpleProperty(form, "owners");
                    Set<Integer> ownerIds = null == ownersArray ? new HashSet<Integer>() : new HashSet<Integer>(Arrays.asList(ownersArray));
                    if (permissions.contains(UserUtilities.PERMISSION_PRODUCT_ADMIN)) {
                        ownerIds.addAll(userIds);
                    } else {
                        AdminProjectUtilities.handleInitialProjectMembers(project, userIds, permissions, projectService, userService);
                    }
                    AdminProjectUtilities.updateProjectOwners(project, ownerIds, projectService, userService);
                }
                if (log.isDebugEnabled()) {
                    log.debug("execute: updating new project: " + project);
                }
                session.removeAttribute(Constants.PROJECT_KEY);
            }
        } catch (RuntimeException e) {
            log.error("execute: Exception processing form data", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.system"));
        } catch (IllegalAccessException e) {
            log.error("execute: Exception processing form data", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.system"));
        } catch (InvocationTargetException e) {
            log.error("execute: Exception processing form data", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.system"));
        } catch (NoSuchMethodException e) {
            log.error("execute: Exception processing form data", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.system"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            if (log.isDebugEnabled()) {
                log.debug("execute: got errors in action-messages: " + errors);
            }
            return mapping.findForward("error");
        }
        return mapping.findForward("listprojectsadmin");
    }
}
