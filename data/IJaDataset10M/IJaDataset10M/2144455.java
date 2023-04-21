package org.plazmaforge.bsolution.carservice.server.services;

import org.plazmaforge.framework.service.hibernate.AbstractHibernateEntityService;
import org.plazmaforge.bsolution.carservice.common.beans.CarMark;
import org.plazmaforge.bsolution.carservice.common.services.CarMarkService;

/**
 * 
 * @author Oleh Hapon
 * $Id: CarMarkServiceImpl.java,v 1.2 2010/04/28 06:23:02 ohapon Exp $
 */
public class CarMarkServiceImpl extends AbstractHibernateEntityService<CarMark, Integer> implements CarMarkService {

    protected Class getEntityClass() {
        return CarMark.class;
    }
}
