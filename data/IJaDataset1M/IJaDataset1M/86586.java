package org.evertree.scroom.test.project.web;

import org.evertree.scroom.test.ControllerTestCase;
import org.springframework.web.servlet.ModelAndView;

public class SprintViewControllerTest extends ControllerTestCase {

    public SprintViewControllerTest() {
        super("sprintView");
    }

    public void testSprintNotFound() throws Exception {
        addRequestParameter("sprintId", "0");
        ModelAndView mv = handleRequest();
        assertNoErrorsAndView("forward:/do/sprint/backlog/view" + urlParameter("sprintId", "0"), mv);
    }

    public void testValidSprint() throws Exception {
        addRequestParameter("sprintId", "7");
        ModelAndView mv = handleRequest();
        assertNoErrorsAndView("forward:/do/sprint/backlog/view" + urlParameter("sprintId", "7"), mv);
    }

    public void testInvalidParameter() throws Exception {
        addRequestParameter("sprintId", "invalid");
        ModelAndView mv = handleRequest();
        assertGlobalErrorsAndView("forward:/do/projects/view", "error.parameter.value.invalid", mv);
    }

    public void testNoParameters() throws Exception {
        ModelAndView mv = handleRequest();
        assertGlobalErrorsAndView("forward:/do/projects/view", "error.parameter.mandatory", mv);
    }
}
