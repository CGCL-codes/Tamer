package cz.muni.fi.pclis.service.onlineConsultation;

import cz.muni.fi.pclis.domain.Course;
import cz.muni.fi.pclis.domain.Term;
import cz.muni.fi.pclis.domain.User;
import cz.muni.fi.pclis.domain.onlineConsultation.OnlineConsultation;
import java.util.List;

/**
 * User: Ľuboš Pecho
 * Date: 27.2.2010
 * Time: 16:12:16
 *
 */
public interface OnlineConsultationService {

    OnlineConsultation getById(long id);

    OnlineConsultation searchById(long id);

    List<OnlineConsultation> getAll();

    OnlineConsultation create(OnlineConsultation entity);

    OnlineConsultation update(OnlineConsultation entity);

    void remove(OnlineConsultation entity);

    void removeById(long id);

    OnlineConsultation refresh(OnlineConsultation entity);

    List<OnlineConsultation> getByCourseAndTerm(Course course, Term term);

    void registerTeacher(long consultationId, User user);

    void registerStudent(long consultationId, User user);
}
