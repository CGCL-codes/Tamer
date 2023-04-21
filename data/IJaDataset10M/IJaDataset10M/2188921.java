package lv.ante.xwiki.testdigester.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lv.ante.xwiki.testdigester.domain.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserDAO {

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public void save(User user) {
        if (user.getId() <= 0) {
            em.persist(user);
        } else {
            em.merge(user);
        }
    }

    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        Query query = em.createNamedQuery("User.selectAllQuery");
        return query.getResultList();
    }

    public void deleteAll() {
        Query query = em.createNamedQuery("User.deleteAllQuery");
        query.executeUpdate();
    }
}
