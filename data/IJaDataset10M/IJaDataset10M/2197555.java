package org.plazmaforge.bsolution.personality.server.services;

import org.plazmaforge.bsolution.personality.common.beans.EducationType;
import org.plazmaforge.bsolution.personality.common.services.EducationTypeService;
import org.plazmaforge.framework.service.hibernate.AbstractHibernateEntityService;

/**
 * @author hapon
 *
 */
public class EducationTypeServiceImpl extends AbstractHibernateEntityService<EducationType, Integer> implements EducationTypeService {

    protected Class getEntityClass() {
        return EducationType.class;
    }
}
