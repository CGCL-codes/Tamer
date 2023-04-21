package com.germinus.merlin.controller.assignments;

import java.util.Set;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import com.germinus.liferay.util.ILiferayUtil;
import com.germinus.liferay.util.IUserUtil;
import com.germinus.merlin.manager.AssignmentManager;
import com.germinus.merlin.manager.AssignmentResultManager;
import com.germinus.merlin.manager.CourseManager;
import com.germinus.merlin.model.Course;
import com.germinus.merlin.model.assignment.Assignment;
import com.germinus.merlin.model.assignment.AssignmentResult;
import com.germinus.merlin.model.assignment.AssignmentUploadASingleFile;
import com.germinus.merlin.util.IMerlinUtil;

/**
 * This controller show the list of assignments in a search container
 * @author Rui Quintas
 * @version 1.0
 * @since 1.0
 * @author Jesús Rodríguez Martínez Gérminus XXI
 * @version 2.0
 * @since 2.0
 */
public class ResultGradeController extends AbstractController implements InitializingBean {

    private ILiferayUtil liferayUtil;

    private IMerlinUtil merlinUtil;

    private IUserUtil userUtil;

    private static final Log log = LogFactory.getLog(ResultGradeController.class);

    private AssignmentManager assignmentManager;

    private CourseManager courseManager;

    private AssignmentResultManager assignmentResultManager;

    public Long resultId;

    public void afterPropertiesSet() throws Exception {
    }

    /**
	 * @return the assignmentManager
	 */
    public AssignmentManager getAssignmentManager() {
        return assignmentManager;
    }

    /**
	 * @return the courseManager
	 */
    public CourseManager getCourseManager() {
        return courseManager;
    }

    /**
	 * Get the parameters from the view and update the AssignmentResult.
	 * Make the Correction of the Assignment.
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    public void handleActionRequestInternal(ActionRequest request, ActionResponse response) throws Exception {
    }

    /**
	 * Process the action request. There is nothing to return.
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    public void handleActionRequestVoid(ActionRequest request, ActionResponse response) throws Exception {
    }

    /**
	 * Process the list of assignmentsresults to be view in the search container.
	 * Process the render request and return a ModelAndView object which the
	 * DispatcherPortlet will render.
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return ModelAndView
	 */
    @SuppressWarnings("unchecked")
    public ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
        log.debug("\n\nResults Grade Controller");
        Assignment assignment;
        AssignmentResult result;
        long courseId = liferayUtil.getComunityGroupId(request, response);
        Course course = courseManager.getCourse(courseId);
        try {
            resultId = new Long(request.getParameter("result"));
        } catch (NumberFormatException ex) {
            resultId = null;
        }
        Set<Assignment> assignmentsSet = course.getAssignments();
        Long assignmentId = new Long(request.getParameter("assignmentId"));
        log.debug("Assignment Id pasado por parámetro: " + assignmentId);
        assignment = assignmentManager.searchAssignment(assignmentsSet, assignmentId);
        Set<AssignmentResult> listsetresults = assignment.getResults();
        result = assignmentResultManager.searchAssignmentResult(listsetresults, resultId);
        request.setAttribute("assignment", assignment);
        String studentName = userUtil.getUserName(result.getStudents().getId().getUserid());
        String assignmentType = assignment.getType();
        request.setAttribute("studentName", studentName);
        if (assignmentType.compareTo("Online Activity") == 0) {
            return new ModelAndView("assignments/gradeAssignment/gradeAssignmentOnline", "result", result);
        } else if (assignmentType.compareTo("Upload a Single File") == 0) {
            if (((AssignmentUploadASingleFile) assignment).getAllowNotes() != null && ((AssignmentUploadASingleFile) assignment).getAllowNotes()) {
                return new ModelAndView("assignments/gradeAssignment/gradeAssignmentUploadSingleFileWithNotes", "result", result);
            } else return new ModelAndView("assignments/gradeAssignment/gradeAssignmentUploadSingleFile", "result", result);
        } else return new ModelAndView("errors/error", "", null);
    }

    /**
	 * @param assignmentManager the assignmentManager to set
	 */
    public void setAssignmentManager(AssignmentManager assignmentsManagement) {
        this.assignmentManager = assignmentsManagement;
    }

    /**
	 * @param courseManager the courseManager to set
	 */
    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    /**
	 * @return the merlinUtil
	 */
    public IMerlinUtil getMerlinUtil() {
        return merlinUtil;
    }

    /**
	 * @param merlinUtil the merlinUtil to set
	 */
    public void setMerlinUtil(IMerlinUtil merlinUtil) {
        this.merlinUtil = merlinUtil;
    }

    /**
	 * @return the liferayUtil
	 */
    public ILiferayUtil getLiferayUtil() {
        return liferayUtil;
    }

    /**
	 * @param liferayUtil the liferayUtil to set
	 */
    public void setLiferayUtil(ILiferayUtil liferayUtil) {
        this.liferayUtil = liferayUtil;
    }

    public AssignmentResultManager getAssignmentResultManager() {
        return assignmentResultManager;
    }

    public void setAssignmentResultManager(AssignmentResultManager assignmentResultManager) {
        this.assignmentResultManager = assignmentResultManager;
    }

    public IUserUtil getUserUtil() {
        return userUtil;
    }

    public void setUserUtil(IUserUtil userUtil) {
        this.userUtil = userUtil;
    }
}
