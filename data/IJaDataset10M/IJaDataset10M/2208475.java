package org.telscenter.sail.webapp.presentation.web.controllers.author.project;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.sail.webapp.dao.ObjectNotFoundException;
import net.sf.sail.webapp.domain.Curnit;
import net.sf.sail.webapp.domain.User;
import net.sf.sail.webapp.presentation.web.controllers.ControllerUtil;
import net.sf.sail.webapp.service.curnit.CurnitService;
import net.sf.sail.webapp.service.jnlp.JnlpService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.telscenter.sail.webapp.domain.impl.OtmlModuleImpl;
import org.telscenter.sail.webapp.domain.impl.ProjectParameters;
import org.telscenter.sail.webapp.domain.impl.RooloOtmlModuleImpl;
import org.telscenter.sail.webapp.domain.impl.UrlModuleImpl;
import org.telscenter.sail.webapp.domain.project.impl.ProjectType;
import org.telscenter.sail.webapp.service.project.ProjectService;

/**
 * @author Hiroki Terashima
 * @version $Id: CreateProjectController.java 2806 2010-04-08 00:01:27Z shadowtorn $
 */
public class CreateProjectController extends SimpleFormController {

    private static final String DEFAULT_PROJECT_NAME = "";

    private ProjectService projectService;

    private JnlpService jnlpService;

    private CurnitService curnitService;

    public CreateProjectController() {
        setSessionForm(true);
    }

    /**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        ProjectParameters projectParameters = new ProjectParameters();
        projectParameters.setProjectname(DEFAULT_PROJECT_NAME);
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
        ProjectParameters projectParameters = (ProjectParameters) command;
        try {
            Curnit curnit = curnitService.getById(projectParameters.getCurnitId());
            if (curnit instanceof RooloOtmlModuleImpl) {
                projectParameters.setProjectType(ProjectType.OTRUNK);
            } else if (curnit instanceof OtmlModuleImpl) {
                projectParameters.setProjectType(ProjectType.POTRUNK);
            } else if (curnit instanceof UrlModuleImpl) {
                projectParameters.setProjectType(ProjectType.LD);
            } else {
                projectParameters.setProjectType(ProjectType.POD);
            }
            User user = ControllerUtil.getSignedInUser();
            Set<User> owners = new HashSet<User>();
            owners.add(user);
            projectParameters.setOwners(owners);
            projectService.createProject(projectParameters);
        } catch (ObjectNotFoundException e) {
            ModelAndView modelAndView = new ModelAndView(getFormView());
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        return modelAndView;
    }

    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("jnlps", jnlpService.getJnlpList());
        model.put("curnits", curnitService.getCurnitList());
        return model;
    }

    /**
	 * @param projectService the projectService to set
	 */
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
	 * @param jnlpService the jnlpService to set
	 */
    public void setJnlpService(JnlpService jnlpService) {
        this.jnlpService = jnlpService;
    }

    /**
	 * @param curnitService the curnitService to set
	 */
    public void setCurnitService(CurnitService curnitService) {
        this.curnitService = curnitService;
    }
}
