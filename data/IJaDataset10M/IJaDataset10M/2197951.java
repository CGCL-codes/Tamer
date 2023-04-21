package com.corratech.opensuite.persist.api;

import com.corratech.opensuite.api.security.User;
import com.corratech.opensuite.persist.businesscomponent.BusinessComponent;

/**
 * @author Aleksandr Kryzhak
 *
 */
public interface BusinessComponentDao extends AbstractDao<BusinessComponent> {

    /**
	 * @param owner
	 * @param name
	 * @param url
	 * @return BusinessComponent instance
	 */
    BusinessComponent create(User owner, String name, String url);

    /**
	 * Removes business component by id
	 * @param id	The BusinessComponent id
	 * @return boolean
	 */
    boolean removeBusinessComponent(int id);

    /**
	 * Removes business component by name
	 * @param name
	 * @return boolean
	 */
    boolean removeBusinessComponent(String name);

    /**
	 * @param businessComponent
	 * @return
	 */
    boolean isActive(com.corratech.opensuite.api.businesscomponent.BusinessComponent businessComponent);

    boolean updateBusinessComponent(int id, String name, int bcTypeId, String url, int endpointId);
}
