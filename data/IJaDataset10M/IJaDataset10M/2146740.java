package cz.muni.fi.pclis.service.peerEvaluation;

import cz.muni.fi.pclis.commons.service.GenericService;
import cz.muni.fi.pclis.domain.Course;
import cz.muni.fi.pclis.domain.Term;
import cz.muni.fi.pclis.domain.peerEvaluation.SelfEvaluationAssignment;
import java.util.List;

/**
 * User: Ľuboš Pecho
 * Date: 1.4.2010
 * Time: 16:17:59
 * To change this template use File | Settings | File Templates.
 */
public interface SelfEvaluationAssignmentService extends PeerEvaluationAssignmentService<SelfEvaluationAssignment> {

    SelfEvaluationAssignment getById(long id);

    SelfEvaluationAssignment searchById(long id);

    List<SelfEvaluationAssignment> getAll();

    SelfEvaluationAssignment create(SelfEvaluationAssignment entity);

    SelfEvaluationAssignment update(SelfEvaluationAssignment entity);

    void remove(SelfEvaluationAssignment entity);

    void removeById(long id);

    SelfEvaluationAssignment refresh(SelfEvaluationAssignment entity);

    List<SelfEvaluationAssignment> getByCourseAndTerm(Course course, Term term);
}
