package cz.cvut.phone.core.data.dao;

import cz.cvut.phone.core.constants.Constants;
import cz.cvut.phone.core.constants.EJBQLConstants;
import cz.cvut.phone.core.data.entity.RoleEntity;
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
public class RoleEntityDAOBean implements RoleEntityDAOLocal {

    @PersistenceContext
    private EntityManager em;

    private Logger log = Logger.getLogger(Constants.LOGGER_NAME);

    public List<RoleEntity> findByCompanyID(Integer companyID) throws Exception {
        log.debug("Hledam RoleEntity [companyID]: [" + companyID + "]");
        Query q = em.createNamedQuery(EJBQLConstants.ROLEENTITY_FIND_BY_COMPANYID);
        q.setParameter("companyID", companyID);
        return q.getResultList();
    }

    public List<RoleEntity> findByPersonID(Integer personID) throws Exception {
        log.debug("Hledam RoleEntity [personID]: [" + personID + "]");
        Query q = em.createNamedQuery(EJBQLConstants.ROLEENTITY_FIND_BY_PERSONID);
        q.setParameter("personID", personID);
        return q.getResultList();
    }

    public List<RoleEntity> find(Integer companyID, String name) throws Exception {
        log.debug("Finding [type 1] RoleEntity. companyID: " + companyID);
        Query q = em.createNamedQuery(EJBQLConstants.ROLEENTITY_FIND_1);
        q.setParameter("companyID", companyID);
        q.setParameter("name", name);
        return q.getResultList();
    }
}
