package cz.muni.fi.pclis.web.controllers.teacher.feedback;

import cz.muni.fi.pclis.web.annotations.Role;
import cz.muni.fi.pclis.web.annotations.AllowedRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Basic controller for handling teacher part of feedbacks
 * User: Ľuboš Pecho
 * Date: 18.9.2009
 * Time: 15:33:33
 */
@Component("feedbackTeacherController")
@AllowedRole(Role.TEACHER)
public class FeedbackTeacherController extends AbstractController {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        return new ModelAndView("feedbackTeacher");
    }
}
