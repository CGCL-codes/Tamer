package org.plazmaforge.bsolution.base.common.services;

import java.util.List;
import org.plazmaforge.framework.config.object.IEntityConfig;
import org.plazmaforge.framework.config.object.IEntityTypeConfig;
import org.plazmaforge.framework.core.criteria.ICriteria;
import org.plazmaforge.framework.core.exception.DAOException;
import org.plazmaforge.framework.core.exception.FinderException;
import org.plazmaforge.framework.platform.ISystemEntityService;
import org.plazmaforge.framework.service.EntityService;

public interface SystemEntityService extends EntityService<IEntityConfig, String>, ISystemEntityService {

    List<IEntityConfig> findByEntityType(String entityType) throws DAOException;

    List<IEntityConfig> findByEntityTypeAndForm(String entityType) throws DAOException;

    List<IEntityConfig> findByCustom() throws DAOException;

    List<IEntityConfig> findByCustomAttribute() throws DAOException;

    List<IEntityConfig> findByCustomize(ICriteria criteria) throws DAOException;

    List<IEntityConfig> findByCustomize() throws DAOException;

    IEntityTypeConfig findEntityTypeById(String id) throws DAOException, FinderException;
}
