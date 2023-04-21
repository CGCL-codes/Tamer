package cz.muni.fi.pclis.web.controllers.teacher.peerEvaluation;

import cz.muni.fi.pclis.commons.service.GenericService;
import cz.muni.fi.pclis.domain.peerEvaluation.PeerEvaluationAssignment;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Abstract controller handling removal of the evaluation assignments
 * User: Ľuboš Pecho
 * Date: 30.3.2010
 * Time: 20:10:49
 */
public abstract class AbstractRemovePeerEvaluationAssignmentController<Ass extends PeerEvaluationAssignment> extends AbstractController {

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long id = Long.parseLong(request.getParameter("id"));
        Ass assignment = getAssignmentService().getById(id);
        if (!assignment.getFilling().getStartTime().before(new Date())) {
            getAssignmentService().removeById(id);
        }
        return new ModelAndView("redirect:peer_evaluationTeacher.htm");
    }

    protected abstract GenericService<Ass> getAssignmentService();
}
