package org.telscenter.sail.webapp.presentation.web.controllers.teacher.project.bookmarked;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.telscenter.sail.webapp.domain.project.Project;
import org.telscenter.sail.webapp.service.project.ProjectService;
import net.sf.sail.webapp.domain.User;
import net.sf.sail.webapp.presentation.web.controllers.ControllerUtil;

/**
 * @author patrick lawler
 * @version $Id:$
 */
public class BookmarkedProjectController extends AbstractController {

    private ProjectService projectService;

    protected static final String BOOKMARKER_PROJECT_LIST = "projectList";

    protected static final String USERID = "userId";

    /**
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = ControllerUtil.getSignedInUser();
        List<Project> projectList = this.projectService.getBookmarkerProjectList(user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(BOOKMARKER_PROJECT_LIST, projectList);
        modelAndView.addObject(USERID, user.getId());
        return modelAndView;
    }

    /**
	 * @param projectService the projectService to set
	 */
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }
}
