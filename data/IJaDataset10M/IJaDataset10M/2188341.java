package org.plazmaforge.bsolution.base.server.services;

import org.plazmaforge.bsolution.base.common.beans.PeriodType;
import org.plazmaforge.bsolution.base.common.services.PeriodTypeService;
import org.plazmaforge.framework.service.hibernate.AbstractHibernateEntityService;

/**
 * @author hapon
 *
 */
public class PeriodTypeServiceImpl extends AbstractHibernateEntityService<PeriodType, Integer> implements PeriodTypeService {

    protected Class getEntityClass() {
        return PeriodType.class;
    }
}
