package org.plazmaforge.bsolution.carservice.server.services;

import org.plazmaforge.framework.service.hibernate.AbstractHibernateEntityService;
import org.plazmaforge.bsolution.carservice.common.beans.CarPaintType;
import org.plazmaforge.bsolution.carservice.common.services.CarPaintTypeService;

/**
 * 
 * @author Oleh Hapon
 * $Id: CarPaintTypeServiceImpl.java,v 1.2 2010/04/28 06:23:02 ohapon Exp $
 */
public class CarPaintTypeServiceImpl extends AbstractHibernateEntityService<CarPaintType, Integer> implements CarPaintTypeService {

    protected Class getEntityClass() {
        return CarPaintType.class;
    }
}
