package org.telscenter.sail.webapp.presentation.web.controllers.teacher.project;

import static org.easymock.EasyMock.*;
import net.sf.sail.webapp.dao.ObjectNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.TestClassRunner;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.AbstractModelAndViewTests;
import org.springframework.web.servlet.ModelAndView;
import org.telscenter.sail.webapp.domain.project.Project;
import org.telscenter.sail.webapp.domain.project.impl.ProjectImpl;
import org.telscenter.sail.webapp.service.project.ProjectService;

/**
 * @author Hiroki Terashima
 * @version $Id: ProjectInfoControllerTest.java 1979 2008-07-03 00:15:50Z hiroki $
 */
@RunWith(TestClassRunner.class)
public class ProjectInfoControllerTest extends AbstractModelAndViewTests {

    private static final Long DEFAULT_PROJECT_ID = new Long(10);

    private static final Long NON_EXISTING_PROJECT_ID = new Long(999999);

    private ProjectInfoController projectInfoController;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private ProjectService projectService;

    private Project project;

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        this.request.addParameter("projectId", DEFAULT_PROJECT_ID.toString());
        this.project = new ProjectImpl();
        this.projectService = createMock(ProjectService.class);
        this.projectInfoController = new ProjectInfoController();
        this.projectInfoController.setProjectService(this.projectService);
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        this.request = null;
        this.response = null;
        this.projectService = null;
    }

    @Test
    public void testHandleRequestInternal_ObjectNotFoundException() throws Exception {
        request.setParameter("projectId", NON_EXISTING_PROJECT_ID.toString());
        expect(projectService.getById(NON_EXISTING_PROJECT_ID.toString())).andThrow(new ObjectNotFoundException(NON_EXISTING_PROJECT_ID, Project.class));
        replay(projectService);
        try {
            projectInfoController.handleRequestInternal(request, response);
            fail("ObjectNotFoundException was expected");
        } catch (ObjectNotFoundException e) {
        }
        verify(projectService);
    }

    @Test
    public void testHandleRequestInternal_success() throws Exception {
        expect(projectService.getById(DEFAULT_PROJECT_ID.toString())).andReturn(project);
        replay(projectService);
        ModelAndView modelAndView = null;
        try {
            modelAndView = projectInfoController.handleRequestInternal(request, response);
        } catch (ObjectNotFoundException e) {
            fail("ObjectNotFoundException was NOT expected but was thrown");
        }
        verify(projectService);
        assertModelAttributeValue(modelAndView, ProjectInfoController.PROJECT_PARAM_NAME, this.project);
    }
}
