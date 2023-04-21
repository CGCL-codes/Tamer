package org.simplecart.dao;

import net.sf.hibernate.HibernateException;
import org.simplecart.account.billing.InternetBillingDetails;

/**
 * @version     $LastChangedRevision$ $LastChangedDate$
 * @author      Daniel Watrous
 */
public class InternetBillingDetailsDAO extends BaseDAO {

    /** Creates a new instance of AdministratorDAO */
    public InternetBillingDetailsDAO() {
        super();
        DAOClassType = InternetBillingDetails.class;
    }

    /**
     * @return
     * @param id
     * @param lock
     */
    public InternetBillingDetails findById(Long id, boolean lock) throws HibernateException {
        InternetBillingDetails billingDetails = null;
        billingDetails = (InternetBillingDetails) abstractFindById(id, lock);
        return billingDetails;
    }
}
