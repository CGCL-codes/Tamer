package be.hoornaert.tom.spring.exercise6;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void delete(User user) {
        User x = get(user.getId());
        em.remove(x);
    }

    @Override
    public List<User> findByFirstLetterOfLastName(char letter) {
        return em.createQuery("Select u from User u where u.lastName like '" + letter + "%'").getResultList();
    }

    @Override
    public User get(Long id) {
        return (User) em.createQuery("Select u from User u where u.id = :id").setParameter("id", id).setMaxResults(1).getSingleResult();
    }

    @Override
    public List<User> getAll() {
        return em.createQuery("Select u from User u").getResultList();
    }

    @Transactional
    @Override
    public void save(User user) {
        em.persist(user);
    }
}
