package org.programmerplanet.intracollab.web.admin.milestone;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.programmerplanet.intracollab.manager.ProjectManager;
import org.programmerplanet.intracollab.model.Milestone;
import org.programmerplanet.intracollab.model.Project;
import org.programmerplanet.intracollab.web.ServletRequestUtils;
import org.programmerplanet.intracollab.web.spring.SimpleMultiActionFormController;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * Comment goes here...
 * 
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2009 Joseph Fifield
 */
public class MilestoneEditController extends SimpleMultiActionFormController {

    private ProjectManager projectManager;

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    /**
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
	 */
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }

    public ModelAndView save(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Milestone milestone = (Milestone) command;
        projectManager.saveMilestone(milestone);
        return new ModelAndView(this.getSuccessView(), "project_id", milestone.getProject().getId());
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Milestone milestone = (Milestone) command;
        projectManager.deleteMilestone(milestone);
        return new ModelAndView(this.getSuccessView(), "project_id", milestone.getProject().getId());
    }

    public ModelAndView cancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Milestone milestone = (Milestone) command;
        return new ModelAndView(this.getSuccessView(), "project_id", milestone.getProject().getId());
    }

    /**
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#suppressBinding(javax.servlet.http.HttpServletRequest)
	 */
    protected boolean suppressBinding(HttpServletRequest request) {
        return !WebUtils.hasSubmitParameter(request, "__save");
    }

    /**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long id = ServletRequestUtils.getLongParameter(request, "id");
        if (id != null) {
            return projectManager.getMilestone(id);
        } else {
            Milestone milestone = new Milestone();
            Long projectId = ServletRequestUtils.getLongParameter(request, "project_id");
            Project project = projectManager.getProject(projectId);
            milestone.setProject(project);
            return milestone;
        }
    }
}
