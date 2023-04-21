package net.larsbehnke.petclinicplus.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import net.larsbehnke.petclinicplus.model.User;
import net.larsbehnke.petclinicplus.security.CRUDUserDetailsService;
import org.acegisecurity.Authentication;
import org.acegisecurity.acl.AclEntry;
import org.acegisecurity.acl.AclProvider;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class EntityManagerSecurity implements AclProvider, CRUDUserDetailsService {

    @PersistenceContext
    private EntityManager em;

    private static Log log = LogFactory.getLog(EntityManagerSecurity.class);

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        Query query = em.createQuery("SELECT user FROM User user WHERE user.username = :username");
        query.setParameter("username", username);
        List<UserDetails> list = query.getResultList();
        if (list.size() > 1) {
            throw new UsernameNotFoundException("Ambigious user name: " + username);
        } else if (list.size() == 0) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return list.get(0);
    }

    /**
     * {@inheritDoc}
     */
    public void clearUsers() {
        Query query = em.createQuery("DELETE User user");
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    public Long countUsers() {
        Query query = em.createQuery("SELECT count(user) FROM User user");
        return (Long) query.getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    public void deleteUser(User user) {
        em.remove(user);
    }

    /**
     * {@inheritDoc}
     */
    public User storeUser(User user) {
        return em.merge(user);
    }

    public AclEntry[] getAcls(Object domainInstance) {
        log.warn("ACL store not supported yet.");
        return null;
    }

    public AclEntry[] getAcls(Object domainInstance, Authentication authentication) {
        log.warn("ACL store not supported yet.");
        return null;
    }

    public boolean supports(Object domainInstance) {
        log.warn("ACL store not supported yet.");
        return false;
    }
}
