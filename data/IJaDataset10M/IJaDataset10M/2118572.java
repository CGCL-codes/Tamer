package org.ejc.api.additionalinfo;

import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.naming.NamingException;
import org.ejc.exception.EJCPersistenceException;
import org.ejc.persist.model.AccomodationType;
import org.ejc.persist.model.AdditionalInfoTypeDesc;
import org.ejc.util.EJCUtils;
import org.hibernate.Session;

/**
 * @author zac
 * 
 * @ejb.bean type="Stateless" name="AdditionalInfoAPI"
 *           description="AdditionalInfoAPI session bean"
 *           local-jndi-name="org.ejc/AdditionalInfoAPILocalHome"
 *           jndi-name="org.ejc/AdditionalInfoAPIHome" view-type="both"
 * 
 * @ejb.ejb-ref ejb-name="AdditionalInfoAPI" ref-name="ejb/AdditionalInfoAPI"
 *              view-type="both"
 * 
 * @ejb.transaction type="RequiresNew"
 * @ejb.util generate = "physical"
 */
public abstract class AdditionalInfoAPIBean implements SessionBean {

    /**
	 * Return a List of Accomodation Types.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
    public Collection getAdditionalInfoTypes() throws NamingException, EJBException {
        Session hsession = null;
        try {
            hsession = EJCUtils.getHibernateSession();
            return hsession.createCriteria(AdditionalInfoTypeDesc.class).list();
        } catch (EJCPersistenceException epe) {
            epe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (hsession != null) {
                hsession.close();
            }
        }
        return new ArrayList();
    }
}
