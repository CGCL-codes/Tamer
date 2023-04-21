package net.sourceforge.iwii.db.dev.persistence.dao.impl.project.artifact.phase4;

import net.sourceforge.iwii.db.dev.persistence.dao.api.projects.artifact.phase4.IBusinessConstraintDAO;
import net.sourceforge.iwii.db.dev.persistence.dao.impl.AbstractDAO;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase4.BusinessConstraintEntity;

/**
 * Implements IBusinessConstraintDAO
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class BusinessConstraintDAO extends AbstractDAO<BusinessConstraintEntity, Long> implements IBusinessConstraintDAO {

    @Override
    public Class<BusinessConstraintEntity> getEntityClass() {
        return BusinessConstraintEntity.class;
    }
}
