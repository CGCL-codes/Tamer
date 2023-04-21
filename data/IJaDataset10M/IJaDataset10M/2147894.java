package sb;

import core.PopularSearchEB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * Handles database calls to table PopularSearches
 *
 * @author jilveroa
 */
@Stateless
public class PopularSearchSessionBeanBean implements PopularSearchSessionBeanRemote {

    @PersistenceContext
    private EntityManager em;

    public List<PopularSearchEB> readAllPopularSearch() {
        Query q = em.createNamedQuery("PopularSearchEB.findAll");
        return (List<PopularSearchEB>) q.getResultList();
    }

    public void addSearch(String s) {
        Query q = em.createNamedQuery("PopularSearchEB.findByWord");
        q.setParameter("word", s);
        try {
            PopularSearchEB p = (PopularSearchEB) q.getSingleResult();
            int tmpOcc = p.getOccurances();
            p.setOccurances(tmpOcc + 1);
            em.merge(p);
        } catch (NoResultException n) {
            PopularSearchEB tmp = new PopularSearchEB();
            tmp.setWord(s);
            tmp.setOccurances(1);
            em.persist(tmp);
        }
    }
}
