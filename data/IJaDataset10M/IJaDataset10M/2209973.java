package cz.cvut.phone.core.data.dao;

import cz.cvut.phone.core.constants.Constants;
import cz.cvut.phone.core.constants.EJBQLConstants;
import cz.cvut.phone.core.data.filter.PhoneNumberFilter;
import cz.cvut.phone.core.data.entity.PhoneNumberEntity;
import cz.cvut.phone.core.data.sql.SQLFactoryAdministration;
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
public class PhoneNumberEntityDAOBean implements PhoneNumberEntityDAOLocal {

    @PersistenceContext
    private EntityManager em;

    private Logger log = Logger.getLogger(Constants.LOGGER_NAME);

    public List<PhoneNumberEntity> findByByNumberAndCompanyId(Integer companyID, String number) throws Exception {
        log.debug("Hledam PhoneNumberEntity [companyID, number]: [" + companyID + ", " + number + "]");
        Query q = em.createNamedQuery(EJBQLConstants.PHONENUMBERENTITY_FIND_ALL_BY_NUMBER_AND_COMPANYID);
        q.setParameter("companyID", companyID);
        q.setParameter("number", String.valueOf(number));
        return q.getResultList();
    }

    public List<PhoneNumberEntity> findByFilter(PhoneNumberFilter filter) throws Exception {
        log.debug("Hledam podle filtru PhoneNumberEntity [filter]: " + filter);
        Query q = em.createQuery(SQLFactoryAdministration.createPhoneSQL(filter));
        return q.getResultList();
    }

    public List<PhoneNumberEntity> findPhoneNumberDTOsFreeByCompanyId(Integer companyID) throws Exception {
        log.debug("Hledam volne (nepouzivane) PhoneNumberEntity [companyID]: [" + companyID + "]");
        Query q1 = em.createNamedQuery(EJBQLConstants.PHONENUMBERENTITY_FIND_ALL_USED_BY_COMPANYID2);
        q1.setParameter("companyID", companyID);
        List<PhoneNumberEntity> phoneEntsUsed = q1.getResultList();
        Query q2 = em.createQuery(SQLFactoryAdministration.createfreePhoneNumberSQL(phoneEntsUsed));
        q2.setParameter("companyID", companyID);
        q2.setParameter("active", Constants.ACTIVE_STATE_ACTIVE);
        return q2.getResultList();
    }

    public List<PhoneNumberEntity> findAllByCompanyId(Integer companyID) throws Exception {
        log.debug("Hledam PhoneNumberEntity [companyID]: [" + companyID + "]");
        Query q = em.createNamedQuery(EJBQLConstants.PHONENUMBERENTITY_FIND_ALL_BY_COMPANYID);
        q.setParameter("companyID", companyID);
        return q.getResultList();
    }
}
