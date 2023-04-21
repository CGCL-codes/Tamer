package cz.muni.fi.pclis.service.onlineConsultation;

import cz.muni.fi.pclis.commons.service.GenericServiceImpl;
import cz.muni.fi.pclis.dao.onlineConsultation.OnlineConsultationRulesDao;
import cz.muni.fi.pclis.domain.Course;
import cz.muni.fi.pclis.domain.Term;
import cz.muni.fi.pclis.domain.onlineConsultation.OnlineConsultationRules;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Ľuboš Pecho
 * Date: 27.2.2010
 * Time: 16:15:21
 *
 */
@Transactional
public class OnlineConsultationRulesServiceImpl implements OnlineConsultationRulesService {

    private GenericServiceImpl<OnlineConsultationRules, OnlineConsultationRulesDao> impl = new GenericServiceImpl<OnlineConsultationRules, OnlineConsultationRulesDao>(OnlineConsultationRules.class);

    public OnlineConsultationRules getById(long id) {
        return impl.getById(id);
    }

    public OnlineConsultationRules searchById(long id) {
        return impl.searchById(id);
    }

    public OnlineConsultationRules searchByCourseAndTerm(Course course, Term term) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("course", course);
        params.put("term", term);
        return (OnlineConsultationRules) impl.useDao().runNamedQueryForSingleResult("getRulesByCourseAndTerm", params);
    }

    public List<OnlineConsultationRules> getAll() {
        return impl.getAll();
    }

    public OnlineConsultationRules create(OnlineConsultationRules entity) {
        return impl.create(entity);
    }

    public OnlineConsultationRules update(OnlineConsultationRules entity) {
        return impl.update(entity);
    }

    public void remove(OnlineConsultationRules entity) {
        impl.remove(entity);
    }

    public void removeById(long id) {
        impl.removeById(id);
    }

    public OnlineConsultationRules refresh(OnlineConsultationRules entity) {
        return impl.refresh(entity);
    }

    public OnlineConsultationRulesDao useDao() {
        return impl.useDao();
    }

    public OnlineConsultationRulesDao getDao() {
        return impl.getDao();
    }

    public void setDao(OnlineConsultationRulesDao onlineConsultationRulesDao) {
        impl.setDao(onlineConsultationRulesDao);
    }
}
