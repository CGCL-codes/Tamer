package cz.muni.fi.pclis.web.controllers.student.learningExpectations;

import cz.muni.fi.pclis.web.annotations.Role;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import cz.muni.fi.pclis.domain.learningExpectations.ExpectationPoint;
import cz.muni.fi.pclis.domain.*;
import cz.muni.fi.pclis.service.controllers.ControllerHelper;
import cz.muni.fi.pclis.service.learningExpectations.ExpectationPointService;
import cz.muni.fi.pclis.service.learningExpectations.LearningExpectationsBehaviourService;
import cz.muni.fi.pclis.service.CourseService;
import cz.muni.fi.pclis.service.TermService;
import cz.muni.fi.pclis.web.annotations.AllowedRole;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Controller handling student view, modification and evaluation of learning expectations
 * User: Ľuboš Pecho
 * Date: 6.9.2009
 * Time: 13:18:06
 */
@Component("learningExpectationsStudentsController")
@AllowedRole(Role.STUDENT)
public class LearningExpectationsStudentController extends SimpleFormController {

    private ControllerHelper controllerHelper;

    private ExpectationPointService expectationPointService;

    private LearningExpectationsBehaviourService learningExpectationsBehaviourService;

    private CourseService courseService;

    private TermService termService;

    public LearningExpectationsStudentController() {
        setCommandClass(ExpectationPoint.class);
        setCommandName("dto");
        setValidator(new ExpectationPointValidator());
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap();
        User user = (User) request.getSession().getAttribute("user");
        Term term = termService.getById(Long.parseLong((String) request.getSession().getAttribute("termStudentCookie")));
        Course course = courseService.getById(Long.parseLong((String) request.getSession().getAttribute("courseStudentCookie")));
        List<ExpectationPoint> termAndCourseAndUser = expectationPointService.getByTermAndCourseAndUser(term, course, user);
        map.put("config", learningExpectationsBehaviourService.getByCourseAndTerm(course, term));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.clear(Calendar.HOUR);
        cal.clear(Calendar.HOUR_OF_DAY);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.clear(Calendar.AM_PM);
        map.put("now", cal.getTime());
        map.put("points", termAndCourseAndUser);
        map.put("editId", request.getParameter("editId"));
        return map;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String editId = request.getParameter("editId");
        ExpectationPoint point;
        if (editId == null) {
            point = (ExpectationPoint) super.formBackingObject(request);
            point.setUser((User) request.getSession().getAttribute("user"));
            point.setCourse(courseService.getById(Long.parseLong((String) request.getSession().getAttribute("courseStudentCookie"))));
            point.setTerm(termService.getById(Long.parseLong((String) request.getSession().getAttribute("termStudentCookie"))));
        } else {
            point = expectationPointService.getById(Long.parseLong(editId));
        }
        return point;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        ExpectationPoint point = (ExpectationPoint) o;
        if (point.getId() == null) {
            expectationPointService.create(point);
        } else {
            expectationPointService.update(point);
        }
        return new ModelAndView(getSuccessView());
    }

    private class ExpectationPointValidator implements Validator {

        public boolean supports(Class aClass) {
            return ExpectationPoint.class.equals(aClass);
        }

        public void validate(Object o, Errors errors) {
            ValidationUtils.rejectIfEmpty(errors, "title", "title.required", "The title is required");
        }
    }

    public ControllerHelper getControllerHelper() {
        return controllerHelper;
    }

    @Resource
    public void setControllerHelper(ControllerHelper controllerHelper) {
        this.controllerHelper = controllerHelper;
    }

    public ExpectationPointService getExpectationPointService() {
        return expectationPointService;
    }

    @Resource
    public void setExpectationPointService(ExpectationPointService expectationPointService) {
        this.expectationPointService = expectationPointService;
    }

    public CourseService getCourseService() {
        return courseService;
    }

    @Resource
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public TermService getTermService() {
        return termService;
    }

    @Resource
    public void setTermService(TermService termService) {
        this.termService = termService;
    }

    public LearningExpectationsBehaviourService getLearningExpectationsBehaviourService() {
        return learningExpectationsBehaviourService;
    }

    @Resource
    public void setLearningExpectationsBehaviourService(LearningExpectationsBehaviourService learningExpectationsBehaviourService) {
        this.learningExpectationsBehaviourService = learningExpectationsBehaviourService;
    }
}
