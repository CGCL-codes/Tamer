package org.openi.web.controller.admin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.openi.application.Application;
import org.openi.project.Module;
import org.openi.project.Project;
import org.openi.project.ProjectContext;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * @author Uddhab Pant
 *
 * Controller for handling <strong>edit project config</strong> request.
 * This class is responsible for
 * <ul>
 * <li> display edit project form
 * <li> form validation
 * <li> form submit
 * </ul>
 *
 */
public class ProjectFormController extends SimpleFormController {

    private static Logger logger = Logger.getLogger(ProjectFormController.class);

    /**
     * Retrieve a backing object for the current form from the given request
     * @param request HttpServletRequest
     * @return Object as application config object
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        try {
            ProjectContext projectContext = (ProjectContext) request.getSession().getAttribute("projectContext");
            Project project = projectContext.getProject();
            return project;
        } catch (Exception e) {
            logger.error("Exception:", e);
            throw e;
        }
    }

    /**
     * Callback for custom post-processing in terms of binding and validation.
     * Called on each submit, after standard binding and validation, but before error evaluation.
     * @param request HttpServletRequest
     * @param command Object
     * @param errors BindException
     * @throws Exception
     */
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        Project project = (Project) command;
        String[] folders = request.getParameterValues("modules.folderName");
        String[] allowedUsers = request.getParameterValues("modules.allowedUsers");
        List modules = new LinkedList();
        List pathList = new LinkedList();
        for (int i = 0; i < folders.length; i++) {
            if ((folders[i] != null) && !folders[i].equals("")) {
                if (!pathList.contains(folders[i])) {
                    pathList.add(folders[i]);
                    Module module = new Module();
                    module.setFolderName(folders[i]);
                    module.setAllowedUsers(allowedUsers[i]);
                    modules.add(module);
                }
            }
        }
        Map rolemap = project.getRoleMembers();
        if (rolemap == null) {
            rolemap = new HashMap();
            project.setRoleMembers(rolemap);
        }
        for (Iterator iter = Application.getInstance().getRoleList().iterator(); iter.hasNext(); ) {
            String role = (String) iter.next();
            String members = request.getParameter("role." + role);
            if (members != null) rolemap.put(role, members); else rolemap.remove(role);
        }
        project.setModules(modules);
    }

    /**
     * Submit callback with all parameters. Called in case of submit
     * without errors reported by the registered validator, or on every submit if no validator.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param command Object
     * @param errors BindException
     * @return ModelAndView
     * @throws Exception
     */
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        try {
            Project project = (Project) command;
            if (request.getParameter("writeImageMap") == null) {
                project.setWriteImageMap(false);
            } else {
                project.setWriteImageMap(true);
            }
            if (request.getParameter("multipleFilterSelection") == null) {
                project.setMultipleFilterSelection(false);
            } else {
                project.setMultipleFilterSelection(true);
            }
            if (request.getParameter("customText") == null) {
                project.setCustomText(false);
            } else {
                project.setCustomText(true);
            }
            ProjectContext projectContext = (ProjectContext) request.getSession().getAttribute("projectContext");
            projectContext.setProject(project);
            projectContext.saveProject();
            return super.onSubmit(request, response, command, errors);
        } catch (Exception e) {
            logger.error("Exception:", e);
            throw e;
        }
    }
}
