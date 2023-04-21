package org.signserver.server.config.entities;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Entity Service class that acts as migration layer for
 * the old Home Interface for the GlobalConfigurationData Entity Bean
 * 
 * Contains about the same methods as the EJB 2 entity beans home interface.
 *
 * @version $Id: GlobalConfigurationDataService.java 2267 2012-03-23 08:20:31Z netmackan $
 */
public class GlobalConfigurationDataService {

    private EntityManager em;

    public GlobalConfigurationDataService(EntityManager em) {
        this.em = em;
    }

    public void setGlobalProperty(String completekey, String value) {
        GlobalConfigurationDataBean data = em.find(GlobalConfigurationDataBean.class, completekey);
        if (data == null) {
            data = new GlobalConfigurationDataBean();
            data.setPropertyKey(completekey);
            data.setPropertyValue(value);
            em.persist(data);
        } else {
            data.setPropertyValue(value);
        }
    }

    public boolean removeGlobalProperty(String completekey) {
        boolean retval = false;
        GlobalConfigurationDataBean data = em.find(GlobalConfigurationDataBean.class, completekey);
        if (data != null) {
            em.remove(data);
            retval = true;
        }
        return retval;
    }

    @SuppressWarnings("unchecked")
    public List<GlobalConfigurationDataBean> findAll() {
        Query query = em.createQuery("SELECT e from GlobalConfigurationDataBean e");
        return (List<GlobalConfigurationDataBean>) query.getResultList();
    }
}
