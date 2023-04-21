package net.zcarioca.zscrum.domain.dao.impl;

import java.util.Collections;
import java.util.List;
import net.zcarioca.zscrum.domain.Sprint;
import net.zcarioca.zscrum.domain.dao.SprintDAO;
import net.zcarioca.zscrum.domain.dao.exceptions.DAOReadException;
import net.zcarioca.zscrum.domain.dao.exceptions.DAOWriteException;

/**
 * Implementation of the {@link Sprint} entity DAO.
 *
 *
 * @author zcarioca
 */
public class SprintDAOImpl extends EntityDAOImpl implements SprintDAO {

    /**
    * {@inheritDoc}
    *
    * @see net.zcarioca.zscrum.domain.dao.EntityDAO#save(java.lang.Object)
    */
    @Override
    public void save(Sprint entity) throws DAOWriteException {
        saveEntity(entity);
    }

    /**
    * {@inheritDoc}
    *
    * @see net.zcarioca.zscrum.domain.dao.EntityDAO#delete(java.lang.Object)
    */
    @Override
    public void delete(Sprint entity) throws DAOWriteException {
        deleteEntity(entity);
    }

    /**
    * {@inheritDoc}
    *
    * @see net.zcarioca.zscrum.domain.dao.EntityDAO#loadAll()
    */
    @Override
    public List<Sprint> loadAll() throws DAOReadException {
        return loadAllEntities(Sprint.class);
    }

    /**
    * {@inheritDoc}
    *
    * @see net.zcarioca.zscrum.domain.dao.EntityDAO#loadById(int)
    */
    @Override
    public Sprint loadById(int entityId) throws DAOReadException {
        return loadEntityById(Sprint.class, entityId);
    }

    /**
    * {@inheritDoc}
    *
    * @see net.zcarioca.zscrum.domain.dao.SprintDAO#loadAllByReleaseId(int)
    */
    @Override
    public List<Sprint> loadAllByReleaseId(int releaseId) throws DAOReadException {
        List<Sprint> sprints = loadEntitiesByProperty(Sprint.class, "parent_id", releaseId);
        Collections.sort(sprints);
        return sprints;
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public List<Sprint> loadAllByProjectId(int projectId) throws DAOReadException {
        List<Sprint> sprints = loadEntitiesByQuery(Sprint.class, "project_sprints", projectId);
        Collections.sort(sprints);
        return sprints;
    }
}
