package test.granite.spring.service.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.granite.tide.data.DataEnabled;
import org.granite.tide.data.DataEnabled.PublishMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.granite.spring.service.ObserveAllPublishAll;
import test.granite.spring.service.PeopleService;

/**
 * @author Sebastien Deleuze
 */
@Service("people")
@Transactional(readOnly = true)
@DataEnabled(topic = "addressBookTopic", params = ObserveAllPublishAll.class, publish = PublishMode.ON_SUCCESS)
public class PeopleServiceImpl implements PeopleService {

    @PersistenceContext
    protected EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public Map<String, Object> find(Map<String, Object> filter, int first, int max, String order, boolean desc) {
        EntityManager entityManager = getEntityManager();
        Map<String, Object> result = new HashMap<String, Object>(4);
        String from = "from Person e ";
        String where = "where lower(e.lastName) like :lastName ";
        String orderBy = order != null ? "order by e." + order + (desc ? " desc" : "") : "";
        String lastName = filter.containsKey("lastName") ? (String) filter.get("lastName") : "";
        Query qc = entityManager.createQuery("select count(e) " + from + where);
        qc.setParameter("lastName", "%" + lastName.toLowerCase() + "%");
        long resultCount = (Long) qc.getSingleResult();
        if (max == 0) max = 36;
        Query ql = entityManager.createQuery("select e " + from + where + orderBy);
        ql.setFirstResult(first);
        ql.setMaxResults(max);
        ql.setParameter("lastName", "%" + lastName.toLowerCase() + "%");
        List<?> resultList = ql.getResultList();
        result.put("firstResult", first);
        result.put("maxResults", max);
        result.put("resultCount", resultCount);
        result.put("resultList", resultList);
        return result;
    }
}
