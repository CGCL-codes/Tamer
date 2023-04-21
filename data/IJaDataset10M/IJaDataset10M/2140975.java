package net.sourceforge.iwii.db.dev.persistence.dao.impl.project.artifact.phase1;

import net.sourceforge.iwii.db.dev.persistence.dao.api.projects.artifact.phase1.ISimilarProjectDAO;
import net.sourceforge.iwii.db.dev.persistence.dao.impl.AbstractDAO;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase1.SimilarProjectEntity;

/**
 * Implements ISimilarProjectDAO
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class SimilarProjectDAO extends AbstractDAO<SimilarProjectEntity, Long> implements ISimilarProjectDAO {

    @Override
    public Class<SimilarProjectEntity> getEntityClass() {
        return SimilarProjectEntity.class;
    }
}
