package net.sourceforge.iwii.db.dev.persistence.dao.impl.project.artifact.phase1;

import net.sourceforge.iwii.db.dev.persistence.dao.api.projects.artifact.phase1.IProjectSoftwareResourceDAO;
import net.sourceforge.iwii.db.dev.persistence.dao.impl.AbstractDAO;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase1.ProjectSoftwareResourceEntity;

/**
 * Implements IProjectSoftwareResourceDAO
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class ProjectSoftwareResourceDAO extends AbstractDAO<ProjectSoftwareResourceEntity, Long> implements IProjectSoftwareResourceDAO {

    @Override
    public Class<ProjectSoftwareResourceEntity> getEntityClass() {
        return ProjectSoftwareResourceEntity.class;
    }
}
