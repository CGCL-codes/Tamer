package cz.cvut.phone.core.data.dao;

import cz.cvut.phone.core.constants.Constants;
import cz.cvut.phone.core.constants.EJBQLConstants;
import cz.cvut.phone.core.data.entity.SuperAdminEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author Frantisek Hradil
 */
@Stateless
public class SuperAdminEntityDAOBean implements SuperAdminEntityDAOLocal {

    @PersistenceContext
    private EntityManager em;

    private Logger log = Logger.getLogger(Constants.LOGGER_NAME);

    public List<SuperAdminEntity> findByUsernameAndPassForLogin(String username, String password) throws Exception {
        log.debug("Hledam SuperAdminEntity [username, password]: [" + username + ", " + password + "]");
        Query q = em.createNamedQuery(EJBQLConstants.SUPERADMINENTITY_FIND_BY_USERNAME_AND_PASS);
        q.setParameter("username", username);
        q.setParameter("password", password);
        return q.getResultList();
    }
}
