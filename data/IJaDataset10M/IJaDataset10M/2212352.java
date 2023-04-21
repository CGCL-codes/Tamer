package org.telscenter.sail.webapp.presentation.web.controllers.author.project;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.sail.webapp.dao.ObjectNotFoundException;
import net.sf.sail.webapp.domain.Curnit;
import net.sf.sail.webapp.domain.User;
import net.sf.sail.webapp.presentation.web.controllers.ControllerUtil;
import net.sf.sail.webapp.service.curnit.CurnitService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.telscenter.sail.webapp.domain.impl.CreateUrlModuleParameters;
import org.telscenter.sail.webapp.domain.impl.ProjectParameters;
import org.telscenter.sail.webapp.domain.impl.PublishProjectParameters;
import org.telscenter.sail.webapp.domain.project.impl.ProjectType;
import org.telscenter.sail.webapp.service.project.ProjectService;

/**
 * Publishes a LDProject, thereby adding the project to the Project Library.
 * The user specifies:
 * 1) where the project.xml file resides in relation to the ContextRoot.
 *    ex: /vlewrapper/curriculum/15/HelloWorld.project.xml.
 * 2) project name/curnit name 
 * 
 * The Curnit is added and a project is created for that project.
 * 
 * @author hirokiterashima
 * @version $Id:$
 */
public class PublishProjectController extends SimpleFormController {

    private ProjectService projectService;

    private CurnitService curnitService;

    public PublishProjectController() {
        setSessionForm(true);
    }

    /**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        PublishProjectParameters projectParameters = new PublishProjectParameters();
        String projectname = request.getParameter("projectname");
        String projectpath = request.getParameter("projectpath");
        if (projectname != null) {
            projectParameters.setProjectname(projectname);
        }
        if (projectpath != null) {
            projectParameters.setProjectpath(projectpath);
        }
        return projectParameters;
    }

    /**
     * On submission of the Create Project form, a <code>Project</code>
     * is created in the datastore
     * 
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) {
        PublishProjectParameters publishProjectParameters = (PublishProjectParameters) command;
        CreateUrlModuleParameters curnitParameters = new CreateUrlModuleParameters();
        curnitParameters.setName(publishProjectParameters.getProjectname());
        curnitParameters.setUrl(publishProjectParameters.getProjectpath());
        Curnit curnit = this.curnitService.createCurnit(curnitParameters);
        ProjectParameters createProjectParameters = new ProjectParameters();
        createProjectParameters.setCurnitId(curnit.getId());
        createProjectParameters.setProjectname(publishProjectParameters.getProjectname());
        createProjectParameters.setProjectType(ProjectType.LD);
        User user = ControllerUtil.getSignedInUser();
        Set<User> owners = new HashSet<User>();
        owners.add(user);
        createProjectParameters.setOwners(owners);
        try {
            projectService.createProject(createProjectParameters);
        } catch (ObjectNotFoundException e) {
            ModelAndView modelAndView = new ModelAndView(getFormView());
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        return modelAndView;
    }

    /**
	 * @return the projectService
	 */
    public ProjectService getProjectService() {
        return projectService;
    }

    /**
	 * @param projectService the projectService to set
	 */
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
	 * @return the curnitService
	 */
    public CurnitService getCurnitService() {
        return curnitService;
    }

    /**
	 * @param curnitService the curnitService to set
	 */
    public void setCurnitService(CurnitService curnitService) {
        this.curnitService = curnitService;
    }
}
