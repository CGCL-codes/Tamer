package org.plazmaforge.bsolution.carservice.server.services;

import org.plazmaforge.framework.service.hibernate.AbstractHibernateEntityService;
import org.plazmaforge.bsolution.carservice.common.beans.CarClass;
import org.plazmaforge.bsolution.carservice.common.services.CarClassService;

/**
 * 
 * @author Oleh Hapon
 * $Id: CarClassServiceImpl.java,v 1.2 2010/04/28 06:23:02 ohapon Exp $
 */
public class CarClassServiceImpl extends AbstractHibernateEntityService<CarClass, Integer> implements CarClassService {

    protected Class getEntityClass() {
        return CarClass.class;
    }
}
