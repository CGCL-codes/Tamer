package org.plazmaforge.bsolution.contact.server.services;

import org.plazmaforge.bsolution.contact.common.beans.AddressType;
import org.plazmaforge.bsolution.contact.common.services.AddressTypeService;
import org.plazmaforge.framework.service.hibernate.AbstractHibernateEntityService;

/**
 * @author hapon
 *
 */
public class AddressTypeServiceImpl extends AbstractHibernateEntityService<AddressType, Integer> implements AddressTypeService {

    public Class getEntityClass() {
        return AddressType.class;
    }
}
